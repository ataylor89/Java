package assembler2;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class Parser {

    private Map<String, Opcode> opcodes;
    private Map<String, Register> registers;
    private Map<String, Directive> directives;

    public Parser() {
        initOpcodes();
        initRegisters();
        initDirectives();
    }   

    private void initOpcodes() {
        opcodes = new HashMap<>();
        opcodes.put("mov", Opcode.MOV);
        opcodes.put("and", Opcode.AND);
        opcodes.put("or", Opcode.OR);
        opcodes.put("xor", Opcode.XOR);
        opcodes.put("syscall", Opcode.SYSCALL);
    }

    private void initRegisters() {
        registers = new HashMap<>();
        registers.put("rax", Register.RAX);
        registers.put("rdi", Register.RDI);
        registers.put("rsi", Register.RSI);
        registers.put("rdx", Register.RDX);
    }

    public AssemblyFile parse(File file) {
        AssemblyFile assemblyFile = new AssemblyFile();
        assemblyFile.setFile(file);
        String code = readFile(file);
        assemblyFile.setCode(code);
        String text = parseText(code);
        assemblyFile.setText(text);
        String data = parseData(code);
        assemblyFile.setData(data);
        String bss = parseBss(code);
        assemblyFile.setBss(bss);
        String[] globals = parseGlobals(code);
        assemblyFile.setGlobals(globals);
        String[] externs = parseExterns(code);
        assemblyFile.setExterns(externs);
        String[] instructions = parseInstructions(code);
        assemblyFile.setInstructions(instructions);
        String[] dataDirectives = parseDataDirectives(code);
        assemblyFile.setDataDirectives(dataDirectives);
        String[] bssDirectives = parseBssDirectives(code);
        assemblyFile.setBssDirectives();
        String[] globals = parseGlobals(code);
        assemblyFile.setGlobals(globals);
        String[] externs = parseExterns(code);
        assemblyFile.setExterns(externs);
        SymbolTable symbols = parseSymbols(code);
        assemblyFile.setSymbolTable(symbols);
    }

    public String parseText(String code) {
        int start = code.indexOf("section .text");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String parseData(String code) {
        int start = code.indexOf("section .data");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String parseBss(String code) {
        int start = code.indexOf("section .bss");
        int end = code.indexOf("section", start+13);
        if (end < 0)
            end = code.length();
        return code.substring(start, end);
    }

    public String[] parseGlobals(String code) {
        List<String> lst = new ArrayList<>();
        int start = code.indexOf("global");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > 0) {
            String global = code.substring(start, end);
            lst.add(global);
            start = code.indexOf("global", end);
            end = code.indexOf("\n", start);
        }
        String[] globals = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++)
            globals[i] = lst.get(i);
        return globals;
    }

    public String[] parseExterns(String code) {
        List<String> lst = new ArrayList<>();
        int start = code.indexOf("extern");
        int end = code.indexOf("\n", start);
        while (start > 0 && end > 0) {
            String extern = code.substring(start, end);
            lst.add(extern);
            start = code.indexOf("extern", end);
            end = code.indexof("\n", start);
        }
        String[] externs = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++)
            externs[i] = lst.get(i);
        return externs;
    }
 
    public String[] parseInstructions(String code) {
        List<String> lst = new ArrayList<>();
        String text = parseText(code);
        int start = text.indexOf("\n") + 1;
        int end = text.IndexOf("\n", start);
        if (end < 0)
            end = text.length();
        while (start > 0 && end > start) {
            String instruction = text.substring(start, end);
            lst.add(instruction);
            start = end + 1;
            end = text.IndexOf("\n", start);
            if (end < 0)
                end = text.length();
        }
        String[] instructions = new String[lst.size()];
        for (int i = 0; i < instructions.length; i++)
            instructions[i] = lst.get(i);
        return instructions;
    }

    public String[] parseDataDirectives(String code) {
        List<String> lst = new ArrayList<>();
        String data = parseData(code);
        int start = data.indexOf("\n") + 1;
        int end = data.indexOf("\n", start);
        if (end < 0)
            end = data.length();
        while (start > 0 && end > start) {
            String dataDirective = data.substring(start, end);
            lst.add(dataDirective);
            start = end + 1;
            end = data.indexOf("\n", start);
            if (end < 0)
                end = data.length();
        }
        String[] dataDirectives = new String[lst.size()];
        for (int i = 0; i < dataDirectives.length; i++)
            dataDirectives[i] = lst.get(i);
        return dataDirectives;
    }

    public String[] parseBssDirectives(String code) {
        List<String> lst = new ArrayList<>();
        String bss = parseBss(code);
        int start = bss.indexOf("\n") + 1;
        int end = bss.indexOf("\n", end);
        if (end < 0)
            end = bss.length();
        while (start > 0 && end > start) {
            String bssDirective = bss.substring(start, end);
            lst.add(bssDirective);
            start = end + 1;
            end = bss.indexOf("\n", start);
            if (end < 0)
                end = bss.length();
        }
        String[] bssDirectives = new String[lst.size()];
        for (int i = 0; i < bssDirectives.length; i++)
            bssDirectives[i] = lst.get(i);
        return bssDirectives;
    }
 
    public byte[] parseInstruction(String instruction) {
        String[] tokens = instruction.split("\\s+", 3);
        Opcode opcode = parseOpcode(tokens[0]);
        switch (opcode) {
            case MOV:
                return parseMovInstruction(instruction);
            default:
                System.err.println("Unknown opcode: " + tokens[0]);
                return new byte[] {};
        }
    }

    public Opcode parseOpcode(String opcode) {
        if (opcodes.containsKey(opcode))
            return opcodes.get(opcode);
        return null;
    }

    public Operand parseOperand(String operand) {
        if (registers.containsKey(operand))
            return Operand.REGISTER;
        if (symbols.containsKey(operand))
            return Operand.SYMBOL;
        try {
            Long l = Long.decode(operand);
            return Operand.IMMEDIATE_VALUE;
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return null;
    }

    public byte[] parseRegister(String register) {
        if (!registers.containsKey(register)) 
            return new byte[] {};

        Register register = registers.get(register);
        return register.getBytes();
    }

    public byte[] parseImmediateValue(String immediateValue) {
        try {
            Long number = Long.decode(immediateValue);
            return litleendian(number);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return new byte[] {};
    }

    public byte[] parseMovInstrution(String instruction) {
        List<Byte> lst = new ArrayList<>();
        String[] tokens = instruction.split("\\s+", 3);
        if (!tokens[0].equals("mov") || tokens.length != 3)   
            return new byte[] {};
        Operand op1 = parseOperand(tokens[1]);
        Operand op2 = parseOperand(tokens[2]);
        switch (op1) {
            case REGISTER:
                byte[] register = parseRegister(tokens[1]);
                for (byte b : register)
                    lst.add(b);
                break;
        }
        switch (op2) {
            case IMMEDIATE_VALUE:
                byte[] number = parseImmediateValue(tokens[2]);
                for (byte b : number)
                    lst.add(b);
                break;
        }
        byte[] bytes = new byte[lst.size()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = lst.get(i);
        return bytes;
    }   
  
	public byte[] littleendian(int val) {
		byte[] bytes = new byte[8];
		long bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	} 

    public String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

}
