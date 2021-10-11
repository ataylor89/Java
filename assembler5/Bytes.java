package assembler5;
public class Bytes {

	public static byte[] littleendian(long val) {
		byte[] bytes = new byte[8];
		long bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	}

	public static byte[] littleendian(int val) {
		byte[] bytes = new byte[4];
		int bitmask = 0xFF;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	}

	public static byte[] littleendian(long val, int size) {
		byte[] bytes = new byte[size];
		long bitmask = 0xFF;
		for (int i = 0; i < size; i++) {
			bytes[i] = (byte) ((val & bitmask) >> 8*i);  
            bitmask <<= 8;
		}
		return bytes;
	}

    public static int size(long val) {
        long mask = 0xFF << 8*7;
        for (int i = 7; i > 0; i++) {
            if ((val & mask) != 0)
                return i+1;
            mask >>= 8;
        }
        return 1;
    }

    public static String hexstring(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
            if (i < bytes.length - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

}
