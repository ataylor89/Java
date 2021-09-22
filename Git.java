import java.io.*;

public class Git {
	
	private File dir;

	public Git() {
		dir = new File(System.getProperty("user.home"), "Java");
	}

	private void run(String ... args) {
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.directory(dir);
		pb.redirectErrorStream(true);
		try {
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (process.isAlive()) {
				reader.lines().forEach(System.out::println);
			}
		} catch (IOException e) {
			System.err.println(e);	
		}
	}

	public void status() {
		run("git", "status");		
	}

	public void add(String filename) {
		run("git", "add", filename);
	}

	public void commit(String message) {
		run("git", "commit", "-m", message);
	}

	public void push() {
		run("git", "push", "-u", "origin", "main");
	}

	public static void main(String[] args) {
		System.out.println("Environment variables:\n" + System.getenv());
		Git git = new Git();
		System.out.println("git status");
		git.status();
		System.out.println("git add Git.java");
		git.add("Git.java");
	}
}