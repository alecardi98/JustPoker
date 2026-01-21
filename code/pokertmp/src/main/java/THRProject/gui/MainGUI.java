package THRProject.gui;

import THRProject.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale per l'avvio dell'applicazione GUI
 * 
 * MODIFICHE:
 * - Inizializzazione corretta del Client
 * - Aggiunto metodo init() per preparare il client prima dello start
 */
public class MainGUI extends Application {

    private Client client;

    @Override
    public void start(Stage stage) {
        client = new Client();
        client.startClient();
        SceneManager manager = new SceneManager(stage, client);
        manager.showLoginScene();
    }

    @Override
    public void stop() throws Exception {
        // Cleanup quando l'applicazione si chiude
        if (client != null) {
            client.quit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
