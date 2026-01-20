package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.game.Game;
import THRProject.gui.SceneManager;
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
					case CLIENT_ID -> handleClientId(msg.getData());
					case LOGIN -> handleLogin();
					case REGISTER -> handleRegister();
					case START_GAME -> handleStartGame();
					case INVALID_ACTION -> handleInvalidAction((String) msg.getData());
					case VALID_ACTION -> handleValidAction((String) msg.getData());
					case UPDATE -> handleUpdate(msg.getData());
					case WINNER -> handleWinner();
					case LOSER -> handleLoser();
					case ENDGAME -> handleEndGame();
					default -> logger.error("ERRORE! Messaggio sconosciuto.");
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("ERRORE! Comunicazione con il Server persa.");
			cleanUp();
		}
	}

	private void handleRegister() {
		logger.info("Registrazione completata.");
	}

	private void handleLogin() {
		logger.info("Login effettuato.");
	}

	/*
	 * Metodo che gestisce l'arrivo del CLIENT_ID
	 */
	private void handleClientId(Object data) {
		client.setClientId((int) data);
		client.sendMessage(new Message(ControlType.PLAYER_JOIN, client.getPlayer()));
		logger.info("Ricevuto ID client: " + data);
	}

	/*
	 * Metodo che gestisce l'arrivo dello START_GAME
	 */
	private void handleStartGame() {
		logger.info("Ricevuto segnale START_GAME");
		client.startGame();
	}

	/*
	 * Metodo che gestisce l'arrivo dell'INVALID_ACTION
	 */
	private void handleInvalidAction(String action) {
		switch (action) {
		case "login" -> logger.info("ERRORE! Login non effettuato.");
		case "apertura" -> logger.info("ERRORE! Apertura non valida.");
		case "cambio" -> logger.info("ERRORE! Cambio non valido.");
		case "puntata" -> logger.info("ERRORE! Puntata non valida.");
		case "ready" -> logger.info("ERRORE! Hai già scelto.");
		default -> logger.warn("Azione invalida sconosciuta: " + action);
		}

		// Mostra notifica GUI
		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Azione non valida");
			alert.setHeaderText(null);
			alert.setContentText("Azione " + action + " non valida in questo momento.");
			alert.showAndWait();
		});

	}

	/*
	 * Metodo che gestisce l'arrivo del VALID_ACTION
	 */
	private void handleValidAction(String action) {
		switch (action) {
		case "invito" -> logger.info("Invito registrato.");
		case "apertura" -> logger.info("Apertura registrata.");
		case "passa" -> logger.info("Passa registrato.");
		case "cambio" -> logger.info("Cambio registrato.");
		case "servito" -> logger.info("Servito registrato.");
		case "puntata" -> logger.info("Puntata registrata.");
		case "fold" -> logger.info("Fold registrato.");
		case "ready" -> logger.info("Ready registrato.");
		default -> logger.warn("Azione valida sconosciuta: " + action);
		}
	}

	/*
	 * Metodo che gestisce l'arrivo dell'UPDATE
	 */
	private void handleUpdate(Object data) {
		client.setGame((Game) data);
		client.getSceneManager().refreshGameTable(); // Aggiorna la GUI
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
	private void handleEndGame() {
		logger.info("Bancarotta! Hai perso.");

		javafx.application.Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Game Over");
			alert.setHeaderText(null);
			alert.setContentText("Bancarotta! Hai finito le fiches.");
			alert.showAndWait();

			// Torna al menu principale
			client.getSceneManager().showMainMenu();
		});

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
