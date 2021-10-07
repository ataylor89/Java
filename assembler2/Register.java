package assembler2;
public enum Register {
    private byte[] bytes;

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
}
