public class XorEncryption {
	public static String crypt(String data, String cipher) {
		char[] result = new char[data.length()];
		for (int i = 0; i < result.length; i++) {
			int j = i % cipher.length();
			char a = data.charAt(i);
			char b = cipher.charAt(j);
			char c = (char) (a ^ b);
			result[i] = c;
		}
		return new String(result);
	}
}