package webbrowser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import com.itextpdf.html2pdf.HtmlConverter;
import org.w3c.dom.Document;

public class WebBrowser extends Application {
	
	private BorderPane root;
	private TabPane tabPane;
	private MenuBar menuBar;
	private Menu webMenu;
	private MenuItem openURL, openHTMLFile, openTextFile, openImage, openHomePage, openSearchPage, saveToPDF, convertToPDF, exit;
	private String home = "https://docs.oracle.com/javase/8/docs/api/";

	public WebBrowser() {}
	
	private void openURL(String url) {
		WebView browser = new WebView();
		browser.getEngine().load(url);	
		String title = getHost(url);
		Tab tab = new Tab(title, browser);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
	}

	private void openHTMLFile(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String html = reader.lines().collect(Collectors.joining("\n"));
			WebView browser = new WebView();
			browser.getEngine().loadContent(html);
			Tab tab = new Tab(file.getName(), browser);
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void openTextFile(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String text = reader.lines().collect(Collectors.joining("\n"));
			TextArea textArea = new TextArea(text);
			Tab tab = new Tab(file.getName(), textArea);
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void openImage(File file) {
		Image image = new Image(file.toURI().toString());
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(1150);
		imageView.setPreserveRatio(true);
		Tab tab = new Tab(file.getName(), imageView);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
	}

	public static String documentToString(Document document) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			StringWriter sw = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(sw));
			return sw.toString();
		} catch (TransformerException e) {
			System.err.println(e);
		}
		return null;
	}

	private void saveToPDF(File file) {
		Tab tab = (Tab) tabPane.getSelectionModel().getSelectedItem();
		Node content = tab.getContent();
		if (content instanceof WebView) {
			WebView browser = (WebView) content;
			Document document = ((WebView) content).getEngine().getDocument();
			String data = documentToString(document);
			try {
				HtmlConverter.convertToPdf(data, new FileOutputStream(file));
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	private void convertToPDF(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String data = reader.lines().collect(Collectors.joining("\n"));
			String path = file.getPath();
			int lio = path.lastIndexOf(".");
			if (lio > 0) {
				path = path.substring(0, lio) + ".pdf";
				HtmlConverter.convertToPdf(data, new FileOutputStream(path));
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private String getHost(String url) {
		try { 
			return new URL(url).getHost();
		} catch (MalformedURLException e) {
			System.err.println(e);
		}
		return null;
	}

	private void setupEventHandling(Stage stage) {
		openURL.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("URL");
			dialog.setHeaderText("URL:");
			dialog.getDialogPane().setMinWidth(500);
			Optional<String> url = dialog.showAndWait();	
			if (url.isPresent()) 
				openURL(url.get());
		});
		openHTMLFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open file");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("HTML files", "*.html"));
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openHTMLFile(file);
		});
		openTextFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open file");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files", "*.txt"));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("HTML files", "*.html"));
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openTextFile(file);
		});
		openImage.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open image");
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openImage(file);
		});
		openHomePage.setOnAction(e -> openURL(home));
		saveToPDF.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save to PDF");
			File file = fileChooser.showSaveDialog(stage);
			if (file != null)
				saveToPDF(file);
		});
		convertToPDF.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Convert to PDF");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("HTML files", "*.html"));
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				convertToPDF(file);
		});
		exit.setOnAction(e -> stage.close());
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);	
		});
	}

	@Override
	public void start(Stage stage) {
		root = new BorderPane();
		menuBar = new MenuBar();
		webMenu = new Menu("Web");
		openURL = new MenuItem("Open URL");
		openHTMLFile = new MenuItem("Open HTML file");
		openTextFile = new MenuItem("Open text file");
		openImage = new MenuItem("Open image");
		openHomePage = new MenuItem("Open home page");
		openSearchPage = new MenuItem("Open search page");
		saveToPDF = new MenuItem("Save to PDF");
		convertToPDF = new MenuItem("Convert to PDF");
		exit = new MenuItem("Exit");
		webMenu.getItems().addAll(openURL, openHTMLFile, openTextFile, openImage, openHomePage, openSearchPage, saveToPDF, convertToPDF, exit);
		menuBar.getMenus().add(webMenu);
		root.setTop(menuBar);
		tabPane = new TabPane();
		openURL(home);
		root.setCenter(tabPane);
		setupEventHandling(stage);
		Scene scene = new Scene(root, 1200, 1200);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}