package assembler;
import java.util.stream.*;
public class ObjectFile {
	
	private byte[] bytes;
	private int index = 0;

	public ObjectFile() {
		bytes = new byte[1000];
	}

	public ObjectFile(int size) {
		bytes = new byte[size];
	}

	public void resize(int amount) {
		byte[] b = new byte[bytes.length + amount];
		for (int i = 0; i < b.length; i++)
			b[i] = bytes[i];
		bytes = b;
	}

	public void addBytes(byte[] b) {
		if (index + b.length > bytes.length) 
			resize(1000);
		for (int i = 0; i < b.length; i++) {
			bytes[index] = b[i];
            index++;
		}
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int size() {
		return index;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("Object file\n");
		sb.append(String.format("Size: %d\n", index));
		sb.append("Hex:\n");
		sb.append(IntStream.range(0, index).mapToObj(i -> String.format("%x", bytes[i])).collect(Collectors.joining(" ")));
		return sb.toString();
	}
}
