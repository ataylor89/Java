import java.net.*;
import java.io.*;

public class Telnet {
	
	private String hostname;
	private int port;

	public Telnet(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void send(PrintWriter out, String datagram) {
		out.print(datagram);
		out.flush();
	}

	public void read(BufferedReader in) {
		try {
			String line;
			int contentLength = 0;
			int numEmptyLines = 0;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				if (line.startsWith("Content-Length:")) 
					contentLength = Integer.parseInt(line.split(" ")[1]);
				if (line.isEmpty())
					numEmptyLines++;
				if (contentLength > 0 && line.isEmpty())
					break;
				if (numEmptyLines == 2)
					break;
			}
			if (contentLength > 0) {
				char[] buf = new char[contentLength];
				in.read(buf, 0, contentLength);
				System.out.println(buf);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void connect() {
		try (
			Socket socket = new Socket(hostname, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		) {
			System.out.println("Connected to " + hostname + ": " + String.valueOf(port));
			String datagram = "";
			while (socket.isConnected()) {
				String line = stdin.readLine();
				datagram += line + "\r\n";
				if (line.isEmpty()) {
					send(out, datagram);
					read(in);
					datagram = "";	
				}
			}
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		if (args.length < 2)
			return;
		Telnet telnet = new Telnet(args[0], Integer.parseInt(args[1]));
		telnet.connect();
	}
}