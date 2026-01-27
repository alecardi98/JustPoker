package THRProject.gui;

import THRProject.card.model.Card;
import THRProject.card.model.Suit;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.player.Player;
import THRProject.server.Server;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Pannello principale del tavolo da gioco
 * 
 * MODIFICHE: - Aggiunto input per puntata con TextField - Aggiunta selezione
 * carte per cambio - Migliorata visualizzazione carte coperte/scoperte -
 * Aggiunta visualizzazione piatto e fiches - Aggiunti pulsanti Quit e Pronto -
 * Corretto abilitazione/disabilitazione pulsanti
 */
public class GameTablePane extends VBox {

	private SceneManager manager;

	private Label phaseLabel;
	private Label turnLabel;
	private Label potLabel;
	private Label myMaxBetLabel;
	private Label fichesLabel;
	private Label minBet;
	private Label maxBet;

	private HBox cardsPane;
	private HBox opponentsCardsPane;

	private Button invitaBtn;
	private Button apriBtn;
	private Button passaBtn;
	private Button cambioBtn;
	private Button servitoBtn;
	private Button puntaBtn;
	private Button foldBtn;
	private Button readyBtn;
	private Button quitBtn;

	private TextField betAmountField;

	private List<Card> selectedCards; // Carte selezionate per il cambio

