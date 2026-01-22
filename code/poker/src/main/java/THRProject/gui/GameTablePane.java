package THRProject.gui;

import THRProject.card.model.Card;
import THRProject.card.model.Suit;
import THRProject.client.ClientObserver;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Pannello principale del tavolo da gioco
 * 
 * MODIFICHE: - Aggiunto input per puntata con TextField - Aggiunta selezione
 * carte per cambio - Migliorata visualizzazione carte coperte/scoperte -
 * Aggiunta visualizzazione piatto e fiches - Aggiunti pulsanti Quit e Pronto -
 * Corretto abilitazione/disabilitazione pulsanti
 */
public class GameTablePane extends VBox implements ClientObserver{

	private SceneManager manager;

	private Label phaseLabel;
	private Label turnLabel;
	private Label potLabel;
	private Label fichesLabel;

	private HBox cardsPane;

	private Button invitaBtn;
	private Button apriBtn;
	private Button passaBtn;
	private Button cambioBtn;
	private Button puntaBtn;
	private Button servitoBtn;
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

		fichesLabel = new Label("Fiches: 0");
		fichesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

		infoPane.getChildren().addAll(phaseLabel, turnLabel, potLabel, fichesLabel);

		// Pannello carte del giocatore
		cardsPane = new HBox(10);
		cardsPane.setAlignment(Pos.CENTER);
		cardsPane.setStyle(
				"-fx-border-color: white; -fx-border-width: 2px; -fx-padding: 15px; -fx-background-color: rgba(255,255,255,0.1);");

		// Pannello puntata
		HBox betPane = new HBox(10);
		betPane.setAlignment(Pos.CENTER);

		betAmountField = new TextField();
		betAmountField.setPromptText("Importo puntata");
		betAmountField.setPrefWidth(100);
		betAmountField.setText("25"); // Valore di default

		Label betLabel = new Label("Puntata:");
		betLabel.setStyle("-fx-text-fill: white;");

		betPane.getChildren().addAll(betLabel, betAmountField);

		// Pulsanti azioni
		invitaBtn = new Button("Invito");
		apriBtn = new Button("Apri");
		passaBtn = new Button("Passa");
		cambioBtn = new Button("Cambio");
		puntaBtn = new Button("Punta");
		servitoBtn = new Button("Servito");
		foldBtn = new Button("Fold");
		readyBtn = new Button("Pronto");
		quitBtn = new Button("Abbandona");

		// Stile pulsanti
		String buttonStyle = "-fx-min-width: 80px; -fx-font-weight: bold;";
		invitaBtn.setStyle(buttonStyle + "-fx-background-color: #28a745; -fx-text-fill: white;");
		apriBtn.setStyle(buttonStyle + "-fx-background-color: #007bff; -fx-text-fill: white;");
		passaBtn.setStyle(buttonStyle + "-fx-background-color: #6c757d; -fx-text-fill: white;");
		cambioBtn.setStyle(buttonStyle + "-fx-background-color: #ffc107; -fx-text-fill: black;");
		puntaBtn.setStyle(buttonStyle + "-fx-background-color: #dc3545; -fx-text-fill: white;");
		servitoBtn.setStyle(buttonStyle + "-fx-background-color: #17a2b8; -fx-text-fill: white;");
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
				manager.getClient().invioCambio((ArrayList<Card>) selectedCards);
				selectedCards.clear(); // Pulisci selezione dopo il cambio
			}
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

		servitoBtn.setOnAction(e -> manager.getClient().invioServito());
		foldBtn.setOnAction(e -> manager.getClient().invioLascia());
		readyBtn.setOnAction(e -> {
			manager.getClient().ready();
			readyBtn.setDisable(true);
			manager.showGameTable();
		});

		quitBtn.setOnAction(e -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Conferma uscita");
			confirm.setHeaderText("Sei sicuro di voler lasciare il tavolo?");

			if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
				manager.getClient().quit();
			}
		});

		HBox buttonsPane = new HBox(10, invitaBtn, apriBtn, passaBtn, cambioBtn, puntaBtn, servitoBtn, foldBtn);
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
		if (game == null)
			return;

		// Fase di gioco
		phaseLabel.setText("Fase: " + game.getPhase());

		// Piatto
		potLabel.setText("Piatto: " + game.getPot().getTotal());

		// Turno
		Player me = game.getPlayers().get(manager.getClient().getClientId());
		if (me != null) {
			if (game.getCurrentTurn() == manager.getClient().getClientId()) {
				turnLabel.setText("È il tuo turno");
				turnLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
			} else {
				turnLabel.setText("Turno avversario");
				turnLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
			}

			// Fiches
			fichesLabel.setText("Fiches: " + me.getStatus().getFiches());
		}

		// Carte (solo se è il tuo turno o se le carte sono visibili)
		if (me != null && me.getHand() != null) {
			updatePlayerCards(me);
		}

		// Abilita/disabilita pulsanti in base alla fase e allo stato del giocatore
		boolean myTurn = game.getCurrentTurn() == manager.getClient().getClientId() && me != null && !me.getStatus().isFold()
				&& !me.getStatus().isEnd();
		boolean isEndPhase = game.getPhase() == GamePhase.END || game.getPhase() == GamePhase.ENDPASS;

		invitaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.INVITO);
		apriBtn.setDisable(!myTurn || game.getPhase() != GamePhase.APERTURA);
		passaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.APERTURA);
		cambioBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
		puntaBtn.setDisable(!myTurn || game.getPhase() != GamePhase.PUNTATA);
		servitoBtn.setDisable(!myTurn || game.getPhase() != GamePhase.ACCOMODO);
		foldBtn.setDisable(!myTurn || isEndPhase);

		readyBtn.setVisible(isEndPhase);
		readyBtn.setDisable(false);
		quitBtn.setVisible(isEndPhase);

		betAmountField
				.setDisable(!myTurn || (game.getPhase() != GamePhase.APERTURA && game.getPhase() != GamePhase.PUNTATA));
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
			if (game != null && game.getPhase() == GamePhase.ACCOMODO && game.getCurrentTurn() == manager.getClient().getClientId()) {
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
	
	@Override
	public void onGameViewUpdate() {
		refresh();
		
	}

	@Override
	public void onLoginResult(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReceived(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTornaMenu() {
		// TODO Auto-generated method stub
		
	}
}
