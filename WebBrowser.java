import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class WebBrowser extends JFrame implements ActionListener {
	
	private JMenuBar menuBar;
	private JMenu web;
	private JMenuItem openURL, openHomePage, openSearchEngine;
	private JPanel panel;	
	private JEditorPane editorPane;

	public WebBrowser() {
		super("Web Browser");
	}

	public void createAndShowGui() {
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar = new JMenuBar();
		web = new JMenu("Web");
		openURL = new JMenuItem("URL");
		openURL.addActionListener(this);
		openHomePage = new JMenuItem("Home");
		openHomePage.addActionListener(this);
		openSearchEngine = new JMenuItem("Search");
		openSearchEngine.addActionListener(this);
		web.add(openURL);
		web.add(openHomePage);
		web.add(openSearchEngine);
		menuBar.add(web);
		setJMenuBar(menuBar);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		try {
			editorPane = new JEditorPane("https://docs.oracle.com/javase/8/docs/api/");
			panel.add(editorPane);
		} catch (IOException e) {
			System.err.println(e);
		}
		add(panel);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

	}

	public static void main(String[] args) {
		WebBrowser webBrowser = new WebBrowser();
		webBrowser.createAndShowGui();
	}
}