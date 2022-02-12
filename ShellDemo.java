import java.io.*;

public class ShellDemo {

    private Process process;
    private String prefix = "& ";

    public ShellDemo() {}

    private void read() {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = process.inputReader()) {
                char[] buf = new char[10000];
                while (reader.ready() || process.isAlive()) {
                    int count = reader.read(buf, 0, 10000);
                    if (count > 0)
                        System.out.print(new String(buf, 0, count));
                } 
                System.out.print(prefix); 
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        thread.start();
    }

    public void listen() {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Shell Demo");
                System.out.print(prefix);
                while (true) {
                    String line = reader.readLine();
                    if (process != null && process.isAlive()) {
                        BufferedWriter writer = process.outputWriter();
                        writer.write(line);
                        writer.newLine();
                        writer.flush();
                    }
                    else {
                        line = "/bin/zsh -c " + line; 
                        String[] args = line.split("\\s+", 3);
                        ProcessBuilder pb = new ProcessBuilder(args);
                        process = pb.start();
                        read();
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        ShellDemo demo = new ShellDemo();
        demo.listen();
    }
}