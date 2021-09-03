import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ProgressMonitor2 extends JFrame implements ActionListener {

    private JPanel panel;
    private JButton start;
    private JButton stop;
    private JButton restart;
    private JProgressBar progressBar;
    private int progress = 0;    
    private boolean running = false;
    private Thread progressThread;

    public ProgressMonitor2() {
        super("Progress Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
 
        panel = new JPanel();
        panel.setSize(800, 600);

        start = new JButton("Start");
        start.addActionListener(this);
        stop = new JButton("Stop");
        stop.addActionListener(this);
        stop.setEnabled(false);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        restart.setEnabled(false);

        progressBar = new JProgressBar(0, 2000);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
           
        panel.add(start);  
        panel.add(stop);
        panel.add(restart);     
        panel.add(progressBar);

        add(panel);
    }
    
    public void setupProgressBar() {
        progressThread = new Thread(() -> {
            while (true) {
                if (running && progress < 2000) {
                    progressBar.setValue(progress);
                    progress += 95;
                }
                try { 
                    Thread.sleep(1000); 
                } catch (InterruptedException e) { 
                    System.err.println(e); 
                }
                
            }
        });
        progressThread.start();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) { 
            running = true;
            start.setEnabled(false);
            stop.setEnabled(true);
            restart.setEnabled(false);
        }
        else if (e.getSource() == stop) {
            running = false;
            start.setEnabled(true);
            stop.setEnabled(false);
            restart.setEnabled(true);
        }
        else if (e.getSource() == restart) {
            running = true;
            progress = 0;
            start.setEnabled(false);
            stop.setEnabled(true);
            restart.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        ProgressMonitor2.setLookAndFeel();
        ProgressMonitor2 frame = new ProgressMonitor2();
        frame.setVisible(true);
        frame.setupProgressBar();
    }

}
