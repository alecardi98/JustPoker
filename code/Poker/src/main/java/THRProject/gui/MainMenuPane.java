package THRProject.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Pannello del menu principale
 * 
 * MODIFICHE: - Integrata connessione al server tramite Client - Aggiunta
 * gestione stati di connessione - Aggiunto feedback visivo durante la
 * connessione
 */
public class MainMenuPane extends VBox {

	private SceneManager manager;

	public MainMenuPane(SceneManager manager) {
		this.manager = manager;

		setSpacing(20);
		setPadding(new Insets(30));
		setAlignment(Pos.CENTER);

		Label title = new Label("JustPoker™");
		title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

		Button playButton = new Button("Partecipa a una partita");
		Button rulesButton = new Button("Regolamento");
		Button exitButton = new Button("Esci dal gioco");

		Label infoLabel = new Label("Benvenuto! Scegli un'opzione.");
		infoLabel.setStyle("-fx-text-fill: blue;");

		playButton.setMinWidth(220);
		rulesButton.setMinWidth(220);
		exitButton.setMinWidth(220);

		/*
		 * ------------------- PARTECIPA A PARTITA -------------------
		 */
		playButton.setOnAction(e -> {
			infoLabel.setStyle("-fx-text-fill: blue;");
			infoLabel.setText("In attesa di altri giocatori...");
			playButton.setDisable(true);	
			
			//C
		});

		/*
		 * ------------- REGOLAMENTO -------------
		 */
		rulesButton.setOnAction(e -> showRulesDialog());

		/*
		 * ---------- ESCI ----------
		 */
		exitButton.setOnAction(e -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Conferma uscita");
			confirm.setHeaderText("Sei sicuro di voler uscire?");

			if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
				System.exit(0);
			}
		});

		getChildren().addAll(title, playButton, rulesButton, exitButton, infoLabel);
	}

	/*
	 * ========================== FINESTRA REGOLAMENTO ==========================
	 */
	private void showRulesDialog() {

		Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
		rulesAlert.setTitle("Regolamento - JustPoker™");
		rulesAlert.setHeaderText("Regole del gioco");

		rulesAlert.setContentText("""
				REGOLE DEL POKER JUSTPOKER™

				• Ogni giocatore riceve 5 carte
				• Il mazzo è composto da 32 carte (dal 7 all'Asso)

				FASI DEL GIOCO:
				1. INVITO: puntata iniziale obbligatoria
				2. APERTURA: si può aprire con almeno una coppia di Jack
				3. PUNTATA: fase di rilanci fino al pareggio
				4. ACCOMODO: possibilità di cambiare fino a 3 carte
				5. SHOWDOWN: confronto delle mani

				AZIONI DISPONIBILI:
				• Invito: partecipa alla mano con puntata minima
				• Apri: apri il gioco con una puntata
				• Passa: passa il turno (solo in apertura)
				• Punta: aumenta la posta in gioco
				• Cambio: sostituisci fino a 3 carte
				• Servito: mantieni le carte attuali
				• Fold: abbandona la mano

				COMBINAZIONI VINCENTI (dal più basso al più alto):
				1. Carta alta
				2. Coppia
				3. Doppia coppia
				4. Tris
				5. Scala
				6. Colore
				7. Full
				8. Poker
				9. Scala colore
				""");

		rulesAlert.showAndWait();
	}
}
