import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

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
		MenuItem openHomePage = new MenuItem("Home");
		MenuItem openSearchPage = new MenuItem("Search");
		webMenu.getItems().addAll(openURL, openHomePage, openSearchPage);
		menuBar.getMenus().add(webMenu);
		root.setTop(menuBar);
		root.setCenter(browser);
		Scene scene = new Scene(root, 1200, 1200);
		engine.load("https://docs.oracle.com/javase/8/docs/api/");
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}