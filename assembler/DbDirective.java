package assembler;
public class DbDirective extends Directive {
    
    private String label;
    private String operand;
    private byte[] bytes;

    public DbDirective() {}

    public DbDirective(String text) {
        super(text);
    }

    /*
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getOperand() {
        return operand;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void addByte(byte b) {
        if (index + 1 > bytes.length)
            resize(1000);
        bytes[index] = b;
        index++;
    }

    public void addBytes(byte[] b) {
        if (index + b.length > bytes.length) 
            resize(1000);
        for (int i = 0; i < b.length; i++) {
            bytes[index] = b[i];
            index++;
        }    
    }

    public void resize(int n) {
        byte[] b = new byte[bytes.length + n];
        for (int i = 0; i < bytes.length; i++)
            b[i] = bytes[i];
        bytes = b;
    }
    */
    
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
