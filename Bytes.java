public class Bytes {
	public static byte[] bigendian(int val) {
		byte b1 = (byte) (val & 0xFF000000);
		byte b2 = (byte) (val & 0x00FF0000);
		byte b3 = (byte) (val & 0x0000FF00);
		byte b4 = (byte) (val & 0x000000FF);
		int count = 0;
		if (b1 != 0)
			count++;
		if (b2 != 0)
			count++;
		if (b3 != 0)
			count++:
		if (b4 != 0)
			count++;
		byte[] bytes = new byte[count];
		int index = 0;
		if (b1 != 0)
			bytes[index++] = b1;
		if (b2 != 0)
			bytes[index++] = b2;
		if (b3 != 0)
			bytes[index++] = b3;
		if (b4 != 0)	
			bytes[index++] = b4;
		return bytes;
	}
}