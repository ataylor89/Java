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
		if (args.length < 3) {
			System.out.println("Usage: java XorEncryption <dataFile> <cipherFile> <outFile>");
			return;
		}
		Path dataPath = Paths.get(args[0]);
		Path cipherPath = Paths.get(args[1]);
		File outFile = new File(args[2]);
		try (PrintWriter out = new PrintWriter(new FileOutputStream(outFile, false), true)) {
			String data = new String(Files.readAllBytes(dataPath));
			String cipher = new String(Files.readAllBytes(cipherPath));
			String datac = crypt(data, cipher);
			out.write(datac);
			System.out.println("Data file:" + args[0]);
			System.out.println("Cipher file:" + args[1]);
			System.out.println("Output file:" + args[2]);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}