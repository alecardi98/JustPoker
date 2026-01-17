package THRProject.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import THRProject.card.Card;
import THRProject.message.ActionType;
import THRProject.message.Communicator;
import THRProject.message.ControlType;
import THRProject.message.Message;
import THRProject.player.Player;

/*
 * Classe che rappresenta il thread del server che gestirà la connessione con un client 
 */
class ClientHandler implements Runnable, Communicator {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private final int clientId; // client al quale è connesso il clienthandler (funge anche da id turno)

	public ClientHandler(Socket clientSocket, int clientId) {
		this.clientId = clientId;
		socket = clientSocket;
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
			this.in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendMessage(new Message(ControlType.CLIENT_ID, clientId)); // invio clientId
	}

	/*
	 * Metodo per ricevere oggetti dai client: ogni ClientHandler sarà associato ad
	 * un client, e si occuperà di gestire il flusso di messaggi
	 */
	@Override
	public void run() {
		try {
			Object obj;
			while (true) {
				obj = in.readObject();
				Message msg = (Message) obj;

				if (msg.getType() instanceof ActionType action) {
					switch (action) {
					case INVITO:
						Server.getServer().checkInvito(clientId);
						break;

					case APRI:
						Server.getServer().checkApertura(clientId, (Integer) msg.getData());
						break;

					case PASSA:
						Server.getServer().checkPassa(clientId);
						break;

					case CAMBIO:
						Server.getServer().checkCambio(clientId, (ArrayList<Card>) msg.getData());
						break;

					case SERVITO:
						Server.getServer().checkServito(clientId);
						break;

					case PUNTA:
						Server.getServer().checkPuntata(clientId, (Integer) msg.getData());
						break;

					case FOLD:
						Server.getServer().getGame().foldPlayer(clientId);
						Server.getServer().broadcastSafeGameView();
						sendMessage(new Message(ControlType.VALID_ACTION, "fold"));
						break;

					default:
						System.out.println("ERRORE! Messaggio sconosciuto.");
						break;
					}
				}
				if (msg.getType() instanceof ControlType control) {
					switch (control) {
					case PLAYER_JOIN:
						Server.getServer().registerPlayer(clientId, (Player) msg.getData());
						break;

					case READY:
						Server.getServer().countReady(clientId);
						break;

					case QUIT:
						cleanup(clientId);
						Server.getServer().checkStart();
						return;

					default:
						System.out.println("ERRORE! Messaggio sconosciuto.");
						break;
					}
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("ERRORE! Comunicazione con il Client " + clientId + " persa.");
			cleanup(clientId);
		}
	}

	/*
	 * Metodo per chiudere correttamente la connessione con il client - rimuove il
	 * player - chiude il clientHandler
	 */
	private void cleanup(int clientId) {
		Server.getServer().getGame().removePlayer(clientId);
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Server.getServer().removeClient(clientId);
	}

	/*
	 * Getter & Setter
	 */
	public int getClientId() {
		return clientId;
	}

	@Override
	public void sendMessage(Object msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
