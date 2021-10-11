package assembler3;
public class Instruction {
    private String text;
    private byte[] bytes;

    public Instruction() {}

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        
    }
}   
