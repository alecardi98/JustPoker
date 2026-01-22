package THRProject.gui;

import THRProject.client.Client;
import THRProject.game.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gestore delle scene per l'applicazione GUI
 * 
 * MODIFICHE: - Aggiunto game loop per aggiornamenti automatici - Corretto
 * passaggio parametri a GameTablePane - Aggiunti metodi per gestione finestra
 * di gioco
 */
public class SceneManager {

	private Stage stage;
	private Client client;

	private GameTablePane gameTablePane;
	
	private Thread gameWatcher;
	private volatile boolean running;

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
		startGameWatcher();
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

			startGameWatcher();
		});
	}

	private void startGameWatcher() {
	    running = true;
	    
	    gameWatcher = new Thread(() -> {
	    	Game lastGame = null;
            Game currentGame = null;
            boolean start = false;

	        while (running) {
	        	start = client.isStart();
	        	lastGame = currentGame;
	        	currentGame = client.getGameView();
	            
	            if (start && lastGame == null) {
	                Platform.runLater(() -> {
	                    if (gameTablePane == null) 
	                        showGameTable();
	                });
	            }else {
	            	if (currentGame != lastGame) {
	            		Platform.runLater(() -> {
	            			gameTablePane.refresh();
		                });
	            	}
	            }
	            try {
	                Thread.sleep(1000); // polling ogni 1000ms
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                break;
	            }
	        }
	    });

	    gameWatcher.setDaemon(true);
	    gameWatcher.start();
	}
	
	public void stopGameWatcher() {
	    running = false;
	    if (gameWatcher != null) {
	        gameWatcher.interrupt();
	        gameWatcher = null;
	    }
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
