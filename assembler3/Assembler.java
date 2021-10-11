package assembler3;
public class Assembler {

    public ObjectFile compile(String code) {
    
    }

    public SymbolTable compileSymbolTable(String code) {
        SymbolTable symbolTable = new SymbolTable();
        int offset = 0;
        String[] dataDirectives = parseDataDirectives(code);
        for (int i = 0; i < dataDirectives.length; i++) {
            String[] tokens = dataDirectives[i].split("\\s+", 3);
            if (tokens.length < 3)  
                continue;
            Opcode opcode = null;
            try {
                opcode = Opcode.valueOf(tokens[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println(e);
                continue;
            }
            Symbol symbol = null;
            String name = null;
            String value = null;
            byte[] bytes = null;
            switch (opcode) {
                case DB:
                    symbol = new Symbol();
                    name = parseLabel(tokens[0]);
                    symbol.setName(name);
                    symbol.setValue(tokens[2]);
                    bytes = parseDb(dataDirectives[i]);
                    symbol.setBytes(bytes);
                    symbol.setOffset(offset);
                    offset += bytes.length;
                    symbolTable.getList().add(symbol);
                    symbolTable.getMap().put(label, symbol);
                    break;
                case EQU:
                    symbol = new Symbol();
                    name = parseLabel(tokens[0]);
                    symbol.setName(name);
                    symbol.setValue(tokens[2]);
                    bytes = parseEqu(dataDirectives[i], symbolTable);
                    symbol.setBytes(bytes);
                    symbol.setOffset(offset);
                    offset += bytes.length;
                    symbolTable.getList().add(symbol);
                    symbolTable.getMap().put(label, symbol);
                    break;
            }        
        }
        return symbolTable;
    }

    public byte[] compileDbDirective(String directive) {
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

    public byte[] compileEquDirective(String directive, SymbolTable symbolTable) {
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

    public TextSection compileTextSection(String code, SymbolTable symbolTable) {

    }

    public DataSection compileDataSection(String code, SymbolTable symbolTable) {

    }

    public BssSection compileBssSection(String code, SymbolTable symbolTable) {

    }

    public List<String> compileGlobals(String code) {

    }

    public List<String> compileExterns(String code) {
    
    }
}
