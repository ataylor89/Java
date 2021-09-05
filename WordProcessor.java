import javax.swing.*;
import javax.swing.plaf.metal.*;
import javax.swing.event.*;
import javax.swing.text.*;
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
    private JMenuItem tabSize, lineCount, characterCount, gotoLine, copyToClipboard;
    private JMenu compile;
    private JMenuItem compileJavaProgram, compileCProgram, compileCPPProgram, compileNASMProgram;
    private JMenu run;
    private JMenuItem runJavaProgram, runPythonProgram, runMachineCodeProgram;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private Color foregroundColor, backgroundColor;
    private int tabWidth;

    private class ProcessDialog extends Thread {

        private JFrame frame;
        private String title;
        private String cmd;
        private JDialog dialog;
        private JTextArea display;

        public ProcessDialog(JFrame frame, String title, String cmd) {
            this.frame = frame;
            this.title = title;
            this.cmd = cmd;
        }

        private void createAndShowDialog() {
            dialog = new JDialog(frame, title);
            dialog.setSize(500, 500);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            display = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(display);
            panel.add(scrollPane);
            dialog.add(panel);
            dialog.setVisible(true);
            display.append(cmd + "\n");
        }

        private void runCommand() {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while (dialog.isShowing() && process.isAlive() && (line = br.readLine()) != null) {
                    display.append(line);
                    Thread.sleep(1000);
                }
                process.destroy();
            } catch (IOException | InterruptedException e) {
                display.append(e + "\n");
            }
        }

        @Override
        public void run() {
            createAndShowDialog();
            runCommand();
        }
    }

    public WordProcessor() {
        super("Word Processor");
        tabWidth = 1;
    }

    public WordProcessor(String path) {
        this();
        System.out.println(path);
        currentFile = new File(path);
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
        gotoLine = new JMenuItem("Goto line number");
        gotoLine.addActionListener(this);
        copyToClipboard = new JMenuItem("Copy text to clipboard");
        copyToClipboard.addActionListener(this);
        tools.add(tabSize);
        tools.add(lineCount);
        tools.add(characterCount);
        tools.add(gotoLine);
        tools.add(copyToClipboard);
        compile = new JMenu("Compile");
        compileJavaProgram = new JMenuItem("Compile Java program");
        compileJavaProgram.addActionListener(this);
        compileCProgram = new JMenuItem("Compile C program");
        compileCProgram.addActionListener(this);
        compileCPPProgram = new JMenuItem("Compile C++ program");
        compileCPPProgram.addActionListener(this);
        compileNASMProgram = new JMenuItem("Compile NASM program");
        compileNASMProgram.addActionListener(this);
        compile.add(compileJavaProgram);
        compile.add(compileCProgram);
        compile.add(compileCPPProgram);
        compile.add(compileNASMProgram);
        run = new JMenu("Run");
        runJavaProgram = new JMenuItem("Run Java program");
        runJavaProgram.addActionListener(this);
        runPythonProgram = new JMenuItem("Run Python program");
        runPythonProgram.addActionListener(this);
        runMachineCodeProgram = new JMenuItem("Run machine code program");
        runMachineCodeProgram.addActionListener(this);
        run.add(runJavaProgram);
        run.add(runPythonProgram);
        run.add(runMachineCodeProgram);
        bar.add(file);
        bar.add(theme);
        bar.add(tools);
        bar.add(compile);
        bar.add(run);
        setJMenuBar(bar);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setTabSize(3);
        scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        add(panel);
        fileChooser = new JFileChooser();
        if (currentFile != null) {
            openCurrentFile();
        }
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

    private void openCurrentFile() {
        try {
            String text = new String(Files.readAllBytes(currentFile.toPath()));
            textArea.setText(text);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            openCurrentFile();
        }
    }

    private String getFilenameWithoutExtension() {
        String filename = currentFile.getName();
        int lio = filename.lastIndexOf(".");
        if (lio > 0) {
            filename = filename.substring(0, lio);
        }
        return filename;
    }

    private void compileJavaProgram() {
        String cmd = "javac " + currentFile.getPath();
        ProcessDialog process = new ProcessDialog(this, "Compiling Java program...", cmd);
        process.start();
    }

    private void compileCProgram() {
    }

    private void compileCPPProgram() {
    }

    private void compileNASMProgram() {
    }

    private void runJavaProgram() {
        String classname = getFilenameWithoutExtension();

        String args = JOptionPane.showInputDialog("Java program arguments:", "");
        if (args != null) {
            args = args.trim();
        }

        String cmd = "java -cp " + currentFile.getParent() + " " + classname;
        if (args != null && args.length() > 0) {
            cmd += " " + args;
        }

        ProcessDialog process = new ProcessDialog(this, "Running Java program...", cmd);
        process.start();
    }

    private void runPythonProgram() {
        String cmd = "python " + currentFile.getPath();
        ProcessDialog process = new ProcessDialog(this, "Running Python program...", cmd);
        process.start();
    }

    private void runMachineCodeProgram() {
    }

    @Override
    public void menuSelected(MenuEvent e) {
        if (e.getSource() == file) {
            save.setEnabled(currentFile != null);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {
    }

    @Override
    public void menuCanceled(MenuEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            saveToFile();
        } else if (e.getSource() == saveAs) {
            saveToFileAs();
        } else if (e.getSource() == open) {
            openFile();
        } else if (e.getSource() == fgcolor) {
            foregroundColor = JColorChooser.showDialog(null, "Select a foreground color", null);
            textArea.setForeground(foregroundColor);
        } else if (e.getSource() == bgcolor) {
            backgroundColor = JColorChooser.showDialog(null, "Select a background color", null);
            textArea.setBackground(backgroundColor);
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == nimbus) {
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } else if (e.getSource() == system) {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else if (e.getSource() == metal) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            setLookAndFeel(new MetalLookAndFeel());
        } else if (e.getSource() == ocean) {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            setLookAndFeel(new MetalLookAndFeel());
        } else if (e.getSource() == tabSize) {
            Integer size = (Integer) JOptionPane.showInputDialog(this, "Select tab size", "Tab size", JOptionPane.PLAIN_MESSAGE, null, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, tabSize);
            tabWidth = size.intValue();
            textArea.setTabSize(tabWidth);
        } else if (e.getSource() == lineCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getLineCount() + " lines in the file");
        } else if (e.getSource() == characterCount) {
            JOptionPane.showMessageDialog(this, "There are " + textArea.getText().length() + " characters in the file");
        } else if (e.getSource() == gotoLine) {
            int maxLineNumber = textArea.getLineCount();
            int lineNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter a line number:", "1"));
            if (lineNumber >= maxLineNumber) {
                lineNumber = maxLineNumber - 1;
            }
            if (lineNumber < 0) {
                lineNumber = 0;
            }
            try {
                textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber));
            } catch (BadLocationException le) {
                System.err.println(le);
            }
        } else if (e.getSource() == copyToClipboard) {
            StringSelection stringSelection = new StringSelection(textArea.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } else if (e.getSource() == compileJavaProgram) {
            compileJavaProgram();
        } else if (e.getSource() == compileCProgram) {
            compileCProgram();
        } else if (e.getSource() == compileCPPProgram) {
            compileCPPProgram();
        } else if (e.getSource() == compileNASMProgram) {
            compileNASMProgram();
        } else if (e.getSource() == runJavaProgram) {
            runJavaProgram();
        } else if (e.getSource() == runPythonProgram) {
            runPythonProgram();
        } else if (e.getSource() == runMachineCodeProgram) {
            runMachineCodeProgram();
        }
    }

    public static void main(String[] args) {
        WordProcessor.setLookAndFeel();
        WordProcessor wordProcessor;
        if (args.length == 1) {
            wordProcessor = new WordProcessor(args[0]);
        } else {
            wordProcessor = new WordProcessor();
        }
        wordProcessor.createAndShowGui();
        wordProcessor.setVisible(true);
    }
}
