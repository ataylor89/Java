package assembler5;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
public class Assembler {
    
    private Parser parser;

    public Assembler() {
        parser = new Parser();
    }

    public ObjectFile compile(File file) {
        ObjectFile objectFile = new ObjectFile();
        AssemblyFile assemblyFile = parser.parse(file);
        String[] dataDirectives = assemblyFile.getDataDirectives();
        compileDataSection(dataDirectives, objectFile);
        String[] instructions = assemblyFile.getInstructions();
        compileTextSection(instructions, objectFile);
        return objectFile;
    }
    
    public void compileTextSection(String[] instructions, ObjectFile objectFile) {
        for (int i = 0; i < instructions.length; i++) {
            String[] tokens = instructions[i].split("\\s+", 4);
            String label = null;
            Opcode opcode = null;
            String operand1 = null;
            String operand2 = null;
            try {
                if (tokens[0].endsWith(":")) {
                    label = tokens[0];
                    if (tokens.length > 1)
                        opcode = Opcode.valueOf(tokens[1].toUpperCase());
                    if (tokens.length > 2) 
                        operand1 = tokens[2];
                    if (tokens.length > 3)
                        operand2 = tokens[3];
                }
                else {
                    opcode = Opcode.valueOf(tokens[0].toUpperCase());
                    if (tokens.length > 1)
                        operand1 = tokens[1];
                    if (tokens.length > 2)
                        operand2 = tokens[2];
                }
                if (operand1 != null && operand1.endsWith(","))
                    operand1 = operand1.substring(0, operand1.length()-1);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            }
            if (opcode == null)
                continue;
            switch (opcode) {
                case MOV:
                    compileMov(operand1, operand2, objectFile);
                    break;
                case XOR:
                    compileXor(operand1, operand2, objectFile);
                    break;
                case SYSCALL:
                    compileSyscall(objectFile);
                    break;
            }
        }
    }
    
    public void compileMov(String operand1, String operand2, ObjectFile objectFile) {
        ByteArray codeSection = objectFile.getCodeSection();
        Map<String, Object> symbols = objectFile.getSymbols();
        OperandType type1 = getOperandType(operand1, symbols);
        OperandType type2 = getOperandType(operand2, symbols);
        if (type1 == OperandType.REGISTER && type2 == OperandType.IMMEDIATE_VALUE) {
            Register register = Registers.map().get(operand1);
            long num = Long.decode(operand2);
            int size = (num > 0xFFFFFFFFL) ? 8 : 4;
            codeSection.addBytes(getMovCode(register, size));
            codeSection.addBytes(Bytes.littleendian(num, size));
        }
        else if (type1 == OperandType.REGISTER && type2 == OperandType.SYMBOL) {
            Register register = Registers.map().get(operand1);
            Object value = symbols.get(operand2);
            if (value instanceof Long) {
                Long num = (Long) value;
                codeSection.addBytes(getMovCode(register, 8));
                codeSection.addBytes(Bytes.littleendian(num, 8));
            }
            if (value instanceof Integer) {
                Integer i = (Integer) value;
                codeSection.addBytes(getMovCode(register, 4));
                codeSection.addBytes(Bytes.littleendian(i, 4));
            }
        }
    }

    public byte[] getMovCode(Register register, int size) {
        switch (register) {
            case RAX:
                if (size == 8)
                    return new byte[] {(byte) 0x48, (byte) 0xb8};
            case EAX:
                return new byte[] {(byte) 0xb8};
            case RDX:
                if (size == 8)
                    return new byte[] {(byte) 0x48, (byte) 0xba};
            case EDX:
                return new byte[] {(byte) 0xba};
            case RSI:
                if (size == 8)
                    return new byte[] {(byte) 0x48, (byte) 0xbe};
            case ESI:
                return new byte[] {(byte) 0xbe};
            case RDI:
                if (size == 8)
                    return new byte[] {(byte) 0x48, (byte) 0xbf};
            case EDI:
                return new byte[] {(byte) 0xbf};
        }
        return new byte[] {};
    }

