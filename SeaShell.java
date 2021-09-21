import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SeaShell extends JFrame implements KeyListener, ActionListener {
	
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem newTab, closeTab, saveFile, openFile, exit;
	private JMenu colors;
	private JMenuItem purplewhite, tealwhite, grayblue, bluegray, whitepurple, whiteteal, setForegroundColor, setBackgroundColor;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JFileChooser fileChooser;
	private Color foregroundColor, backgroundColor;
	private final Color purple = new Color(153, 0, 153, 255);
	private final Color teal = new Color(0, 153, 153, 255);
	private final Color gray = new Color(204, 204, 204, 255);
	private final Color blue = new Color(0, 0, 204, 255);

	private class ProcessController extends Thread {
		
		private JDialog dialog;
		private JTextArea display;
		private String title;
		private String cmd;

		public ProcessController(JFrame frame, String title, String cmd) {
			dialog = new JDialog(frame, title);
			dialog.setSize(700, 500);
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			display = new JTextArea();
			JScrollPane sp = new JScrollPane(display);
			panel.add(sp);
			dialog.add(panel);
			dialog.setVisible(true);
		}

		@Override
		public void run() {
			try {
				Process process = Runtime.getRuntime().exec(cmd);
				BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	
				while (dialog.isShowing() && process.isAlive()) {
					inputStream.lines().forEach(line -> display.append(line + "\n"));
					errorStream.lines().forEach(line -> display.append("Error: " + line + "\n"));
				}
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	public SeaShell() {
		super("SeaShell");
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
		colors.add(setForegroundColor);
		setBackgroundColor = new JMenuItem("Set background color");
		setBackgroundColor.addActionListener(this);
		colors.add(setBackgroundColor);
		purplewhite = new JMenuItem("Purple white");
		purplewhite.addActionListener(this);
		colors.add(purplewhite);
		tealwhite = new JMenuItem("Teal white");
		tealwhite.addActionListener(this);
		colors.add(tealwhite);
		grayblue = new JMenuItem("Gray blue");
		grayblue.addActionListener(this);
		colors.add(grayblue);
		whitepurple = new JMenuItem("White purple");
		whitepurple.addActionListener(this);
		colors.add(whitepurple);
		whiteteal = new JMenuItem("White teal");
		whiteteal.addActionListener(this);
		colors.add(whiteteal);
		bluegray = new JMenuItem("Blue gray");
		bluegray.addActionListener(this);
		colors.add(bluegray);
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
		this.foregroundColor = fgColor;
		this.backgroundColor = bgColor;
		JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
		JTextArea ta = (JTextArea) sp.getViewport().getView();
		ta.setForeground(foregroundColor);
		ta.setBackground(backgroundColor);
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JScrollPane sp = (JScrollPane) tabbedPane.getSelectedComponent();
			JTextArea ta = (JTextArea) sp.getViewport().getView();
			ta.append("& ");
		}	
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newTab) {
			String title = JOptionPane.showInputDialog(this, "Title:", "New tab", JOptionPane.QUESTION_MESSAGE);
			JTextArea textArea = new JTextArea();
			textArea.append("& ");
			textArea.addKeyListener(this);
			JScrollPane scrollPane = new JScrollPane(textArea);
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
		} else if (e.getSource() == backgroundColor) {
			Color color = JColorChooser.showDialog(this, "Select a background color", backgroundColor);
			if (color != null) backgroundColor = color;
			setBackgroundColor(backgroundColor);
		} else if (e.getSource() == purplewhite) {
			setColors(Color.WHITE, purple);
		} else if (e.getSource() == tealwhite) {
			setColors(Color.WHITE, teal);
		} else if (e.getSource() == grayblue) {
			setColors(blue, gray);
		} else if (e.getSource() == whitepurple) {
			setColors(purple, Color.WHITE);
		} else if (e.getSource() == whiteteal) {
			setColors(teal, Color.WHITE);
		} else if (e.getSource() == bluegray) {
			setColors(gray, blue);
		}				
	}

	public static void main(String[] args) {
		SeaShell seaShell = new SeaShell();
		seaShell.createAndShowGui();
	}	
}