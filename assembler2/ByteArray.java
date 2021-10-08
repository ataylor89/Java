package assembler2;
public class ByteArray {
    
    private byte[] bytes;
    private int index;

    public ByteArray() {
        bytes = new byte[1000];
        index = 0;
    }

    public void resize(int amount) {
        byte[] b = new byte[bytes.length + amount];
        for (int i = 0; i < bytes.length; i++)
            b[i] = bytes[i];
        bytes = b;
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
        System.arraycopy(b, 0, bytes, index, b.length);
        index += b.length;
    }

    public byte[] getBytes() {
        byte[] arr = new byte[index];
        System.arraycopy(bytes, 0, arr, 0, index);
        return arr;
    }

    @Override
    public String toString() {
        return new String(getBytes());
    }
}
