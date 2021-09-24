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
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import java.util.Optional;

public class WebBrowser extends Application {
	
	private BorderPane root;
	private TabPane tabPane;
	private MenuBar menuBar;
	private Menu webMenu;
	private MenuItem openURL, openFile, openHomePage, openSearchPage, exit;
	private String home = "https://docs.oracle.com/javase/8/docs/api/";

	public WebBrowser() {
	
	}
	
	private void createTab(String title, String url) {
		WebView browser = new WebView();
		Tab tab = new Tab(title, browser);
		tabPane.getTabs().add(tab);
		browser.getEngine().load(url);	
		tabPane.getSelectionModel().select(tab);
	}

	private void createTab(String url) {
		createTab("Tab", url);
	}

	@Override
	public void start(Stage stage) {
		root = new BorderPane();
		menuBar = new MenuBar();
		webMenu = new Menu("Web");
		openURL = new MenuItem("URL");
		openURL.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("URL");
			dialog.setHeaderText("URL:");
			dialog.getDialogPane().setMinWidth(500);
			Optional<String> url = dialog.showAndWait();	
			if (url.isPresent()) 
				createTab(url.get());
		});
		openHomePage = new MenuItem("Home");
		openHomePage.setOnAction(e -> {
			createTab("Home Page", home);
		});
		openSearchPage = new MenuItem("Search");
		exit = new MenuItem("Exit");
		exit.setOnAction(e -> stage.close());
		webMenu.getItems().addAll(openURL, openHomePage, openSearchPage, exit);
		menuBar.getMenus().add(webMenu);
		root.setTop(menuBar);
		tabPane = new TabPane();
		createTab("Home Page", home);
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