    public void compileXor(String operand1, String operand2, ObjectFile objectFile) {
        ByteArray codeSection = objectFile.getCodeSection();
        Map<String, Object> symbols = objectFile.getSymbols();
        OperandType type1 = getOperandType(operand1, symbols);
        OperandType type2 = getOperandType(operand2, symbols);
        if (type1 == OperandType.REGISTER && type2 == OperandType.REGISTER) {
            Register register1 = Registers.map().get(operand1);
            Register register2 = Registers.map().get(operand2);
            if (register1 == Register.RAX && register2 == Register.RAX) 
                codeSection.addBytes(new byte[] {(byte) 0x48, (byte) 0x31, (byte) 0xc0});
            else if (register1 == Register.RDI && register2 == Register.RDI)
                codeSection.addBytes(new byte[] {(byte) 0x48, (byte) 0x31, (byte) 0xff});
            else if (register1 == Register.RSI && register2 == Register.RSI)
                codeSection.addBytes(new byte[] {(byte) 0x48, (byte) 0x31, (byte) 0xf6});
            else if (register1 == Register.RDX && register2 == Register.RDX) 
                codeSection.addBytes(new byte[] {(byte) 0x48, (byte) 0x31, (byte) 0xd2});
            // c0 ff f6 d2
        }
    }

    public void compileSyscall(ObjectFile objectFile) {
        // 0f 05
        ByteArray codeSection = objectFile.getCodeSection();
        codeSection.addByte((byte) 0x0f);
        codeSection.addByte((byte) 0x05);
    }

    public void compileDataSection(String[] directives, ObjectFile objectFile) {
        ByteArray dataSection = objectFile.getDataSection();
        Map<String, Object> symbols = objectFile.getSymbols();
        for (int i = 0; i < directives.length; i++) {
            String[] tokens = directives[i].split("\\s+", 3);
            String label = tokens[0];
            if (label.endsWith(":"))
                label = label.substring(0, label.length()-1);
            String type = tokens[1];
            String data = tokens[2];
            Directive directive = null;
            try {
                directive = Directive.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println(e);
                continue;
            }
            long index = (long) dataSection.getIndex();
            switch (directive) {
                case DB:
                    String[] constants = data.split(",");
                    for (String constant : constants) {
                        if (constant.startsWith("'") && constant.endsWith("'")) {
                            constant = constant.substring(1, constant.length()-1);
                            dataSection.addBytes(constant.getBytes());
                        }
                        else if (constant.startsWith("\"") && constant.endsWith("\"")) {
                            constant = constant.substring(1, constant.length()-1);
                            dataSection.addBytes(constant.getBytes());
                        }
                        else {
                            try {
                                Long num = Long.decode(constant);
                                int size = Bytes.size(num);
                                byte[] bytes = Bytes.littleendian(num, size);
                                dataSection.addBytes(bytes);
                            } catch (NumberFormatException e) {
                                System.err.println(e);
                            }    
                        }   
                    }
                    symbols.put(label, index); 
                    break;
                case EQU:
                    if (data.startsWith("$-")) {
                        int start = ((Long) symbols.get(data.substring(2))).intValue();
                        byte[] bytes = dataSection.getBytes();
                        int end = start;
                        while (end < bytes.length - 1 && bytes[end] != 0x00)
                            end++;
                        int value = end - start + 1;
                        symbols.put(label, value);
                    }
                    else {
                        try {
                            Long value = Long.decode(data);
                            symbols.put(label, value);
                        } catch (NumberFormatException e) {
                            System.err.println(e);
                        }   
                    } 
                    break;
            }       
        }
    }

    public OperandType getOperandType(String operand, Map<String, Object> symbols) {
        if (operand == null)
            return null;
        if (symbols.containsKey(operand))
            return OperandType.SYMBOL;
        if (Registers.map().containsKey(operand))
            return OperandType.REGISTER;
        try {
            Long num = Long.decode(operand);
            return OperandType.IMMEDIATE_VALUE;
        } catch (NumberFormatException e) {
            System.err.println(e);
        }       
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java assembler5.Assembler <sourcefile.asm> <objectfile.o>");
            return;
        }
        Assembler assembler = new Assembler();
        File src = new File(args[0]);
        ObjectFile objectFile = assembler.compile(src);
        System.out.println(objectFile);
    }
}
