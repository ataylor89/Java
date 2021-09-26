import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.logging.*;

public class SeaShell extends JFrame implements KeyListener, ActionListener {
	
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem newTab, closeTab, saveFile, openFile, exit;
	private JMenu colors;
	private JMenuItem setForegroundColor, setBackgroundColor, blackwhite, graywhite, grayblue, tealwhite, purplewhite, whiteblack, whitegray, bluegray, whiteteal, whitepurple;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JFileChooser fileChooser;
	private Color foregroundColor, backgroundColor;
	private final Color purple = new Color(153, 0, 153, 255);
	private final Color teal = new Color(0, 153, 153, 255);
	private final Color gray = new Color(204, 204, 204, 255);
	private final Color blue = new Color(0, 0, 204, 255);
	private Interpreter interpreter;
	private Logger logger;

	private class SeaShellTab extends JTextArea {
		private String title;
		private File currentDirectory;

		public SeaShellTab(String title) {
			this.title = title;
			currentDirectory = new File(System.getProperty("user.home"));
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}

		public void setCurrentDirectory(File currentDirectory) {
			this.currentDirectory = currentDirectory;
		}

		public File getCurrentDirectory() {
			return currentDirectory;
		}
	}

	private class Interpreter {		
		private Map<String, Integer> tokens;
		private final int LS = 1;
		private final int CD = 2;
		private final int ECHO = 3;
		private final int CAT = 4;
		private final int CP = 5;
		private final int MKDIR = 6;
		private final int COUNT = 7;
		private final int COMPUTE = 8;
		private final int JAVAC = 9;
		private final int JAVA = 10;
		private final int MVN = 11;
		private final int PYTHON = 12;
		private final int GCC = 13;
		private final int GPP = 14;
		private final int NASM = 15;
		private final int LD = 16;
		private final int GIT = 17;

		public Interpreter() {
			tokens = new HashMap<>();
			tokens.put("ls", LS);
			tokens.put("cd", CD);
			tokens.put("echo", ECHO);		
			tokens.put("cat", CAT);
			tokens.put("cp", CP);
			tokens.put("mkdir", MKDIR);
			tokens.put("count", COUNT);
			tokens.put("javac", JAVAC);
			tokens.put("java", JAVA);
			tokens.put("mvn", MVN);
			tokens.put("python", PYTHON);
			tokens.put("gcc", GCC);
			tokens.put("g++", GPP);
			tokens.put("nasm", NASM);
			tokens.put("ld", LD); 
			tokens.put("git", GIT);
		}

		private String[] tokenize(String program) {
			java.util.List<String> tokens = new java.util.ArrayList<>();
			boolean isQuoted = false;
			int position = 0;
			for (int i = 0; i < program.length(); i++) {
				if (i > 0 && i == program.length() - 1) {
					String token = program.substring(position, i);
					tokens.add(token);
				}
				else if (!isQuoted && program.charAt(i) == ' ') {
					String token = program.substring(position, i);
					tokens.add(token);
					position = i + 1;
				}
				else if (program.charAt(i) == '\"') {
					isQuoted = !isQuoted;
				}
			}
			String msg = "Tokens: " + tokens.stream().collect(Collectors.joining(" ")) + "\n";
			logger.log(Level.INFO, msg);
			return tokens.stream().toArray(String[]::new);
		}

		private File getFile(String path, File currentDirectory) {
			if (path.startsWith("~"))
				path = System.getProperty("user.home") + path.substring(1);
			File file = new File(path);
			if (!file.isAbsolute())
				file = new File(currentDirectory, path);	
			return file;
		}

