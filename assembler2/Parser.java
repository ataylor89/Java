package assembler2;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
public class Parser {

    private Map<String, Opcode> opcodes;
    private Map<String, Register> registers;
    private Map<String, Directive> directives;
    
    public Parser() {
        initMaps();
    }   

    private void initMaps() {
        opcodes = new HashMap<>();
        opcodes.put("mov", Opcode.MOV);
        opcodes.put("and", Opcode.AND);
        opcodes.put("or", Opcode.OR);
        opcodes.put("xor", Opcode.XOR);
        opcodes.put("syscall", Opcode.SYSCALL);
        registers = new HashMap<>();
        registers.put("rax", Register.RAX);
        registers.put("rdi", Register.RDI);
        registers.put("rsi", Register.RSI);
        registers.put("rdx", Register.RDX);
        directives = new HashMap<>();
        directives.put("db", Directive.DB);
        directives.put("dw", Directive.DW);
        directives.put("dd", Directive.DD);
        directives.put("dq", Directive.DQ);
        directives.put("resb", Directive.RESB);
        directives.put("resw", Directive.RESW);
        directives.put("resd", Directive.RESD);
        directives.put("resq", Directive.RESQ);
        directives.put("equ", Directive.EQU);
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
        assemblyFile.setBssDirectives(bssDirectives);
        SymbolTable symbols = parseSymbols(code);
        assemblyFile.setSymbolTable(symbols);
        return assemblyFile;
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

    public String parseText(String code) {
        int start = code.indexOf("section .text");
        int end = code.indexOf("section", start+13) - 1;
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
        int end = code.indexOf("section", start);
        if (end < 0)
            end = code.length();
        if (start > 0 && end > start)
            return code.substring(start, end);
        return null;
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
            end = code.indexOf("\n", start);
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
        int end = text.indexOf("\n", start);
        if (end < 0)
            end = text.length();
        while (start > 0 && end > start) {
            String instruction = text.substring(start, end);
            lst.add(instruction);
            start = end + 1;
            end = text.indexOf("\n", start);
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
        if (bss == null)
            return null;
        int start = bss.indexOf("\n") + 1;
        int end = bss.indexOf("\n", start);
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
 
    public Opcode parseOpcode(String opcode) {
        if (!opcodes.containsKey(opcode))
            return null;
        return opcodes.get(opcode);
    }

    public Operand parseOperand(String operand, SymbolTable symbolTable) {
        if (registers.containsKey(operand))
            return Operand.REGISTER;
        if (symbolTable.getMap().containsKey(operand))
            return Operand.SYMBOL;
        try {
            Long l = Long.decode(operand);
            return Operand.IMMEDIATE_VALUE;
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return null;
    }

    public Directive parseDirectiveType(String type) {
        if (!directives.containsKey(type))
            return null;
        return directives.get(type);
    }

    public Register parseRegister(String register) {
        if (!registers.containsKey(register)) 
            return null;
        return registers.get(register);
    }

    public byte[] parseImmediateValue(String immediateValue) {
        try {
            Long number = Long.decode(immediateValue);
            return littleendian(number);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return new byte[] {};
    }  
  
	public byte[] littleendian(long val) {
		byte[] bytes = new byte[8];
		long bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	} 

    public String hexstring(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%x", bytes[i]));
            if (i < bytes.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    public SymbolTable parseSymbols(String code) {
        SymbolTable symbolTable = new SymbolTable();
        String[] dataDirectives = parseDataDirectives(code);
        for (int i = 0; i < dataDirectives.length; i++) {
            String[] tokens = dataDirectives[i].split("\\s+", 3);
            Directive directive = parseDirectiveType(tokens[1]); 
            Symbol symbol = null;
            String label = null;
            byte[] bytes = null;
            String hexstring = null;
            switch (directive) {
                case DB:
                    symbol = new Symbol();
                    label = parseLabel(tokens[0]);
                    symbol.setName(label);
                    symbol.setValue(tokens[2]);
                    bytes = parseDb(dataDirectives[i]);
                    symbol.setBytes(bytes);
                    hexstring = hexstring(bytes);
                    symbol.setHexString(hexstring);
                    symbolTable.getList().add(symbol);
                    symbolTable.getMap().put(label, symbol);
                    break;
                case EQU:
                    symbol = new Symbol();
                    label = parseLabel(tokens[0]);
                    symbol.setName(label);
                    symbol.setValue(tokens[2]);
                    bytes = parseEqu(dataDirectives[i], symbolTable);
                    symbol.setBytes(bytes);
                    hexstring = hexstring(bytes);
                    symbol.setHexString(hexstring);
                    symbolTable.getList().add(symbol);
                    symbolTable.getMap().put(label, symbol);
                    break;
            }        
        }
        return symbolTable;
    }

    public String parseLabel(String label) {
        if (label != null && label.endsWith(":"))
            return label.substring(0, label.length()-1);
        return null;
    }

    public byte[] parseDb(String directive) {
        ByteArray byteArray = new ByteArray();
        String[] tokens = directive.split("\\s+", 3);
        String[] constants = tokens[2].split(",");
        for (String constant : constants) {
            if (constant.startsWith("'") && constant.endsWith("'")) {
                constant = constant.substring(1, constant.length()-1);
                byteArray.addBytes(constant.getBytes());
            }
            else if (constant.startsWith("\"") && constant.endsWith("\"")) {
                constant = constant.substring(1, constant.length()-1);
                byteArray.addBytes(constant.getBytes());
            }
            else {
                try {
                    Long num = Long.decode(constant);
                    byteArray.addBytes(littleendian(num));
                } catch (NumberFormatException e) {
                    System.err.println(e);
                }
            }
        }
        return byteArray.getBytes();
    }

    public byte[] parseEqu(String directive, SymbolTable symbolTable) {
        String[] tokens = directive.split("\\s+", 3);
        String label = tokens[0];
        String value = tokens[2];
        if (value.startsWith("$-")) {
            String name = value.substring(2);
            byte[] bytes = symbolTable.getMap().get(name).getBytes();
            int val = bytes.length;
            return littleendian(val);
        }
        if (value.startsWith("'") && value.endsWith("'")) {
            String charConstant = value.substring(1, value.length()-1);
            return charConstant.getBytes();
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            String stringConstant = value.substring(1, value.length()-1);
            return stringConstant.getBytes();
        }
        try {
            Long num = Long.decode(value);
            return littleendian(num);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return new byte[] {};
    }   

    public static void main(String[] args) {
        File file = new File(args[0]);
        Parser parser = new Parser();
        AssemblyFile af = parser.parse(file);
        System.out.println(af);
    }
}
