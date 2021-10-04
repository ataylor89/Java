package assembler;
import java.util.stream.*;
public class Bytes {

	public static byte[] littleendian(int val) {
		byte[] bytes = new byte[4];
		int bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	}
        
    public static String hexstring(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%x", bytes[i]));
            if (i < bytes.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }
}
