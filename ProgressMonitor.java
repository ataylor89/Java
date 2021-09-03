import javax.swing.*;

public class ProgressMonitor extends JFrame {

    private JProgressBar progressBar;
    private int progress = 0;    

    public ProgressMonitor() {
        super("Progress Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        progressBar = new JProgressBar(0, 2000);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar);
    }

    private void iterate() {
        while (progress < 2000) {
            progressBar.setValue(progress);
            try { 
                Thread.sleep(1000); 
            } catch (InterruptedException e) { 
                System.err.println(e); 
            }
            progress += 95;
        }
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        ProgressMonitor.setLookAndFeel();
        ProgressMonitor frame = new ProgressMonitor();
        frame.setVisible(true);
        frame.iterate();
    }

}
