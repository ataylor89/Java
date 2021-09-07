import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.net.*;

public class MagicSquares extends JFrame implements MenuListener, ActionListener, KeyListener {
    
    private JMenuBar bar;
    private JMenu file;
    private JMenuItem newFile, saveFile, saveFileAs, openFile, download, exit;
    private JMenu colors;
    private JMenuItem setForegroundColor, setBackgroundColor, setHighlightColor, whiteblack, whitegray, grayblue, tealwhite, purplewhite;
    private JMenu theme;
    private JMenuItem nimbus, system, metal, ocean;
    private JMenu validate;
    private JMenuItem validateSquares;
    private Color foregroundColor = new Color(0, 0, 204, 255), backgroundColor = Color.WHITE, highlightColor = Color.RED;
    private JPanel panel;
    private JTextField[][] grid;
    private JFileChooser fileChooser;
    private File currentFile;
    private boolean highlightingOn = false;

    public static class CharacterEncoding {
        public static Map<String, String> dataToDisplayMap, editableFieldMap;

	static {
            dataToDisplayMap = new HashMap<>();
            editableFieldMap = new HashMap<>();
            dataToDisplayMap.put("0", "");
            editableFieldMap.put("", "0");
            for (int i = 0; i < 9; i++) {
                editableFieldMap.put(String.valueOf(i+1), String.valueOf((char) ('a' + i)));
                dataToDisplayMap.put(String.valueOf(i+1), String.valueOf(i+1));
                dataToDisplayMap.put(String.valueOf((char) ('a' + i)), String.valueOf(i+1));
            } 
        }

	public static String dataToDisplay(String data) {
            return dataToDisplayMap.get(data);
        }

        public static String displayToData(JTextField field) {
             return field.isEditable() ? editableFieldMap.get(field.getText()) : field.getText();
        }
    }

    public MagicSquares() {
        super("Magic Squares");
    }

