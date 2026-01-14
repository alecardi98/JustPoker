package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Game;

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
				switch (msg.getType()) {
				case CLIENT_ID:
					client.setClientId((int) msg.getData());
					client.sendMessage(new Message(MessageType.PLAYER_JOIN, client.getPlayer()));
					break;

				case START_GAME:
					client.setGame((Game) msg.getData());
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
					break;

				case VALID_ACTION:
					String validAction = (String) msg.getData();
					if (validAction.equals("invito")) {
						System.out.println("Invito registrato.");
					}
					if (validAction.equals("apertura")) {
						System.out.println("Apertura registrata.");
					}
					if (validAction.equals("cambio")) {
						System.out.println("Cambio registrato.");
					}
					if (validAction.equals("servito")) {
						System.out.println("Servito registrato.");
					}
					break;

				case UPDATE_GAME:
					client.setGame((Game) msg.getData());
					break;

				case YOUR_TURN:
					break;

				default:
					System.out.println("ERRORE! Messaggio sconosciuto.\n");
					break;
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("ERRORE! Comunicazione con il Server persa.");
			cleanup();
			client.serverDisconnection();
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
	}

}
