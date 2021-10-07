package assembler;
import java.util.Map;
public class EquDirectives {
    
    public static EquDirective parse(String text) {
        EquDirective dir = new EquDirective(text);
        String[] tokens = text.split("\\s+", 3);
        String label = tokens[0].trim();
        if (label.endsWith(":"))
            label.substring(0, label.length() - 1);
        dir.setLabel(label);
        String operand = tokens[2].trim();
        dir.setOperand(operand);
        if (operand.startsWith("$-")) {
            // System.out.println("Label: " + label);
            // System.out.println("Operand: " + operand);
            Map<String, Symbol> symtable = Symbols.getMap();
            String name = operand.substring(2);
            // System.out.println("Name: " + name);
            // System.out.println(Symbols.view());
            Symbol symbol = symtable.get(name);
            byte[] bytes = symbol.getBytes();
            dir.setBytes(bytes);
        }
        return dir;                
    }
}
