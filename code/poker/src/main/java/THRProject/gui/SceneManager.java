package THRProject.gui;

import THRProject.client.Client;
import THRProject.client.ClientObserver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Gestore delle scene per l'applicazione GUI
 * 
 * MODIFICHE: - Aggiunto game loop per aggiornamenti automatici - Corretto
 * passaggio parametri a GameTablePane - Aggiunti metodi per gestione finestra
 * di gioco
 */
public class SceneManager implements ClientObserver {

	private Stage stage;
	private Client client;

	private GameTablePane gameTablePane;

	public SceneManager(Stage stage, Client client) {
		this.stage = stage;
		this.client = client;

		// Configurazione finestra principale
		stage.setResizable(false);
		stage.setOnCloseRequest(e -> {
		Platform.exit();
		});
	}

	@Override
	public void onStart() {
		showGameTable();
	}

	public void showLoginScene() {
		LoginPane loginPane = new LoginPane(this);
		Scene scene = new Scene(loginPane, 400, 350);
		stage.setScene(scene);
		stage.setTitle("Login - JustPoker™");
		stage.show();
	}

	public void showRegisterScene() {
		RegisterPane registerPane = new RegisterPane(this);
		Scene scene = new Scene(registerPane, 400, 350);
		stage.setScene(scene);
		stage.setTitle("Registrazione - JustPoker™");
	}

	public void showMainMenu() {
		MainMenuPane menuPane = new MainMenuPane(this);
		Scene scene = new Scene(menuPane, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Menù principale - JustPoker™");
	}

	public void showLobby() {
		LobbyPane lobbyPane = new LobbyPane();
		Scene scene = new Scene(lobbyPane, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Lobby - JustPoker™");
	}

	public void showGameTable() {
		Platform.runLater(() -> {
			// Creazione GameTablePane con parametri corretti
			gameTablePane = new GameTablePane(this);
			stage.setScene(new Scene(gameTablePane, 1000, 700));
			stage.setTitle("Tavolo da gioco - JustPoker™");
		});
	}

	/*
	 * Getter & Setter
	 */
	public Stage getStage() {
		return stage;
	}

	public Client getClient() {
		return client;
	}
	
	@Override
	public void onLoginResult(boolean success) {
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

	@Override
	public void onGameViewUpdate() {
		// TODO Auto-generated method stub
		
	}
}
