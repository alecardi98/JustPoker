package THRProject.gui;

import THRProject.client.Client;
import THRProject.card.Card;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameTablePane extends VBox {

    private Client client;
    private int clientId;

    private Label phaseLabel;
    private Label turnLabel;
    private HBox cardsPane;

    private Button invitaBtn;
    private Button apriBtn;
    private Button passaBtn;
    private Button cambioBtn;
    private Button puntaBtn;
    private Button servitoBtn;

    public GameTablePane(Client client, Game game, int clientId) {
        this.client = client;
        this.clientId = clientId;

        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        phaseLabel = new Label("Fase: ---");
        turnLabel = new Label("Turno: ---");

        cardsPane = new HBox(10);
        cardsPane.setAlignment(Pos.CENTER);

        invitaBtn = new Button("Invito");
        apriBtn = new Button("Apri");
        passaBtn = new Button("Passa");
        cambioBtn = new Button("Cambio");
        puntaBtn = new Button("Punta");
        servitoBtn = new Button("Servito");

        // Eventi pulsanti (usano metodi già esistenti in Client)
        invitaBtn.setOnAction(e -> client.invioInvito());
        apriBtn.setOnAction(e -> client.invioApertura(0)); // puntata placeholder
        passaBtn.setOnAction(e -> client.invioPassa());
        cambioBtn.setOnAction(e -> client.invioCambio());
        puntaBtn.setOnAction(e -> client.invioPuntata(0)); // puntata placeholder
        servitoBtn.setOnAction(e -> client.invioServito());

        HBox buttonsPane = new HBox(10, invitaBtn, apriBtn, passaBtn, cambioBtn, puntaBtn, servitoBtn);
        buttonsPane.setAlignment(Pos.CENTER);

        getChildren().addAll(phaseLabel, turnLabel, cardsPane, buttonsPane);
    }

    /**
     * Aggiorna la visualizzazione della mano e dello stato del gioco
     */
    public void refresh() {
        Game game = client.getGameView(); // il Client ha già la reference al Game
        if (game == null) return;

        // Fase di gioco
        phaseLabel.setText("Fase: " + game.getPhase());

        // Turno
        if (game.getCurrentTurn() == clientId) {
            turnLabel.setText("È il tuo turno");
        } else {
            turnLabel.setText("Turno avversario");
        }

        // Carte
        cardsPane.getChildren().clear();
        Player me = game.getPlayers().get(clientId);
        if (me != null) {
            for (Card c : me.getHand().getCards()) {
                cardsPane.getChildren().add(createCardView(c));
            }
        }

        // Abilita/disabilita pulsanti in base alla fase e allo stato del giocatore
        boolean myTurn = game.getCurrentTurn() == clientId && !me.getStatus().isFold() && !me.getStatus().isEnd();

        invitaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.INVITO);
        apriBtn.setDisable(!myTurn || game.getPhase() != GamePhase.APERTURA);
        passaBtn.setDisable(!myTurn);
        cambioBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
        puntaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.PUNTATA);
        servitoBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
    }

    /**
     * Crea un nodo JavaFX per visualizzare la carta
     */
    private VBox createCardView(Card card) {
        Label cardLabel = new Label(card.toString());
        VBox box = new VBox(cardLabel);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: black; -fx-padding: 5px;");
        return box;
    }
}
