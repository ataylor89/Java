import java.io.*;
import java.nio.file.*;
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

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java XorEncryption <dataFile> <cipherFile>");
			return;
		}
		Path dataPath = Paths.get(args[0]);
		Path cipherPath = Paths.get(args[1]);
		File outFile = new File(args[0] + ".enc");
		try (PrintWriter out = new PrintWriter(new FileWriter(outFile, true))) {
			String data = new String(Files.readAllBytes(dataPath));
			String cipher = new String(Files.readAllBytes(cipherPath));
			String encryptedData = crypt(data, cipher);
			String decryptedData = crypt(encryptedData, cipher);
			System.out.println("Data:\n" + data);
			System.out.println("Encrypted data:\n" + encryptedData);
			System.out.println("Decrypted data:\n" + decryptedData);
			out.print(encryptedData);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}