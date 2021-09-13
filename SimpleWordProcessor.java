import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class SimpleWordProcessor extends JFrame implements MenuListener, ActionListener {
	
	private JPanel panel;
	private JTextArea textArea;
	private JMenuBar bar;
	private JMenu file; 
	private JMenuItem newFile, saveFile, saveFileAs, openFile, exit;
	private File currentFile;
	private JFileChooser fileChooser;

	public SimpleWordProcessor() {
		super("Word Processor");
	}

	public void createAndShowGui() {
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bar = new JMenuBar();
		file = new JMenu("File");
		newFile = new JMenuItem("New");
		newFile.addActionListener(this);
		saveFile = new JMenuItem("Save");
		saveFile.addActionListener(this);
		saveFileAs = new JMenuItem("Save as");
		saveFileAs.addActionListener(this);
		openFile = new JMenuItem("Open");
		openFile.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		file.add(newFile);
		file.add(saveFile);
		file.add(saveFileAs);
		file.add(openFile);
		file.add(exit);
		bar.add(file);
		setJMenuBar(bar);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		textArea = new JTextArea();
		panel.add(textArea);
		add(panel);
		fileChooser = new JFileChooser();
		setVisible(true);
	}

	private void saveToFile(File f) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
			writer.write(textArea.getText());
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void openFile(File f) {
		try  {
			String text = new String(Files.readAllBytes(f.toPath()));
			textArea.setText(text);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void menuSelected(MenuEvent e) {
		saveFile.setEnabled(currentFile != null);
	}
	
	public void menuDeselected(MenuEvent e) {}

	public void menuCanceled(MenuEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newFile) {
			currentFile = null;
			textArea.setText("");
		}
		else if (e.getSource() == saveFile) {
			saveToFile(currentFile);
		}
		else if (e.getSource() == saveFileAs) {
			int val = fileChooser.showSaveDialog(this);

			if (val == JFileChooser.APPROVE_OPTION) {
				currentFile = fileChooser.getSelectedFile();
				saveToFile(currentFile);
			}
		}
		else if (e.getSource() == openFile) {
			int val = fileChooser.showOpenDialog(this);

			if (val == JFileChooser.APPROVE_OPTION) {
				currentFile = fileChooser.getSelectedFile();
				openFile(currentFile);
			}
		}
		else if (e.getSource() == exit) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		SimpleWordProcessor swp = new SimpleWordProcessor();
		swp.createAndShowGui();
	}
}