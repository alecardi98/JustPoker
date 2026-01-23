package THRProject.gui;

import THRProject.client.Client;
import THRProject.client.ClientObserver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	private RegisterPane registerPane;
	private LoginPane loginPane;
	private MainMenuPane mainMenuPane;
	private LobbyPane lobbyPane;
	private GameTablePane gameTablePane;
	private EndPane endPane;

	public SceneManager(Stage stage, Client client) {
		this.stage = stage;
		this.client = client;

		// Configurazione finestra principale
		stage.setResizable(false);
		stage.setOnCloseRequest(e -> {
			Platform.exit();
		});
	}

	public void showLoginScene() {
		loginPane = new LoginPane(this);
		Scene scene = new Scene(loginPane, 400, 350);
		stage.setScene(scene);
		stage.setTitle("Login - JustPoker™");
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public void showRegisterScene() {
		registerPane = new RegisterPane(this);
		Scene scene = new Scene(registerPane, 400, 350);
		stage.setScene(scene);
		stage.setTitle("Registrazione - JustPoker™");
	}

	public void showMainMenu() {
		mainMenuPane = new MainMenuPane(this);
		Scene scene = new Scene(mainMenuPane, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Menù principale - JustPoker™");
	}

	public void showLobby() {
		lobbyPane = new LobbyPane();
		Scene scene = new Scene(lobbyPane, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Lobby - JustPoker™");
	}

	public void showGameTable() {
		// Creazione GameTablePane con parametri corretti
		gameTablePane = new GameTablePane(this);
		stage.setScene(new Scene(gameTablePane, 1000, 700));
		stage.setTitle("Tavolo da gioco - JustPoker™");
	}

	public void showEndPane() {
		endPane = new EndPane(this);
		stage.setScene(new Scene(endPane, 600, 400));
		stage.setTitle("Tavolo da gioco - JustPoker™");
	}

	@Override
	public void onStart() {
		javafx.application.Platform.runLater(() -> {
			showGameTable();
		});
	}

	@Override
	public void onLoginResult(boolean success) {
		javafx.application.Platform.runLater(() -> {
			if (success) {
				showMainMenu();
			} else {
				loginPane.getUsernameField().setDisable(false);
				loginPane.getPasswordField().setDisable(false);
				loginPane.getLoginButton().setDisable(false);
				loginPane.getRegisterButton().setDisable(false);
			}
		});
	}

	@Override
	public void onGameViewUpdate() {
		javafx.application.Platform.runLater(() -> {
			gameTablePane.refresh();
		});
	}

	@Override
	public void onEndGame() {
		javafx.application.Platform.runLater(() -> {
			showEndPane();
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

}
