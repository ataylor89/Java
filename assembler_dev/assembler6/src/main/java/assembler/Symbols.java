package assembler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrewtaylor
 */
public class Symbols {
    
    public static List<Symbol> list = new ArrayList<>();
    public static Map<String, Symbol> map = new HashMap<>();
    public static int offset = 0;
    public static String stringTable;
    
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
            
    public static void init(AssemblyFile assemblyFile) {      
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
            Symbols.list.add(symbol);
            Symbols.map.put(name, symbol);
        }
        for (String global : assemblyFile.getGlobals()) {
            Symbol symbol = new Symbol();
            String name = global.split("\\s+")[1].trim();
            symbol.setName(name);
            symbol.setIndex(index++);
            symbol.setStrx(strx);
            strx += name.length() + 1;
            symbol.setType(SymbolType.GLOBAL);
            Symbols.list.add(symbol);
            Symbols.map.put(name, symbol);
        }
        String[] instructions = assemblyFile.getInstructions();
        for (int i = 0; i < instructions.length; i++) {
            String[] tokens = instructions[i].split("\\s+", 4);
            if (tokens[0].endsWith(":")) {
                String label = tokens[0].substring(0, tokens[0].length()-1).trim();
                if (!Symbols.map.containsKey(label)) {
                    Symbol symbol = new Symbol();
                    symbol.setName(label);
                    symbol.setIndex(index++);
                    symbol.setStrx(strx);
                    strx += label.length() + 1;
                    symbol.setType(SymbolType.TEXT);
                    Symbols.list.add(symbol);
                    Symbols.map.put(label, symbol);
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
            symbol.setStrx(strx += label.length() + 1);
            switch (opcode) {
                case DB:
                    String value = (String) Expressions.eval(operand);
                    symbol.setValue((long) Symbols.offset);
                    symbol.setSize(value.length());
                    symbol.setType(SymbolType.DATA);
                    Symbols.offset += value.length();
                    break;
                case EQU:
                    Integer num = (Integer) Expressions.eval(operand);
                    symbol.setValue(num.longValue());
                    symbol.setType(SymbolType.ABSOLUTE);
                    break;
                    
            }
            Symbols.list.add(symbol);
            Symbols.map.put(label, symbol);
        }
        StringBuilder sb = new StringBuilder();
        sb.append('\0');
        for (Symbol symbol : list) {
            sb.append(symbol.getName());
            sb.append('\0');
        }
        stringTable = sb.toString();
    }
    
    public static boolean isSymbol(String expression) {
        return map.containsKey(expression);
    }
}
