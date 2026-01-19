package THRProject.gui;

import THRProject.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private Client client;

	@Override
    public void start(Stage stage) {
        SceneManager manager = new SceneManager(stage, client);
        manager.showLoginScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
