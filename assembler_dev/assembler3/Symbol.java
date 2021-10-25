package assembler3;
public class Symbol {
    private String name;
    private String value;
    private byte[] bytes;
    private int offset;

    public Symbol() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;        
    }

    public String getValue() {
        return value;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
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
        sb.append(String.format("%s\t%s\t%s\n", name, value, Bytes.hexstring(bytes)));
        return sb.toString();
    }
}
