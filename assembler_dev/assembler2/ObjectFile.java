package assembler2;
import java.io.File;
public class ObjectFile {
    private File file;
    private ByteArray byteArray;
    
    public ObjectFile(File file) {
        this.file = file;
        byteArray = new ByteArray();
    }

    public ObjectFile() {
        byteArray = new ByteArray();
    }

    public void setFile(File file) {
        this.file = file;
    }       

    public File getFile() {
        return file;
    }

    public void setByteArray(ByteArray byteArray) {
        this.byteArray = byteArray;
    }

    public ByteArray getByteArray() {
        return byteArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Object file\n");
        byte[] bytes = byteArray.getBytes();
        sb.append(Bytes.hexstring(bytes));
        return sb.toString();
    }
}