		public void interpret(String program, SeaShellTab display) {
			File currentDirectory = display.getCurrentDirectory();
			String[] tokenarr = tokenize(program);
			if (tokenarr.length == 0)
				return;
			String firstToken = tokenarr[0];
			int tokenId = tokens.containsKey(firstToken) ? tokens.get(firstToken) : -1;
			File file = null;
			switch (tokenId) {
				case LS:
					file = (tokenarr.length == 2) ? getFile(tokenarr[1], currentDirectory) : currentDirectory;
					if (file.isDirectory()) {			
						String[] files = file.list();
						if (files.length > 0) {
							Stream.of(files).forEach(name -> display.append(name + " "));
							display.append("\n");
						}
					}
					display.append("& ");
					break;
				case CD:
					file = getFile(tokenarr[1], currentDirectory);
					if (file.isDirectory())
						display.setCurrentDirectory(file);
					display.append("& ");
					break;
				case ECHO:
					String msg = program.substring(5);
					display.append(msg + "& ");
					break;
				case CAT:
					file = getFile(tokenarr[1], currentDirectory);
					if (file.isFile()) {
						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String contents = reader.lines().collect(Collectors.joining("\n"));
							display.append(contents);
							display.append("\n");
						} catch (IOException e) {	
							System.err.println(e);
						}
					}
					display.append("& ");
					break;
				case COUNT:
					file = getFile(tokenarr[1], currentDirectory);
					if (file.isFile()) {
						try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
							String contents = reader.lines().collect(Collectors.joining("\n"));
							int numChars = contents.length();
							long numLines = contents.split("\n").length;
							display.append("Number of characters: " + numChars + "\n");
							display.append("Number of lines: " + numLines + "\n");
							display.append("& ");
						} catch (IOException e) {	
							System.err.println(e);
						}
					}
					break;
				case CP:
					File source = getFile(tokenarr[1], currentDirectory);
					File dest = getFile(tokenarr[2], currentDirectory);
					if (source.isFile()) {
						try (
							BufferedReader reader = new BufferedReader(new FileReader(source));
							PrintWriter writer = new PrintWriter(new FileOutputStream(dest, false), true)
						) {
							String contents = reader.lines().collect(Collectors.joining("\n"));
							writer.write(contents);
						} catch (IOException e) {
							System.err.println(e);
						}
					}
					display.append("& ");
					break;
				case MKDIR:
					file = getFile(tokenarr[1], currentDirectory);	
					if (!file.exists())
						file.mkdir();
					display.append("& ");
					break;
				case COMPUTE:
					break;
				case JAVAC:
				case JAVA:
				case MVN:
				case PYTHON:
				case GCC:
				case GPP:
				case NASM:
				case LD:
				case GIT:
					CommandLineProgram process = new CommandLineProgram(tokenarr, display);
					process.start();
					break;		
				default:
					display.append(program + "& ");
			}
		}
	}

	private class CommandLineProgram extends Thread {
		private String[] tokens;
		private SeaShellTab display;		

		public CommandLineProgram(String[] tokens, SeaShellTab display) {
			this.tokens = tokens;
			this.display = display;
		}
	
		@Override
		public void run() {		
			ProcessBuilder pb = new ProcessBuilder(tokens);
			pb.directory(display.getCurrentDirectory());
			pb.redirectErrorStream(true);
			try {
				Process process = pb.start();
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
				display.setEnabled(false);
				while (process.isAlive()) {
					inputStream.lines().forEach(line -> display.append(line + "\n"));
				}
				display.append("& ");
				display.setEnabled(true);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	public SeaShell() {
		super("SeaShell");
		interpreter = new Interpreter();
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
			logger.log(Level.INFO, e.toString());
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
		menuBar.add(file);
		menuBar.add(colors);
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
				System.err.println(e);
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
				System.err.println(e);
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
			int offset = seaShellTab.getCaretPosition();
			try { 
				int lineNumber = seaShellTab.getLineOfOffset(offset) - 1;
				int startIndex = seaShellTab.getLineStartOffset(lineNumber);
				int endIndex = seaShellTab.getLineEndOffset(lineNumber);
				String text = seaShellTab.getText();
				String program = text.substring(startIndex+2, endIndex);
				interpreter.interpret(program, seaShellTab);
			} catch (BadLocationException ex) {
				System.err.println(ex);
			}
		}	
	} 

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newTab) {
			String title = JOptionPane.showInputDialog(this, "Title:", "New tab", JOptionPane.QUESTION_MESSAGE);
			SeaShellTab seaShellTab = new SeaShellTab(title);
			seaShellTab.append("& ");
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
			if (color != null) foregroundColor = color;
			setForegroundColor(foregroundColor);
		} else if (e.getSource() == setBackgroundColor) {
			Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
			if (color != null) backgroundColor = color;
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