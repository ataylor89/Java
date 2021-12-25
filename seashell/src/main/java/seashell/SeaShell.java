package seashell;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;

public class SeaShell extends JFrame implements KeyListener, ActionListener {

    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem newTab, closeTab, saveFile, openFile, exit;
    private JMenu colors;
    private JMenuItem setForegroundColor, setBackgroundColor, blackwhite, graywhite, grayblue, tealwhite, purplewhite, whiteblack, whitegray, bluegray, whiteteal, whitepurple;
    private JMenu settings;
    private JMenuItem setTitle, setStarter;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JFileChooser fileChooser;
    private Color foregroundColor, backgroundColor;
    private final Color purple = new Color(153, 0, 153, 255);
    private final Color teal = new Color(0, 153, 153, 255);
    private final Color gray = new Color(204, 204, 204, 255);
    private final Color blue = new Color(0, 0, 204, 255);
    private Logger logger;

    private class SeaShellTab extends JTextArea {
        private Interpreter interpreter;
        private String startingText = "& ";
        private int previousCaretPosition;
        
        public SeaShellTab() {
            interpreter = new Interpreter(this);
        }
        
        public void setInterpreter(Interpreter interpreter) {
            this.interpreter = interpreter;
        }
        
        public Interpreter getInterpreter() {
            return interpreter;
        }
        
        public void setPreviousCaretPosition(int previousCaretPosition) {
            this.previousCaretPosition = previousCaretPosition;
        }
        
        public int getPreviousCaretPosition() {
            return previousCaretPosition;
        }
        
        @Override
        public void append(String text) {
            super.append(text);
            setCaretPosition(getText().length());
            setPreviousCaretPosition(getText().length());
        }
        
        public void setStartingText(String startingText) {
            this.startingText = startingText;
        }
        
        public String getStartingText() {
            return startingText;
        }
        
        public void startNewLine() {
            if (startingText != null & startingText.length() > 0)
                append(startingText);
        }
    }
    
    private class Interpreter {
        private SeaShellTab display;
        private Process bash;
        
        public Interpreter(SeaShellTab display) {
            this.display = display;
            init();
        }
        
        private void init() {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash");
            try {
                bash = pb.start();
                read();
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }
        
        private void read() {
            Thread thread = new Thread(() -> {
                try {
                    BufferedReader reader = bash.inputReader();
                    char[] buf = new char[10000];
                    while (bash.isAlive()) {
                        while (reader.ready()) {
                            int count = reader.read(buf, 0, 10000);
                            display.append(new String(buf, 0, count));
                        }
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException ex) {
                    logger.warning(ex.toString());
                }                  
            });
            thread.start();
        }
        
        public void interpret(String code, SeaShellTab display) {  
            BufferedWriter writer = bash.outputWriter();
            try {
                writer.write(code, 0, code.length());
                writer.flush();
                Thread.sleep(1000);
                display.startNewLine();
            } catch (IOException | InterruptedException ex) {
                logger.warning(ex.toString());
            }
        }
    }

    public SeaShell() {
        super("SeaShell");
        initLogger();
    }

    private void initLogger() {
        logger = Logger.getLogger("SeaShell");
        logger.setLevel(Level.ALL);
        logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        try {
            logger.addHandler(new FileHandler("SeaShell.log", true));
            logger.log(Level.INFO, "Set up file logging");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void createAndShowGui() {
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBar = new JMenuBar();
        file = new JMenu("File");
        newTab = new JMenuItem("New tab");
        newTab.addActionListener(this);
        file.add(newTab);
        closeTab = new JMenuItem("Close tab");
        closeTab.addActionListener(this);
        file.add(closeTab);
        saveFile = new JMenuItem("Save file");
        saveFile.addActionListener(this);
        file.add(saveFile);
        openFile = new JMenuItem("Open file");
        openFile.addActionListener(this);
        file.add(openFile);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        file.add(exit);
        colors = new JMenu("Colors");
        setForegroundColor = new JMenuItem("Set foreground color");
        setForegroundColor.addActionListener(this);
        setBackgroundColor = new JMenuItem("Set background color");
        setBackgroundColor.addActionListener(this);
        blackwhite = new JMenuItem("Black white");
        blackwhite.addActionListener(this);
        graywhite = new JMenuItem("Gray white");
        graywhite.addActionListener(this);
        bluegray = new JMenuItem("Blue gray");
        bluegray.addActionListener(this);
        whiteteal = new JMenuItem("White teal");
        whiteteal.addActionListener(this);
        whitepurple = new JMenuItem("White purple");
        whitepurple.addActionListener(this);
        whiteblack = new JMenuItem("White black");
        whiteblack.addActionListener(this);
        whitegray = new JMenuItem("White gray");
        whitegray.addActionListener(this);
        grayblue = new JMenuItem("Gray blue");
        grayblue.addActionListener(this);
        tealwhite = new JMenuItem("Teal white");
        tealwhite.addActionListener(this);
        purplewhite = new JMenuItem("Purple white");
        purplewhite.addActionListener(this);
        colors.add(setForegroundColor);
        colors.add(setBackgroundColor);
        colors.add(blackwhite);
        colors.add(graywhite);
        colors.add(bluegray);
        colors.add(whiteteal);
        colors.add(whitepurple);
        colors.add(whiteblack);
        colors.add(whitegray);
        colors.add(grayblue);
        colors.add(tealwhite);
        colors.add(purplewhite);
        settings = new JMenu("Settings");
        setTitle = new JMenuItem("Set tab title");
        setTitle.addActionListener(this);
        setStarter = new JMenuItem("Set starting text");
        setStarter.addActionListener(this);
        settings.add(setTitle);
        settings.add(setStarter);
        menuBar.add(file);
        menuBar.add(colors);
        menuBar.add(settings);
        setJMenuBar(menuBar);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tabbedPane);
        add(panel);
        fileChooser = new JFileChooser();
        setVisible(true);
    }

    private void saveToFile() {
        int opt = fileChooser.showSaveDialog(this);

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
                JTextArea ta = (JTextArea) sp.getViewport().getView();
                String text = ta.getText();
                writer.print(text);
            } catch (IOException e) {
                logger.log(Level.WARNING, e.toString());
            }
        }
    }

