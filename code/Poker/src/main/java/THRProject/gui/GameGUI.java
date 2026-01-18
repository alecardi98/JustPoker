package THRProject.gui;

import java.util.ArrayList;
import java.util.List;


import THRProject.card.Card;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.message.ActionType;
import THRProject.message.Message;
import THRProject.player.Player;
import THRProject.player.PlayerStatus;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GameGUI extends Application {

    private Player player;
    private Game game;

    private Label phaseLabel;
    private Label fichesLabel;
    private Label potLabel;

    private Button btnInvito;
    private Button btnApri;
    private Button btnPassa;
    private Button btnCambio;
    private Button btnServito;
    private Button btnPunta;
    private Button btnLascia;

    private TextField puntataField;

    private List<Card> selectedCards = new ArrayList<>();
    private Button[] cardButtons = new Button[5];

    @Override
    public void start(Stage stage) {
        // --- MOCK per test GUI (verranno dal server) ---
        player = new Player("Player1", "pwd");
        game = new Game(10);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // --- TOP ---
        phaseLabel = new Label("Fase: " + game.getPhase());
        potLabel = new Label("Piatto: 0");
        fichesLabel = new Label("Fiches: 0");
        
        HBox topBar = new HBox(20);
        topBar.getChildren().addAll(phaseLabel, potLabel, fichesLabel);
        topBar.setAlignment(Pos.CENTER);
        root.setTop(topBar);

        // --- CENTER (CARTE) ---
        HBox cardsBox = new HBox(10);
        cardsBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < 5; i++) {
            Button cardBtn = new Button("Carta");
            cardBtn.setMinSize(80, 120);

            final int index = i;
            cardBtn.setOnAction(e -> onCardClicked(index));

            cardButtons[i] = cardBtn;
            cardsBox.getChildren().add(cardBtn);
        }

        root.setCenter(cardsBox);

        // --- BOTTOM (AZIONI) ---
        btnInvito = new Button("Invito");
        btnApri = new Button("Apri");
        btnPassa = new Button("Passa");
        btnCambio = new Button("Cambio");
        btnServito = new Button("Servito");
        btnPunta = new Button("Punta");
        btnLascia = new Button("Lascia");

        puntataField = new TextField();
        puntataField.setPromptText("Importo");

        btnInvito.setOnAction(e -> send(player.invito()));
        btnApri.setOnAction(e -> send(player.apri(getBet())));
        btnPassa.setOnAction(e -> send(player.passa()));
        btnCambio.setOnAction(e -> sendCambio());
        btnServito.setOnAction(e -> send(player.servito()));
        btnPunta.setOnAction(e -> send(player.punta(getBet())));
        btnLascia.setOnAction(e -> send(player.lascia()));

        HBox bottomBar = new HBox(
                10,
                btnInvito,
                btnApri,
                btnPassa,
                btnCambio,
                btnServito,
                puntataField,
                btnPunta,
                btnLascia
        );
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));

        root.setBottom(bottomBar);

        updateButtons();

        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("JustPoker™");
        stage.setScene(scene);
        stage.show();
    }

    // =========================
    // LOGICA GUI
    // =========================

    private void onCardClicked(int index) {
        if (game.getPhase() != GamePhase.ACCOMODO) return;

        if (player.getHand().getCards().size() <= index) return;

        Card c = player.getHand().getCards().get(index);

        if (selectedCards.contains(c)) {
            selectedCards.remove(c);
            cardButtons[index].setStyle("");
        } else {
            if (selectedCards.size() < 5) {
                selectedCards.add(c);
                cardButtons[index].setStyle("-fx-background-color: lightgray");
            }
        }
    }

    private void sendCambio() {
        Message msg = new Message(ActionType.CAMBIO, new ArrayList<>(selectedCards));
        selectedCards.clear();
        send(msg);
    }

    private int getBet() {
        try {
            return Integer.parseInt(puntataField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void send(Message msg) {
        // QUI normalmente invieresti il messaggio al server
        System.out.println("Invio messaggio: " + msg.getType());

        updateGUI();
    }

    private void updateGUI() {
        phaseLabel.setText("Fase: " + game.getPhase());
        potLabel.setText("Piatto: " + game.getPot().getTotal());
        fichesLabel.setText("Fiches: " + player.getStatus().getFiches());

        updateCards();
        updateButtons();
    }

    private void updateCards() {
        for (int i = 0; i < 5; i++) {
            if (player.getHand().getCards().size() > i) {
                cardButtons[i].setText(player.getHand().getCards().get(i).toString());
            } else {
                cardButtons[i].setText("Carta");
            }
        }
    }

    private void updateButtons() {
        GamePhase phase = game.getPhase();
        PlayerStatus status = player.getStatus();

        btnInvito.setDisable(phase != GamePhase.INVITO);
        btnApri.setDisable(phase != GamePhase.APERTURA);
        btnPassa.setDisable(phase != GamePhase.APERTURA);
        btnCambio.setDisable(phase != GamePhase.ACCOMODO);
        btnServito.setDisable(phase != GamePhase.ACCOMODO);
        btnPunta.setDisable(phase != GamePhase.PUNTATA);
        btnLascia.setDisable(status.isFold());
    }

    public static void main(String[] args) {
        launch(args);
    }
}

