package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Game;

public class ServerListener implements Runnable {

	ObjectInputStream in;
	Client client;

	public ServerListener(ObjectInputStream in, Client client) {
		this.in = in;
		this.client = client;
	}

	@Override
	public void run() {
		try {
			Object obj;
			while ((obj = in.readObject()) != null) {
				Message msg = (Message) obj;
				// gestione delle risposte del Server
				switch (msg.getType()) {
				case CLIENT_ID:
					client.setClientId((int) msg.getData());
					client.sendMessage(new Message(MessageType.PLAYER_JOIN, client.getPlayer()));
					break;

				case START_GAME:
					client.setGame((Game) msg.getData());
					client.startToPlay();
					break;

				default:
					System.out.println("ERRORE! Messaggio sconosciuto.\n");
					break;
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Server Disconnesso.");
		}
	}

}
