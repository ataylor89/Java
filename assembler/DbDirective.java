package assembler;
public class DbDirective extends Directive {
    
    public DbDirective() {}

    public DbDirective(String text) {
        super(text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Directive: " + text);
        sb.append("\n");
        sb.append("Label: " + label);
        sb.append("\n");
        sb.append("Operand: " + operand);
        sb.append("\n");
        sb.append("Bytes: " + Bytes.hexstring(bytes));
        return sb.toString();
    }       
}
