package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import THRProject.game.Game;
import THRProject.message.ControlType;
import THRProject.message.Message;

public class ServerListener implements Runnable {

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
							System.out.println("ERRORE! Invito non valido.");
						}
						if (invalidAction.equals("apertura")) {
							System.out.println("ERRORE! Apertura non valida.");
						}
						if (invalidAction.equals("cambio")) {
							System.out.println("ERRORE! Cambio non valido.");
						}
						if (invalidAction.equals("puntata")) {
							System.out.println("ERRORE! Puntata non valida.");
						}
						if (invalidAction.equals("ready")) {
							System.out.println("ERRORE! Hai già scelto.");
						}
						break;

					case VALID_ACTION:
						String validAction = (String) msg.getData();
						if (validAction.equals("invito")) {
							System.out.println("Invito registrato.");
						}
						if (validAction.equals("apertura")) {
							System.out.println("Apertura registrata.");
						}
						if (validAction.equals("passa")) {
							System.out.println("Passa registrato.");
						}
						if (validAction.equals("cambio")) {
							System.out.println("Cambio registrato.");
						}
						if (validAction.equals("servito")) {
							System.out.println("Servito registrato.");
						}
						if (validAction.equals("puntata")) {
							System.out.println("Puntata registrata.");
						}
						if (validAction.equals("fold")) {
							System.out.println("Fold registrato.");
						}
						if (validAction.equals("ready")) {
							System.out.println("Ready registrato.");
						}
						break;

					case UPDATE:
						client.setGame((Game) msg.getData());
						client.checkMoment();
						break;

					case WINNER:
						System.out.println("Hai vinto la mano!");
						break;

					case LOSER:
						System.out.println("Hai perso la mano.");
						break;

					default:
						System.out.println("ERRORE! Messaggio sconosciuto.\n");
						break;
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("ERRORE! Comunicazione con il Server persa.");
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