	public GameTablePane(SceneManager manager) {

		this.manager = manager;

		this.selectedCards = new ArrayList<>();

		setSpacing(15);
		setPadding(new Insets(20));
		setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: #0F5132;"); // Colore feltro da poker

		// Pannello informazioni di gioco
		HBox infoPane = new HBox(20);
		infoPane.setAlignment(Pos.CENTER);

		phaseLabel = new Label("Fase: ---");
		phaseLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

		turnLabel = new Label("Turno: ---");
		turnLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

		potLabel = new Label("Piatto: 0");
		potLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: gold;");

		myMaxBetLabel = new Label("MyBet: 0");
		myMaxBetLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

		fichesLabel = new Label("Fiches: 0");
		fichesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

		infoPane.getChildren().addAll(phaseLabel, turnLabel, potLabel, myMaxBetLabel, fichesLabel);

		// Pannello carte del giocatore
		cardsPane = new HBox(10);
		cardsPane.setAlignment(Pos.CENTER);
		cardsPane.setStyle(
				"-fx-border-color: white; -fx-border-width: 2px; -fx-padding: 15px; -fx-background-color: rgba(255,255,255,0.1);");

		// Pannello carte degli avversari
		opponentsCardsPane = new HBox(10);
		opponentsCardsPane.setAlignment(Pos.CENTER);
		opponentsCardsPane.setStyle(
				"-fx-border-color: white; -fx-border-width: 2px; -fx-padding: 15px; -fx-background-color: rgba(255,255,255,0.1);");
		getChildren().addAll(opponentsCardsPane);

		// Pannello puntata
		HBox betPane = new HBox(10);
		betPane.setAlignment(Pos.CENTER);

		betAmountField = new TextField();
		betAmountField.setPromptText("Importo puntata");
		betAmountField.setPrefWidth(100);
		betAmountField.setText("25"); // Valore di default

		Label betLabel = new Label("Puntata:");
		betLabel.setStyle("-fx-text-fill: white;");

		minBet = new Label("Min: 25");
		minBet.setStyle("-fx-text-fill: white;");

		maxBet = new Label("Max: ---");
		maxBet.setStyle("-fx-text-fill: white;");

		betPane.getChildren().addAll(betLabel, betAmountField, minBet, maxBet);

		// Pulsanti azioni
		invitaBtn = new Button("Invito");
		apriBtn = new Button("Apri");
		passaBtn = new Button("Passa");
		cambioBtn = new Button("Cambio");
		servitoBtn = new Button("Servito");
		puntaBtn = new Button("Punta");
		foldBtn = new Button("Fold");
		readyBtn = new Button("Pronto");
		quitBtn = new Button("Abbandona");

		// Stile pulsanti
		String buttonStyle = "-fx-min-width: 80px; -fx-font-weight: bold;";
		invitaBtn.setStyle(buttonStyle + "-fx-background-color: #28a745; -fx-text-fill: white;");
		apriBtn.setStyle(buttonStyle + "-fx-background-color: #007bff; -fx-text-fill: white;");
		passaBtn.setStyle(buttonStyle + "-fx-background-color: #6c757d; -fx-text-fill: white;");
		cambioBtn.setStyle(buttonStyle + "-fx-background-color: #ffc107; -fx-text-fill: black;");
		servitoBtn.setStyle(buttonStyle + "-fx-background-color: #17a2b8; -fx-text-fill: white;");
		puntaBtn.setStyle(buttonStyle + "-fx-background-color: #dc3545; -fx-text-fill: white;");
		foldBtn.setStyle(buttonStyle + "-fx-background-color: #fd7e14; -fx-text-fill: white;");
		readyBtn.setStyle(buttonStyle + "-fx-background-color: #20c997; -fx-text-fill: white;");
		quitBtn.setStyle(buttonStyle + "-fx-background-color: #6f42c1; -fx-text-fill: white;");

		// Eventi pulsanti
		invitaBtn.setOnAction(e -> manager.getClient().invioInvito());

		apriBtn.setOnAction(e -> {
			try {
				int amount = Integer.parseInt(betAmountField.getText());
				manager.getClient().invioApertura(amount);
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Puntata non valida");
				alert.setHeaderText(null);
				alert.setContentText("Inserisci un numero valido per la puntata.");
				alert.showAndWait();
			}
		});

		passaBtn.setOnAction(e -> manager.getClient().invioPassa());

		cambioBtn.setOnAction(e -> {
			if (selectedCards.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Nessuna carta selezionata");
				alert.setHeaderText(null);
				alert.setContentText("Seleziona almeno una carta da cambiare.");
				alert.showAndWait();
			} else {
				ArrayList<Card> cards = new ArrayList<Card>(selectedCards);
				manager.getClient().invioCambio((ArrayList<Card>) cards);
				selectedCards.clear(); // Pulisci selezione dopo il cambio
			}
		});

		servitoBtn.setOnAction(e -> manager.getClient().invioServito());
		foldBtn.setOnAction(e -> manager.getClient().invioLascia());
		readyBtn.setOnAction(e -> {
			manager.getClient().ready();
			readyBtn.setDisable(true);
			manager.showGameTable();
		});

		puntaBtn.setOnAction(e -> {
			try {
				int amount = Integer.parseInt(betAmountField.getText());
				manager.getClient().invioPuntata(amount);
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Puntata non valida");
				alert.setHeaderText(null);
				alert.setContentText("Inserisci un numero valido per la puntata.");
				alert.showAndWait();
			}
		});

		quitBtn.setOnAction(e -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Conferma uscita");
			confirm.setHeaderText("Sei sicuro di voler lasciare il tavolo?");

			if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
				manager.getClient().quit();
				manager.getStage().close();
			}
		});

		HBox buttonsPane = new HBox(10, invitaBtn, apriBtn, passaBtn, cambioBtn, servitoBtn, puntaBtn, foldBtn);
		buttonsPane.setAlignment(Pos.CENTER);

		HBox endButtonsPane = new HBox(10, readyBtn, quitBtn);
		endButtonsPane.setAlignment(Pos.CENTER);

		getChildren().addAll(infoPane, cardsPane, betPane, buttonsPane, endButtonsPane);
	}

	/**
	 * Aggiorna la visualizzazione della mano e dello stato del gioco
	 */
	public void refresh() {
		Game game = manager.getClient().getGameView();
		Player me = game.getPlayers().get(manager.getClient().getClientId());
		if (game == null)
			return;

		// Fase di gioco
		phaseLabel.setText("Fase: " + game.getPhase());

		// Piatto
		potLabel.setText("Piatto: " + game.getPot().getTotal());

		myMaxBetLabel.setText(
				"MyBet: " + game.getPlayers().get(manager.getClient().getClientId()).getStatus().getTotalBet());

		if (game.getPlayers().get(manager.getClient().getClientId()).getStatus().getTotalBet() >= game.getPot()
				.getMaxBet())
			myMaxBetLabel.setTextFill(Color.GREEN);
		else
			myMaxBetLabel.setTextFill(Color.RED);

		betAmountField.setText("" + (game.getPot().getMaxBet() - me.getStatus().getTotalBet())); // puntata minima

		minBet.setText("( " + (game.getPot().getMaxBet() - me.getStatus().getTotalBet()));

		if (game.getPot().getTotal() == 0)
			maxBet.setText((game.getPot().getMinBet()) + " )");
		else if (game.getPot().getTotal() > Server.MAXBET)
			maxBet.setText((Server.MAXBET - me.getStatus().getTotalBet()) + " )");
		else
			maxBet.setText((game.getPot().getTotal() - me.getStatus().getTotalBet()) + " )");

		// Turno

		if (me != null) {
			if (game.getCurrentTurn() == manager.getClient().getClientId()) {
				turnLabel.setText("È il tuo turno");
				turnLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
			} else {
				turnLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
				if (!(game.getPhase().equals(GamePhase.END) || game.getPhase().equals(GamePhase.ENDPASS))) {
					turnLabel.setText("Turno di " + game.getPlayers().get(game.getCurrentTurn()).getUsername());
				} else {
					turnLabel.setText("Turno ---");
				}
			}

			// Fiches
			if (me.getStatus().getFiches() > 0)
				fichesLabel.setText("Fiches: " + me.getStatus().getFiches());
			else
				fichesLabel.setText("Fiches: ALL-IN");
		}

		// Carte (solo se è il tuo turno o se le carte sono visibili)
		if (me != null && me.getHand() != null) {
			updatePlayerCards(me);
		}

		// Abilita/disabilita pulsanti in base alla fase e allo stato del giocatore
		boolean myTurn = game.getCurrentTurn() == manager.getClient().getClientId() && me != null
				&& !me.getStatus().isFold() && !me.getStatus().isEnd();
		boolean isEndPhase = game.getPhase() == GamePhase.END || game.getPhase() == GamePhase.ENDPASS;

		invitaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.INVITO);
		apriBtn.setDisable(!myTurn || game.getPhase() != GamePhase.APERTURA);
		passaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.APERTURA || game.isOpen());
		cambioBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
		servitoBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
		puntaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.PUNTATA);
		foldBtn.setDisable(!myTurn || isEndPhase || game.allFold() || me.getStatus().getFiches() <= 0);

		readyBtn.setVisible(isEndPhase);
		readyBtn.setDisable(false);
		quitBtn.setVisible(isEndPhase);

		betAmountField
				.setDisable(!myTurn || (game.getPhase() != GamePhase.APERTURA && game.getPhase() != GamePhase.PUNTATA));

		isEndPhase = game.getPhase() == GamePhase.END || game.getPhase() == GamePhase.ENDPASS;

		if (isEndPhase) {
			updateOtherPlayersCards(game);
		} else {
			opponentsCardsPane.getChildren().clear();
		}

	}

	private void updateOtherPlayersCards(Game game) {
		if (game == null)
			return;

		opponentsCardsPane.getChildren().clear();

		for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {

			// Salta il giocatore locale
			if (entry.getKey() == manager.getClient().getClientId())
				continue;

			Player player = entry.getValue();
			if (player == null)
				continue;

			var hand = player.getHand();

			if (hand == null)
				continue;

			Label name = new Label(player.getUsername());
			name.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

			HBox handPane = new HBox(5);
			handPane.setAlignment(Pos.CENTER);

			for (Card c : hand.getCards()) {
				handPane.getChildren().add(createCardView(c));
			}

			VBox playerBox = new VBox(5, name, handPane);
			playerBox.setAlignment(Pos.CENTER);
			playerBox.setStyle("-fx-border-color: white; -fx-padding: 10px;");

			opponentsCardsPane.getChildren().add(playerBox);
		}
	}

	/**
	 * Aggiorna la visualizzazione delle carte del giocatore
	 */
	private void updatePlayerCards(Player player) {
		cardsPane.getChildren().clear();

		for (Card card : player.getHand().getCards()) {
			VBox cardView = createCardView(card);
			cardsPane.getChildren().add(cardView);
		}
	}

	/**
	 * Crea un nodo JavaFX per visualizzare la carta
	 */
	private VBox createCardView(Card card) {
		Label cardLabel = new Label(card.toString());

		if (card.getSeme().equals(Suit.CUORI) || card.getSeme().equals(Suit.QUADRI))
			cardLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
		else
			cardLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

		VBox box = new VBox(cardLabel);
		box.setAlignment(Pos.CENTER);

		box.setStyle("-fx-border-color: black; -fx-padding: 5px; -fx-background-color: white; -fx-cursor: hand;");

		// Se è la fase di accomodo, permetti la selezione delle carte
		box.setOnMouseClicked(e -> {
			Game game = manager.getClient().getGameView();
			if (game != null && game.getPhase() == GamePhase.ACCOMODO
					&& game.getCurrentTurn() == manager.getClient().getClientId()) {
				if (selectedCards.contains(card)) {
					selectedCards.remove(card);
					box.setStyle(
							"-fx-border-color: black; -fx-padding: 5px; -fx-background-color: white; -fx-cursor: hand;");

				} else if (selectedCards.size() < 3) {
					selectedCards.add(card);
					box.setStyle(
							"-fx-border-color: red; -fx-border-width: 3px; -fx-padding: 5px; -fx-background-color: lightyellow; -fx-cursor: hand;");
				}
			}
		});

		return box;
	}

}
