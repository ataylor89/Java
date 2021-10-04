package assembler;
import java.util.stream.*;
public class Bytes {

	public static byte[] bigendian(int val) {
		byte[] bytes = new byte[4];
		int bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (bitmask & val);
            bitmask <<= 8;
		}
		return bytes;
	}
        
    public static String hexstring(byte[] bytes) {
        return IntStream.range(0, bytes.length).mapToObj(i -> String.format("%x", bytes[i])).collect(Collectors.joining(" "));
    }
}
