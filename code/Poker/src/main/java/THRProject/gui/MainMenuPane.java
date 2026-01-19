package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainMenuPane extends VBox {

    private SceneManager manager;
    private Client client;

    public MainMenuPane(SceneManager manager, Client client) {
        this.manager = manager;
        this.client = client;

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);

        Label title = new Label("JustPoker™");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Button playButton = new Button("Partecipa a una partita");
        Button rulesButton = new Button("Regolamento");
        Button exitButton = new Button("Esci dal gioco");

        Label infoLabel = new Label();

        playButton.setMinWidth(220);
        rulesButton.setMinWidth(220);
        exitButton.setMinWidth(220);

        /* -------------------
         * PARTECIPA A PARTITA
         * ------------------- */
        playButton.setOnAction(e -> {
            infoLabel.setText("Connessione al server...");
            
            // Qui in futuro:
            // client.startClient();
            // client.joinLobby();

            infoLabel.setText("In attesa di una partita...");
            
            manager.showGameTable();
        });

        /* -------------
         * REGOLAMENTO
         * ------------- */
        rulesButton.setOnAction(e -> showRulesDialog());

        /* ----------
         * ESCI
         * ---------- */
        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        getChildren().addAll(
                title,
                playButton,
                rulesButton,
                exitButton,
                infoLabel
        );
    }

    /* ==========================
     * FINESTRA REGOLAMENTO
     * ========================== */
    private void showRulesDialog() {

        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("Regolamento - JustPoker™");
        rulesAlert.setHeaderText("Regole del gioco");

        rulesAlert.setContentText(
                """
                - Ogni giocatore riceve 5 carte
                - È possibile aprire, passare o puntare
                - Dopo il cambio carte si procede al servito
                - Vince la mano migliore
                """
        );

        rulesAlert.showAndWait();
    }
}
