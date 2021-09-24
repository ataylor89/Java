import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class SeaShore extends Application {
	
	private WebView browser;
	private WebEngine engine;

	public SeaShore() {
		browser = new WebView();
		engine = browser.getEngine();
	}

	@Override
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu webMenu = new Menu("Web");
		MenuItem openURL = new MenuItem("URL");
		openURL.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("URL");
			dialog.setHeaderText("URL:");
			dialog.getDialogPane().setMinWidth(500);
			Optional<String> url = dialog.showAndWait();	
			if (url.isPresent()) {
				engine.load(url.get());
			}	
		});
		MenuItem openHomePage = new MenuItem("Home");
		MenuItem openSearchPage = new MenuItem("Search");
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> stage.close());
		webMenu.getItems().addAll(openURL, openHomePage, openSearchPage, exit);
		menuBar.getMenus().add(webMenu);
		root.setTop(menuBar);
		root.setCenter(browser);
		Scene scene = new Scene(root, 1200, 1200);
		engine.load("https://docs.oracle.com/javase/8/docs/api/");
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