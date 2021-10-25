package assembler;
public class Symbol extends Operand {
    
    private String value;
    private int offset;

    public Symbol() {
        super(SYMBOL);
    }
    
    public Symbol(String name, String value, int offset, byte[] bytes) {
        super(name, bytes, SYMBOL);
        this.value = value;
        this.offset = offset;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {   
		StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name);
        sb.append("\n");
        sb.append("Type: " + type);
        sb.append("\n");
        sb.append("Value: " + value);
        sb.append("\n");
        sb.append("Bytes: " + Bytes.hexstring(bytes));
        sb.append("\n");
        return sb.toString();
    }
}