    public void createAndShowGui() {
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar = new JMenuBar();
        file = new JMenu("File");
	file.addMenuListener(this);
	newFile = new JMenuItem("New");
	newFile.addActionListener(this);
        saveFile = new JMenuItem("Save");
        saveFile.addActionListener(this);
	saveFileAs = new JMenuItem("Save as");
	saveFileAs.addActionListener(this);
        openFile = new JMenuItem("Open");
        openFile.addActionListener(this);
	download = new JMenuItem("Download");
	download.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
	file.add(newFile);
        file.add(saveFile);
	file.add(saveFileAs);
        file.add(openFile);
	file.add(download);
        file.add(exit);
	colors = new JMenu("Colors");
        setForegroundColor = new JMenuItem("Set foreground color");
        setForegroundColor.addActionListener(this);
        setBackgroundColor = new JMenuItem("Set background color");
        setBackgroundColor.addActionListener(this);
 	setHighlightColor = new JMenuItem("Set highlight color");
       	setHighlightColor.addActionListener(this);
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
	colors.add(setHighlightColor);
	colors.add(whiteblack);
	colors.add(whitegray);
	colors.add(grayblue);
	colors.add(tealwhite);
	colors.add(purplewhite);
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
	validate = new JMenu("Validate");
	validateSquares = new JMenuItem("Validate 9x9 grid");
	validateSquares.addActionListener(this);
	validate.add(validateSquares);
        bar.add(file);
	bar.add(colors);
        bar.add(theme);
	bar.add(validate);
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
		grid[i][j].addKeyListener(this);
                panel.add(grid[i][j]);
            }
        }
        add(panel);
        fileChooser = new JFileChooser();
    }

    private void refreshColors() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!grid[i][j].isEditable())
                    grid[i][j].setForeground(foregroundColor);
                grid[i][j].setBackground(backgroundColor);
            }
        }
    }

    private void newFile() {
	currentFile = null;
	for (int i = 0; i < 9; i++) {
	    for (int j = 0; j < 9; j++) {
		grid[i][j].setText("");
                 grid[i][j].setEditable(true);
		grid[i][j].setForeground(Color.BLACK);
		grid[i][j].setBackground(Color.WHITE);
	    }
	}
    }

    private void saveToFile(File file) {
	try {
	    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
	    String str = "";
	    for (int i = 0; i < 9; i++) {
	        for (int j = 0; j < 9; j++) {
		    str += CharacterEncoding.displayToData(grid[i][j]);                        
                }
                str += "\n";
            }
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void saveToFileAs() {
	int returnVal = fileChooser.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    currentFile = fileChooser.getSelectedFile();
	    saveToFile(currentFile);
	}
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {       
            currentFile = fileChooser.getSelectedFile();
            try {
                String text = new String(Files.readAllBytes(currentFile.toPath()));
		load(text);
	    } catch (IOException ex) {
	    	System.err.println(ex);
	    }
	}
     }

     private void download(String address) {
	try {
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) connection.getContent()));
		String text = br.lines().collect(Collectors.joining("\n"));
		load(text);
		currentFile = null;
	} catch (MalformedURLException ex) {
		System.err.println(ex);
	} catch (IOException ex) {
		System.err.println(ex);
	}
     }

     private void load(String text) {
     	String[] lines = text.split("\n");
        for (int i = 0; i < 9; i++) {
     	    for (int j = 0; j < 9; j++) {
                	char c = lines[i].charAt(j);
               	String s = String.valueOf(c);
                	if (c >= '1' && c <= '9') {
                    grid[i][j].setForeground(foregroundColor);
                    grid[i][j].setEditable(false);
                	}
		grid[i][j].setText(CharacterEncoding.dataToDisplay(s));  
		grid[i][j].setBackground(backgroundColor);
            }
        }
    }

    private int getValueAt(int row, int col) {
	String s = grid[row][col].getText();
        if (s.equals(""))
	    return 0;
	return Integer.parseInt(s);
    }

    private void removeHighlighting() {
	for (int row = 0; row < 9; row++) {
		for (int col = 0; col < 9; col++) {
			grid[row][col].setBorder(BorderFactory.createEmptyBorder());
		}
	}
	highlightingOn = false;
    }

    private void highlightRow(int row) {
	for (int col = 0; col < 9; col++) {
	    grid[row][col].setBorder(new LineBorder(highlightColor, 1));
	}
	highlightingOn = true;
    }

    private void highlightColumn(int col) {
	for (int row = 0; row < 9; row++) {
	    grid[row][col].setBorder(new LineBorder(highlightColor, 1));
	}
	highlightingOn = true;
    }

    private void highlightSubgrid(int x, int y) {
	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
		grid[x+i][y+j].setBorder(new LineBorder(highlightColor, 1));
	    }
	}
	highlightingOn = true;
    }

    private boolean validateSubgrid(int x, int y) {
	boolean[] values = new boolean[10];
	Arrays.fill(values, false);
	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
	        int value = getValueAt(x+i, y+j);
		values[value] = true;
	    }
	}
	boolean isValid = IntStream.range(1, 10).filter(i->values[i]).count() == 9;
	if (!isValid)
	    highlightSubgrid(x, y);
	return isValid;
    }	

    private boolean validateRow(int row) {
	boolean[] values = new boolean[10];
	Arrays.fill(values, false);
	for (int col = 0; col < 9; col++) {
	    int value = getValueAt(row, col);
	    values[value] = true;
	}
	boolean isValid = IntStream.range(1, 10).filter(i->values[i]).count() == 9;
	if (!isValid)
	    highlightRow(row);
	return isValid;
    }

    private boolean validateColumn(int col) {
	boolean[] values = new boolean[10];
	Arrays.fill(values, false);
	for (int row = 0; row < 9; row++) {
	    int value = getValueAt(row, col);
	    values[value] = true;
	}
	boolean isValid = IntStream.range(1, 10).filter(i->values[i]).count() == 9;
	if (!isValid)
	    highlightColumn(col);
	return isValid;
    }

    private boolean validatePuzzle() {
        boolean isValid = true;
	for (int row = 0; row < 9; row++) {
            if (!validateRow(row)) {
		isValid = false;
            }
	}
	for (int col = 0; col < 9; col++) {
	    boolean isColumnValid = validateColumn(col);
	    if (!validateColumn(col)) {
		isValid = false;
            }
        }
	for (int x = 0; x < 9; x += 3) {
	    for (int y = 0; y < 9; y += 3) {
		if (!validateSubgrid(x, y)) {
		    isValid = false;
                }
	    }
	}
	return isValid;
    }

    private void enableDisableMenuItems() {
	saveFile.setEnabled(currentFile != null);
    }

    public void menuSelected(MenuEvent e) {
	enableDisableMenuItems();
    }

    public void menuDeselected(MenuEvent e) {}

    public void menuCanceled(MenuEvent e) {}

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == newFile) {
	    newFile();
	}
        else if (e.getSource() == saveFile) {
            saveToFile(currentFile);
        }
	else if (e.getSource() == saveFileAs) {
	    saveToFileAs();
	}
        else if (e.getSource() == openFile) {
            openFile();
        }
        else if (e.getSource() == download) {
           String url = JOptionPane.showInputDialog(this, "URL:", "Download puzzle from the internet", JOptionPane.QUESTION_MESSAGE);
	   if (url != null && url.length() > 0) {
		download(url);
	   }   
        }
        else if (e.getSource() == setForegroundColor) {
            Color color = JColorChooser.showDialog(null, "Select a foreground color", foregroundColor);
	    if (color != null) {
		foregroundColor = color;
	    	refreshColors();
	    }
        }
        else if (e.getSource() == setBackgroundColor) {
            Color color = JColorChooser.showDialog(null, "Select a background color", backgroundColor);
	    if (color != null) {
		backgroundColor = color;
	    	refreshColors();
	    }
        }
        else if (e.getSource() == setHighlightColor) {
            Color color = JColorChooser.showDialog(this, "Select a highlight color", highlightColor);
	    if (color != null) {
		highlightColor = color;
	    }
        }
	else if (e.getSource() == whiteblack) {
	    foregroundColor = Color.BLACK;
	    backgroundColor = Color.WHITE;
	    refreshColors();
	}
	else if (e.getSource() == whitegray) {
	    foregroundColor = Color.LIGHT_GRAY;
	    backgroundColor = Color.WHITE;
	    refreshColors();
	}
	else if (e.getSource() == grayblue) {
	    foregroundColor = new Color(0, 0, 204, 255);
	    backgroundColor = new Color(204, 204, 204, 255);
            refreshColors();
	}
	else if (e.getSource() == tealwhite) {
	    foregroundColor = Color.WHITE;
	    backgroundColor = new Color(0, 153, 153, 255);
	    refreshColors();
	}
	else if (e.getSource() == purplewhite) {
	    foregroundColor = Color.WHITE;
	    backgroundColor = new Color(153, 0, 153, 255);
	    refreshColors();
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
	else if (e.getSource() == validateSquares) {
	    boolean isValid = validatePuzzle();
	    if (isValid) {
		JOptionPane.showMessageDialog(this, "You solved the magic squares puzzle", "Solved!", JOptionPane.INFORMATION_MESSAGE);
	    }
	    else {
		JOptionPane.showMessageDialog(this, "The solution is not valid", "The solution is not valid", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
        else if (e.getSource() == exit) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }
    }

    public void keyTyped(KeyEvent e) {
 	if (highlightingOn) {
		removeHighlighting();
	}
    }

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    private static void setupKeyStrokes() {
	InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
    }

    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
	setupKeyStrokes();
    }

    public void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel); 
        } catch (Exception e) {
            System.err.println(e);
        }
        SwingUtilities.updateComponentTreeUI(this);
	setupKeyStrokes();
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println(e);
        }
	setupKeyStrokes();
    }

    public static void main(String[] args) {
        MagicSquares.setLookAndFeel();
        MagicSquares frame = new MagicSquares();
        frame.createAndShowGui();
        frame.setVisible(true);
    }
}
