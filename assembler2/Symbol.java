package assembler2;
public class Symbol {
    private String name;
    private String value;
    private byte[] bytes;
    private String hexstring;

    public Symbol() {}
        
    public Symbol(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

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

    public void setHexString(String hexstring) {
        this.hexstring = hexstring;
    }

    public String getHexString() {
        return hexstring;
    }

    @Override
    public String toString() {
        return name + ":\t" + value + "\t" + hexstring;
    }
}
