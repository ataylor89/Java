import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.metal.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class MagicSquares extends JFrame implements ActionListener {
    
    private JMenuBar bar;
    private JMenu file;
    private JMenuItem save, open, colors, exit;
    private JMenu theme;
    private JMenuItem nimbus, system, metal, ocean;
    private JDialog colorsDialog;
    private JPanel colorsDialogPanel;
    private JButton setForegroundColor, setBackgroundColor, saveColors;
    private Color foregroundColor, backgroundColor;
    private JPanel panel;
    private JTextField[][] grid;
    private JFileChooser fileChooser;

    public MagicSquares() {
        super("Magic Squares");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
        save = new JMenuItem("Save");
        save.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        colors = new JMenuItem("Colors");
        colors.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(save);
        file.add(open);
        file.add(colors);
        file.add(exit);
        colorsDialog = new JDialog(this, "Colors");
        colorsDialog.setSize(500, 500);
        colorsDialogPanel = new JPanel();
        colorsDialogPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        setForegroundColor = new JButton("Foreground color");
        setForegroundColor.setSize(50, 50);
        setForegroundColor.addActionListener(this);
        colorsDialogPanel.add(setForegroundColor);
        setBackgroundColor = new JButton("Background color");
        setBackgroundColor.setSize(50, 50);
        setBackgroundColor.addActionListener(this);
        colorsDialogPanel.add(setBackgroundColor);
        saveColors = new JButton("Save and close");
        saveColors.setSize(50, 50);
        saveColors.addActionListener(this);
        colorsDialogPanel.add(saveColors);
        colorsDialog.add(colorsDialogPanel);
        theme = new JMenu("Theme");
        nimbus = new JMenuItem("Nimbus");
        nimbus.addActionListener(this);
        system = new JMenuItem("System");
        system.addActionListener(this);
        metal = new JMenuItem("Metal");
        metal.addActionListener(this);
        ocean = new JMenuItem("Ocean");
        ocean.addActionListener(this);
        theme.add(nimbus);
        theme.add(system);
        theme.add(metal);
        theme.add(ocean);
        bar.add(file);
        bar.add(theme);
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9));
        grid = new JTextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Sans Serif", Font.PLAIN, 30));
                grid[i][j].setDocument(new PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                        if (getLength() > 0)
                            return;
                        if (str.length() != 1)
                            return;
                        char c = str.charAt(0);
                        if (c < '1' || c > '9')
                            return;
                        super.insertString(offs, str, a);
                    }
                });
                panel.add(grid[i][j]);
            }
        }
        add(panel);
        fileChooser = new JFileChooser();
    }

    private void setColors() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setForeground(foregroundColor);
                grid[i][j].setBackground(backgroundColor);
            }
        }
    }

    private void saveToFile() {
        int returnVal = fileChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String str = "";
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        String s = grid[i][j].getText();
                        if (s.equals(""))
                            s = "0";
                        str += s;
                    }
                    str += "\n";
                }

                bufferedWriter.write(str);
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
                String[] lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    for (int j = 0; j < lines[i].length(); j++) {
                        char c = lines[i].charAt(j);
                        if (c >= '1' || c <= '9')
                            grid[i][j].setText(String.valueOf(c));
                    }
                }
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
        else if (e.getSource() == colors) {
            colorsDialog.setVisible(true);           
        }
        else if (e.getSource() == setForegroundColor) {
            foregroundColor = JColorChooser.showDialog(null, "Select a foreground color", null);
        }
        else if (e.getSource() == setBackgroundColor) {
            backgroundColor = JColorChooser.showDialog(null, "Select a background color", null);
        }
        else if (e.getSource() == saveColors) {
            setColors();
            colorsDialog.setVisible(false);
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
        else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
    }

    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel); 
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        MagicSquares.setLookAndFeel();
        MagicSquares frame = new MagicSquares();
        frame.setVisible(true);
    }
}
