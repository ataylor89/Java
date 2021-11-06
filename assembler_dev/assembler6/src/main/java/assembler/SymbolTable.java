package assembler;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewtaylor
 */
public class SymbolTable {
    
    private List<Symbol> list;
    private Map<String, Symbol> map;
    private int offset;
    
    private SymbolTable() {
        list = new ArrayList<>();
        map = new HashMap<>();
        offset = 0;
    }
    
    public SymbolTable(File file) {
        this();
        Parser parser = new Parser();
        AssemblyFile assemblyFile = parser.parse(file);
        init(assemblyFile);
    }
    
    public SymbolTable(AssemblyFile assemblyFile) {
        this();
        init(assemblyFile);
    }
    
    class SortOrders {
        public static Comparator<Symbol> symTable = (Symbol s1, Symbol s2) -> {
            return s1.getType().compareTo(s2.getType());
        };
        public static Comparator<Symbol> stringTable = (Symbol s1, Symbol s2) -> {
            Integer i1 = s1.getIndex();
            Integer i2 = s2.getIndex();
            return i1.compareTo(i2);
        };
    }

    /**
     * @return the list
     */
    public List<Symbol> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<Symbol> list) {
        this.list = list;
    }

    /**
     * @return the map
     */
    public Map<String, Symbol> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<String, Symbol> map) {
        this.map = map;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
            
    private void init(AssemblyFile assemblyFile) {      
        int index = 0;
        int strx = 1;
        for (String extern : assemblyFile.getExterns()) {
            Symbol symbol = new Symbol();
            String name = extern.split("\\s+")[1].trim();
            symbol.setName(name);
            symbol.setIndex(index++);
            symbol.setStrx(strx);
            strx += name.length() + 1;
            symbol.setType(SymbolType.EXTERN);
            getList().add(symbol);
            getMap().put(name, symbol);
        }
        for (String global : assemblyFile.getGlobals()) {
            Symbol symbol = new Symbol();
            String name = global.split("\\s+")[1].trim();
            symbol.setName(name);
            symbol.setIndex(index++);
            symbol.setStrx(strx);
            strx += name.length() + 1;
            symbol.setType(SymbolType.GLOBAL);
            getList().add(symbol);
            getMap().put(name, symbol);
        }
        String[] instructions = assemblyFile.getInstructions();
        for (int i = 0; i < instructions.length; i++) {
            String[] tokens = instructions[i].split("\\s+", 4);
            if (tokens[0].endsWith(":")) {
                String label = tokens[0].substring(0, tokens[0].length()-1).trim();
                if (!map.containsKey(label)) {
                    Symbol symbol = new Symbol();
                    symbol.setName(label);
                    symbol.setIndex(index++);
                    symbol.setStrx(strx);
                    strx += label.length() + 1;
                    symbol.setType(SymbolType.TEXT);
                    getList().add(symbol);
                    getMap().put(label, symbol);
                }
            }
        }
        String[] directives = assemblyFile.getDataDirectives();
        for (int i = 0; i < directives.length; i++) {
            DataDirective directive = new DataDirective(directives[i]);
            Pseudoopcode opcode = Pseudoopcode.parse(directive.getOpcode());
            String label = directive.getLabel();
            String operand = directive.getOperand();  
            Symbol symbol = new Symbol();
            symbol.setName(label);
            symbol.setIndex(index++);
            symbol.setStrx(strx);
            strx += label.length() + 1;
            switch (opcode) {
                case DB:
                    String value = (String) new Expression(operand, this).getValue();
                    symbol.setValue((long) offset);
                    symbol.setSize(value.length());
                    symbol.setType(SymbolType.DATA);
                    offset += value.length();
                    break;
                case EQU:
                    Integer num = (Integer) new Expression(operand, this).getValue();
                    symbol.setValue(num.longValue());
                    symbol.setType(SymbolType.ABSOLUTE);
                    break;
                    
            }
            list.add(symbol);
            map.put(label, symbol);
        }
    }
    
    public boolean isSymbol(String expression) {
        return getMap().containsKey(expression);
    }
}
