import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WebBrowser extends Application {
	
	private BorderPane root;
	private TabPane tabPane;
	private MenuBar menuBar;
	private Menu webMenu;
	private MenuItem openURL, openHTMLFile, openTextFile, openImage, openHomePage, openSearchPage, exit;
	private String home = "https://docs.oracle.com/javase/8/docs/api/";

	public WebBrowser() {}
	
	private void openURL(String url) {
		WebView browser = new WebView();
		browser.getEngine().load(url);	
		String title = "Tab";
		try {
			title = new URL(url).getHost();
		} catch (MalformedURLException e) {
			System.err.println(e);
		}
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
	}

	@Override
	public void start(Stage stage) {
		root = new BorderPane();
		menuBar = new MenuBar();
		webMenu = new Menu("Web");
		openURL = new MenuItem("Open URL");
		openURL.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("URL");
			dialog.setHeaderText("URL:");
			dialog.getDialogPane().setMinWidth(500);
			Optional<String> url = dialog.showAndWait();	
			if (url.isPresent()) 
				openURL(url.get());
		});
		openHTMLFile = new MenuItem("Open HTML file");
		openHTMLFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open file");
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openHTMLFile(file);
		});
		openTextFile = new MenuItem("Open text file");
		openTextFile.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open file");
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openTextFile(file);
		});
		openImage = new MenuItem("Open image");
		openImage.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open image");
			File file = fileChooser.showOpenDialog(stage);
			if (file != null)
				openImage(file);
		});
		openHomePage = new MenuItem("Open home page");
		openHomePage.setOnAction(e -> {
			openURL(home);
		});
		openSearchPage = new MenuItem("Open search page");
		exit = new MenuItem("Exit");
		exit.setOnAction(e -> stage.close());
		webMenu.getItems().addAll(openURL, openHTMLFile, openTextFile, openImage, openHomePage, openSearchPage, exit);
		menuBar.getMenus().add(webMenu);
		root.setTop(menuBar);
		tabPane = new TabPane();
		openURL(home);
		root.setCenter(tabPane);
		Scene scene = new Scene(root, 1200, 1200);
		stage.setScene(scene);
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);	
		});
		stage.sizeToScene();
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}