package assembler2;
import java.util.Map;
import java.util.HashMap;
public enum Register {
    
    private byte[] bytes;
    
    private static Map<String, Register> registers;
    
    static {
        registers.put("rax", Register.RAX);
        registers.put("rdi", Register.RDI);
        registers.put("rsi", Register.RSI);
        registers.put("rdx", Register.RDX);
    }
    
    RAX (new byte[] {(byte) 0x48, (byte) 0xb8}), 
    RDI (new byte[] {(byte) 0x48, (byte) 0xbf}), 
    RSI (new byte[] {(byte) 0x48, (byte) 0xbe}), 
    RDX (new byte[] {(byte) 0x48, (byte) 0xba});

    Register(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public static Map<String, Register> map() {
        return registers;
    }
}
