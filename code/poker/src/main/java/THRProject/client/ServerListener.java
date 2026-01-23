package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.game.Game;
import THRProject.message.ControlType;
import THRProject.message.Message;
import javafx.scene.control.Alert;

/**
 * Thread per ascoltare i messaggi dal server
 * 
 * MODIFICHE: - Aggiunto riferimento a SceneManager per aggiornamenti GUI -
 * Migliorata gestione aggiornamenti del game state - Aggiunto supporto per
 * notifiche GUI
 */
public class ServerListener implements Runnable {

	private static final Logger logger = LogManager.getLogger(ServerListener.class);
	private ObjectInputStream in;
	private Client client;

	public ServerListener(ObjectInputStream in, Client client) {
		this.in = in;
		this.client = client;
	}

	@Override
	public void run() {
		try {
			Object obj;
			while (true) {
				obj = in.readObject();
				Message msg = (Message) obj;

				if (msg.getType() instanceof ControlType control) {
					switch (control) {
					case CLIENT_ID -> handleClientId((int) msg.getData());
					case START_GAME -> handleStartGame();
					case LOGIN -> handleLogin((String) msg.getData());
					case INVALID_ACTION -> handleAction((String) msg.getData());
					case VALID_ACTION -> handleAction((String) msg.getData());
					case UPDATE -> handleUpdate(msg.getData());
					case WINNER -> handleWinner();
					case LOSER -> handleLoser();
					case ENDGAME -> handleEndGame((String) msg.getData());
					default -> logger.error("ERRORE! Messaggio sconosciuto.");
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("ERRORE! Comunicazione con il Server persa.");
			cleanUp();
		}
	}

	/*
	 * Metodo che gestisce l'arrivo del CLIENT_ID
	 */
	private void handleClientId(int clientId) {
		client.setClientId(clientId);
		logger.info("Ricevuto ID client: " + clientId);
	}

	/*
	 * Metodo che gestisce l'arrivo dello START_GAME
	 */
	private void handleStartGame() {
		logger.info("Ricevuto segnale START_GAME");
		client.notifyStartGame();
	}

	/*
	 * Metodo che gestisce l'arrivo dell'INVALID_ACTION e del VALID_ACTION
	 */
	private void handleAction(String action) {
		logger.info(action);
		// Mostra notifica GUI
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Attenzione");
			alert.setHeaderText(null);
			alert.setContentText(action);
			alert.showAndWait();
		});
	}

	/*
	 * Metodo che gestisce l'arrivo del LOGIN
	 */
	private void handleLogin(String result) {
		logger.info(result);
		if (result.equals("Login effettuato.")) {
			client.notifyLoginResult(true);
		} else {
			client.notifyLoginResult(false);
		}

		// Mostra notifica GUI
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Autenticazione");
			alert.setHeaderText(null);
			alert.setContentText(result);
			alert.showAndWait();
		});
	}

	/*
	 * Metodo che gestisce l'arrivo dell'UPDATE
	 */
	private void handleUpdate(Object data) {
		client.setGameView((Game) data);
		client.notifyGameViewUpdate();
	}

	/*
	 * Metodo che gestisce l'arrivo del WINNER
	 */
	private void handleWinner() {
		logger.info("Hai vinto la mano!");

		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Vittoria!");
			alert.setHeaderText(null);
			alert.setContentText("Hai vinto la mano!");
			alert.showAndWait();
		});

	}

	/*
	 * Metodo che gestisce l'arrivo del LOSER
	 */
	private void handleLoser() {
		logger.info("Hai perso la mano.");

		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Hai perso");
			alert.setHeaderText(null);
			alert.setContentText("Hai perso la mano.");
			alert.showAndWait();
		});

	}

	/*
	 * Metodo che gestisce l'arrivo dell'ENDGAME
	 */
	private void handleEndGame(String endgame) {
		logger.info(endgame);
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Game Over");
			alert.setHeaderText(null);
			alert.setContentText(endgame);
			alert.showAndWait();
		});
		client.notifyEndGame();
	}

	/*
	 * Metodo che chiude correttamente il ServerListener
	 */
	private void cleanUp() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.serverDisconnection();
	}

}
