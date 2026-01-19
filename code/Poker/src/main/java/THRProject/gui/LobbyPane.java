package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LobbyPane extends VBox {

    private SceneManager manager;
    private Client client;

    private Label statusLabel;

    public LobbyPane(SceneManager manager, Client client) {
        this.manager = manager;
        this.client = client;

        setSpacing(15);
        setPadding(new Insets(25));
        setAlignment(Pos.CENTER);

        Label title = new Label("Lobby - JustPoker™");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        statusLabel = new Label("In attesa di altri giocatori...");

        Button joinButton = new Button("Partecipa");
        Button leaveButton = new Button("Non partecipare");
        Button readyButton = new Button("Pronto");
        Button quitButton = new Button("Lascia la partita");

        joinButton.setMinWidth(200);
        leaveButton.setMinWidth(200);
        readyButton.setMinWidth(200);
        quitButton.setMinWidth(200);

        /* ----------------
         * PARTECIPA
         * ---------------- */
        joinButton.setOnAction(e -> {
            // client.joinGame();
            statusLabel.setText("Hai partecipato alla partita.");
        });

        /* ----------------
         * NON PARTECIPARE
         * ---------------- */
        leaveButton.setOnAction(e -> {
            // client.leaveGame();
            statusLabel.setText("Non partecipi alla partita.");
        });

        /* --------------
         * PRONTO
         * -------------- */
        readyButton.setOnAction(e -> {
            // client.sendReady();
            statusLabel.setText("Sei pronto. In attesa degli altri...");
        });

        /* -------------------
         * LASCIA PARTITA
         * ------------------- */
        quitButton.setOnAction(e -> {
            // client.quit();
            manager.showMainMenu();
        });

        getChildren().addAll(
                title,
                statusLabel,
                joinButton,
                leaveButton,
                readyButton,
                quitButton
        );
    }

    /* =========================
     * AGGIORNAMENTO DA SERVER
     * ========================= */
    public void updateFromServer(String msg) {
        statusLabel.setText(msg);
    }
}
