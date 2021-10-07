package assembler2;
public class Symbol {
    private String name;
    private byte[] bytes;

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

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
