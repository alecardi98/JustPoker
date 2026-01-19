package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.game.Game;
import THRProject.gui.SceneManager;
import THRProject.message.ControlType;
import THRProject.message.Message;

public class ServerListener implements Runnable {

	private static final Logger logger = LogManager.getLogger(ServerListener.class);
	private ObjectInputStream in;
	private Client client;
	private SceneManager sceneManager;

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

	/*
	 * Metodo che gestisce l'arrivo del CLIENT_ID
	 */
	private void handleClientId(Object data) {
		client.setClientId((int) data);
		client.sendMessage(new Message(ControlType.PLAYER_JOIN, client.getPlayer()));
	}

	/*
	 * Metodo che gestisce l'arrivo del lo START_GAME
	 */
	private void handleStartGame() {
		client.startGame();
	}

	/*
	 * Metodo che gestisce l'arrivo dell'INVALID_ACTION
	 */
	private void handleInvalidAction(String action) {
		switch (action) {
		case "apertura" -> logger.info("ERRORE! Apertura non valida.");
		case "cambio" -> logger.info("ERRORE! Cambio non valido.");
		case "puntata" -> logger.info("ERRORE! Puntata non valida.");
		case "ready" -> logger.info("ERRORE! Hai già scelto.");
		default -> logger.warn("Azione invalida sconosciuta: " + action);
		}
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
		client.checkMoment();
	}

	/*
	 * Metodo che gestisce l'arrivo del WINNER
	 */
	private void handleWinner() {
		logger.info("Hai vinto la mano!");
	}

	/*
	 * Metodo che gestisce l'arrivo del LOSER
	 */
	private void handleLoser() {
		logger.info("Hai perso la mano.");
	}

	/*
	 * Metodo che gestisce l'arrivo dell'ENDGAME
	 */
	private void handleEndGame() {
		logger.info("Bancarotta! Hai perso.");
		cleanUp();
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
