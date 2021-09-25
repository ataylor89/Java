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
		String outFilePath = args[0];
		if (outFilePath.endsWith(".enc"))
			outFilePath = outFilePath.substring(0, outFilePath.length() - 4) + ".dec";
		else if (outFilePath.endsWith(".dec"))
			outFilePath = outFilePath.substring(0, outFilePath.length() - 4) + ".enc";
		else
			outFilePath += ".enc";
		File outFile = new File(outFilePath);
		try (PrintWriter out = new PrintWriter(new FileOutputStream(outFile, false), true)) {
			String data = new String(Files.readAllBytes(dataPath));
			String cipher = new String(Files.readAllBytes(cipherPath));
			String datac = crypt(data, cipher);
			out.write(datac);
			System.out.println("Data file:" + dataPath);
			System.out.println("Cipher file:" + cipherPath);
			System.out.println("Output file:" + outFilePath);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}