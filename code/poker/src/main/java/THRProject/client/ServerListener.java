package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.game.Game;
import THRProject.message.ControlType;
import THRProject.message.Message;

public class ServerListener implements Runnable {

	private static final Logger logger = LogManager.getLogger("listener");
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

				// gestione delle risposte del Server
				if (msg.getType() instanceof ControlType control) {
					switch (control) {
					case CLIENT_ID:
						client.setClientId((int) msg.getData());
						client.sendMessage(new Message(ControlType.PLAYER_JOIN, client.getPlayer()));
						break;

					case START_GAME:
						client.startGame();
						break;

					case INVALID_ACTION:
						String invalidAction = (String) msg.getData();
						if (invalidAction.equals("invito")) {
							logger.info("ERRORE! Invito non valido.");
						}
						if (invalidAction.equals("apertura")) {
							logger.info("ERRORE! Apertura non valida.");
						}
						if (invalidAction.equals("cambio")) {
							logger.info("ERRORE! Cambio non valido.");
						}
						if (invalidAction.equals("puntata")) {
							logger.info("ERRORE! Puntata non valida.");
						}
						if (invalidAction.equals("ready")) {
							logger.info("ERRORE! Hai già scelto.");
						}
						break;

					case VALID_ACTION:
						String validAction = (String) msg.getData();
						if (validAction.equals("invito")) {
							logger.info("Invito registrato.");
						}
						if (validAction.equals("apertura")) {
							logger.info("Apertura registrata.");
						}
						if (validAction.equals("passa")) {
							logger.info("Passa registrato.");
						}
						if (validAction.equals("cambio")) {
							logger.info("Cambio registrato.");
						}
						if (validAction.equals("servito")) {
							logger.info("Servito registrato.");
						}
						if (validAction.equals("puntata")) {
							logger.info("Puntata registrata.");
						}
						if (validAction.equals("fold")) {
							logger.info("Fold registrato.");
						}
						if (validAction.equals("ready")) {
							logger.info("Ready registrato.");
						}
						break;

					case UPDATE:
						client.setGame((Game) msg.getData());
						client.checkMoment();
						break;

					case WINNER:
						logger.info("Hai vinto la mano!");
						break;

					case LOSER:
						logger.info("Hai perso la mano.");
						break;

					case ENDGAME:
						logger.info("Bancarotta! Hai perso.");
						client.serverDisconnection();
						break;
						
					default:
						logger.error("ERRORE! Messaggio sconosciuto.");
						break;
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("ERRORE! Comunicazione con il Server persa.");
			cleanup();
		}
	}

	/*
	 * Permette di chiudere correttamente il ServerListener
	 */
	private void cleanup() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.serverDisconnection();
	}

}
