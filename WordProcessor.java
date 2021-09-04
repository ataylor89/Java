import javax.swing.*;
import javax.swing.plaf.metal.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.*;

public class WordProcessor extends JFrame implements MenuListener, ActionListener {

    private JMenuBar bar;
    private JMenu file;
    private JMenuItem save, saveAs, open, fgcolor, bgcolor, exit;
    private JMenu theme;
    private JMenuItem nimbus, system, metal, ocean;
    private JMenu tools; 
    private JMenuItem tabSize, lineCount, characterCount, copyToClipboard;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextArea textArea;    
    private JFileChooser fileChooser;
    private File currentFile;

    public WordProcessor() {
        super("Word Processor");
    }

    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
        file.addMenuListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        fgcolor = new JMenuItem("Set foreground color");
        fgcolor.addActionListener(this);
        bgcolor = new JMenuItem("Set background color");
        bgcolor.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(save);
        file.add(saveAs);
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
        tools = new JMenu("Tools");
        tabSize = new JMenuItem("Set tab size");
        tabSize.addActionListener(this);
        lineCount = new JMenuItem("Check line count");
        lineCount.addActionListener(this);
        characterCount = new JMenuItem("Check character count");
        characterCount.addActionListener(this);	
	copyToClipboard = new JMenuItem("Copy text to clipboard");
	copyToClipboard.addActionListener(this);
        tools.add(tabSize);
        tools.add(lineCount);
        tools.add(characterCount);
	tools.add(copyToClipboard);
        bar.add(file);
        bar.add(theme);
        bar.add(tools);       
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setTabSize(3);
        scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
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
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(currentFile));
            String text = textArea.getText();
            bufferedWriter.write(text);
            bufferedWriter.close();
         } catch (IOException ex) {
            System.err.println(ex);
         }
    }

    private void saveToFileAs() {
        int returnVal = fileChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(currentFile));
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
            currentFile = fileChooser.getSelectedFile();
            try {
                String text = new String(Files.readAllBytes(currentFile.toPath()));
                textArea.setText(text);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public void menuSelected(MenuEvent e) {
        if (e.getSource() == file) {
            save.setEnabled(currentFile != null);    
        }
    }     
    
    public void menuDeselected(MenuEvent e) {}
    
    public void menuCanceled(MenuEvent e) {}
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            saveToFile();
        }
        else if (e.getSource() == saveAs) {
            saveToFileAs();
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
        else if (e.getSource() == tabSize) {
            Integer size = (Integer) JOptionPane.showInputDialog(this, "Select tab size", "Tab size", JOptionPane.PLAIN_MESSAGE, null, new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}, 3);
            textArea.setTabSize(size);
        }
	else if (e.getSource() == lineCount) {
	    JOptionPane.showMessageDialog(this, "There are " + textArea.getLineCount() + " lines in the file"); 				
	}
	else if (e.getSource() == characterCount) {
	    JOptionPane.showMessageDialog(this, "There are " + textArea.getText().length() + " characters in the file"); 
	}
	else if (e.getSource() == copyToClipboard) {
	    StringSelection stringSelection = new StringSelection(textArea.getText());
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();	
            clipboard.setContents(stringSelection, null);
	}
    }

    public static void main(String[] args) {
        WordProcessor.setLookAndFeel();
        WordProcessor wordProcessor = new WordProcessor();
        wordProcessor.createAndShowGui();
        wordProcessor.setVisible(true);
    }
}