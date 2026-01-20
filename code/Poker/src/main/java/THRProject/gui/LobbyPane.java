package THRProject.gui;

import THRProject.client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Pannello della lobby di gioco
 * 
 * MODIFICHE: - Integrata con metodi Client per partecipazione - Aggiunta
 * gestione stati di gioco - Aggiunto feedback visivo per azioni utente
 */
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

		statusLabel = new Label("Connessione stabilita. In attesa di altri giocatori...");
		statusLabel.setStyle("-fx-text-fill: blue;");

		getChildren().addAll(title, statusLabel);
	}

	/*
	 * ========================= AGGIORNAMENTO DA SERVER =========================
	 */
	public void updateFromServer(String msg) {
		statusLabel.setText(msg);
	}
}