    private void openFile() {
        int opt = fileChooser.showOpenDialog(this);

        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String text = reader.lines().reduce((partial, s) -> partial + s).toString();
                JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
                JTextArea ta = (JTextArea) sp.getViewport().getView();
                ta.setText(text);
            } catch (IOException e) {
                logger.log(Level.WARNING, e.toString());
            }
        }
    }

    private void setForegroundColor(Color color) {
        foregroundColor = color;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setForeground(foregroundColor);
    }

    private void setBackgroundColor(Color color) {
        backgroundColor = color;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setBackground(backgroundColor);
    }

    private void setColors(Color fgColor, Color bgColor) {
        foregroundColor = fgColor;
        backgroundColor = bgColor;
        JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
        JTextArea ta = (JTextArea) sp.getViewport().getView();
        ta.setForeground(foregroundColor);
        ta.setBackground(backgroundColor);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
            SeaShellTab seaShellTab = (SeaShellTab) scrollPane.getViewport().getView();
            Interpreter interpreter = seaShellTab.getInterpreter();
            String text = seaShellTab.getText();
            int p = seaShellTab.getPreviousCaretPosition(), q = seaShellTab.getCaretPosition();
            if (p < q) {
                String code = text.substring(p, q);
                logger.info("Interpreting code: " + code);
                interpreter.interpret(code, seaShellTab);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newTab) {
            String title = JOptionPane.showInputDialog(this, "Title:", "New tab", JOptionPane.QUESTION_MESSAGE);
            SeaShellTab seaShellTab = new SeaShellTab();
            seaShellTab.startNewLine();
            seaShellTab.addKeyListener(this);
            JScrollPane scrollPane = new JScrollPane(seaShellTab);
            tabbedPane.addTab(title, scrollPane);
        } else if (e.getSource() == closeTab) {
            int index = tabbedPane.getSelectedIndex();
            tabbedPane.removeTabAt(index);
        } else if (e.getSource() == saveFile) {
            saveToFile();
        } else if (e.getSource() == openFile) {
            openFile();
        } else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        } else if (e.getSource() == setForegroundColor) {
            Color color = JColorChooser.showDialog(this, "Select a foreground color", foregroundColor);
            if (color != null) {
                foregroundColor = color;
            }
            setForegroundColor(foregroundColor);
        } else if (e.getSource() == setBackgroundColor) {
            Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
            if (color != null) {
                backgroundColor = color;
            }
            setBackgroundColor(backgroundColor);
        } else if (e.getSource() == blackwhite) {
            setColors(Color.BLACK, Color.WHITE);
        } else if (e.getSource() == graywhite) {
            setColors(Color.LIGHT_GRAY, Color.WHITE);
        } else if (e.getSource() == bluegray) {
            setColors(blue, gray);
        } else if (e.getSource() == whiteteal) {
            setColors(Color.WHITE, teal);
        } else if (e.getSource() == whitepurple) {
            setColors(Color.WHITE, purple);
        } else if (e.getSource() == whiteblack) {
            setColors(Color.WHITE, Color.BLACK);
        } else if (e.getSource() == whitegray) {
            setColors(Color.WHITE, Color.LIGHT_GRAY);
        } else if (e.getSource() == grayblue) {
            setColors(gray, blue);
        } else if (e.getSource() == tealwhite) {
            setColors(teal, Color.WHITE);
        } else if (e.getSource() == purplewhite) {
            setColors(purple, Color.WHITE);
        } else if (e.getSource() == setTitle) {
            int index = tabbedPane.getSelectedIndex();
            if (index >= 0) {
                String title = JOptionPane.showInputDialog(this, "Set tab title: ", "Set tab title", JOptionPane.QUESTION_MESSAGE);
                if (title != null && title.trim().length() > 0)
                    tabbedPane.setTitleAt(index, title);
            }
        } else if (e.getSource() == setStarter) {
            JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
            SeaShellTab seaShellTab = (SeaShellTab) scrollPane.getViewport().getView();
            String s = JOptionPane.showInputDialog(this, "Set starting text: ", "Set starting text", JOptionPane.QUESTION_MESSAGE);
            if (s != null && s.length() < 10)
                seaShellTab.setStartingText(s);
        } 
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void setupKeyStrokes() {
        InputMap im = (InputMap) UIManager.get("TextArea.focusInputMap");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
    }

    public static void main(String[] args) {
        SeaShell.setLookAndFeel();
        SeaShell.setupKeyStrokes();
        SeaShell seaShell = new SeaShell();
        seaShell.createAndShowGui();
    }
}
