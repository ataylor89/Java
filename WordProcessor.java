import javax.swing.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class WordProcessor extends JFrame implements ActionListener {

    private JMenuBar bar;
    private JMenu file;
    private JMenuItem save, open, fgcolor, bgcolor, exit;
    private JMenu theme;
    private JMenuItem nimbus, system, metal, ocean;
    private JPanel panel;
    private JTextArea textArea;    
    private JFileChooser fileChooser;

    public WordProcessor() {
        super("Word Processor");
    }

    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
        save = new JMenuItem("Save to file");
        save.addActionListener(this);
        open = new JMenuItem("Open file");
        open.addActionListener(this);
        fgcolor = new JMenuItem("Set foreground color");
        fgcolor.addActionListener(this);
        bgcolor = new JMenuItem("Set background color");
        bgcolor.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(save);
        file.add(open);
        file.add(fgcolor);
        file.add(bgcolor);
        file.add(exit);
        theme = new JMenu("Theme");
        nimbus = new JMenuItem("Nimbus theme");
        nimbus.addActionListener(this);
        system = new JMenuItem("System theme");
        system.addActionListener(this);
        metal = new JMenuItem("Metal theme");
        metal.addActionListener(this);
        ocean = new JMenuItem("Ocean theme");
        ocean.addActionListener(this);
        theme.add(nimbus);
        theme.add(system);
        theme.add(metal);
        theme.add(ocean);
        bar.add(file);
        bar.add(theme);
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        panel.add(textArea);
        add(panel);
        fileChooser = new JFileChooser();
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void saveToFile() {
        int returnVal = fileChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String text = textArea.getText();
                bufferedWriter.write(text);
                bufferedWriter.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {       
            File file = fileChooser.getSelectedFile();
            try {
                String text = new String(Files.readAllBytes(file.toPath()));
                textArea.setText(text);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            saveToFile();
        }
        else if (e.getSource() == open) {
            openFile();
        }
        else if (e.getSource() == fgcolor) {
            textArea.setForeground(JColorChooser.showDialog(null, "Select a foreground color", null));
        }
        else if (e.getSource() == bgcolor) {
            textArea.setBackground(JColorChooser.showDialog(null, "Select a background color", null));
        }
        else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
        else if (e.getSource() == nimbus) {
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        else if (e.getSource() == system) {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        else if (e.getSource() == metal) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            setLookAndFeel(new MetalLookAndFeel());
        }
        else if (e.getSource() == ocean) {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            setLookAndFeel(new MetalLookAndFeel());
        }
    }

    public static void main(String[] args) {
        WordProcessor.setLookAndFeel();
        WordProcessor wordProcessor = new WordProcessor();
        wordProcessor.createAndShowGui();
        wordProcessor.setVisible(true);
    }
}
