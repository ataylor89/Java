import java.io.*;

public class ShellDemo {

    private ProcessBuilder pb;
    private Process process;
    private String prefix = "& ";

    public ShellDemo() {}

    private void init() {
        System.out.println("Shell Demo");
        System.out.print(prefix);
        try {
            pb = new ProcessBuilder("/bin/zsh");
            process = pb.start();
            read();
            listen();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void read() {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = process.inputReader()) {
                char[] buf = new char[10000];
                int count = 0;
                while (process.isAlive()) {
                    count = reader.read(buf, 0, 10000);
                    if (count > 0) {
                        System.out.print(new String(buf, 0, count));
                    }
                    if (process.children().filter(p -> p.isAlive()).count() > 0) 
                        Thread.sleep(500);
                    if (process.children().filter(p -> p.isAlive()).count() == 0) 
                        System.out.print(prefix);
                } 
            } catch (IOException | InterruptedException ex) {
                System.err.println(ex);
            }
        });
        thread.start();
    }

    public void listen() {
        Thread thread = new Thread(() -> {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = process.outputWriter();
            ) {
                while (process.isAlive()) {
                    String line = reader.readLine();
                    writer.write(line);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        ShellDemo demo = new ShellDemo();
        demo.init();
    }
}