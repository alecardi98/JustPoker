package THRProject.server;

import java.io.*;
import java.net.*;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Player;

/*
 * Classe che rappresenta il thread del server che gestirà la connessione con un client 
 */
class ClientHandler implements Runnable {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private static int countId = 0; // assegazione id incrementale a partire da 0
	private final int clientId; // client al quale è connesso il clienthandler (funge anche da id turno)

	public ClientHandler(Socket clientSocket) {
		clientId = nextId();
		socket = clientSocket;
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
			this.in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendMessage(new Message(MessageType.CLIENT_ID, clientId)); // invio clientId
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

				switch (msg.getType()) {

				case PLAYER_JOIN:
					handlePlayerJoin((Player) msg.getData());
					break;

				case QUIT:
					handleQuit((int) msg.getData());
					cleanup();
					return;

				default:
					System.out.println("ERRORE! Messaggio sconosciuto.");
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Errore di comunicazione col client.");
			cleanup();
		}
	}

	/*
	 * Metodo per la gestione del messaggio di PLAYER_JOIN
	 */
	private void handlePlayerJoin(Player player) {
		Server.getServer().registerPlayer(clientId, player);
	}

	/*
	 * Metodo per la gestione del messaggio di QUIT
	 */
	private void handleQuit(int clientId) {
		Server.getServer().removePlayer(clientId);
	}

	/*
	 * Metodo per inviare messaggi al client
	 */
	public void sendMessage(Object msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo per chiudere correttamente il clientHandler
	 */
	private void cleanup() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("BHO");
		}
		System.out.println("ClientHandler rimosso.");
		Server.getServer().removeClient(this);
	}

	/*
	 * Metodo per incrementare il contatore dei client in modo atomico
	 */
	public static synchronized int nextId() {
		return countId++;
	}

	/*
	 * Getter & Setter
	 */
	public int getClientId() {
		return clientId;
	}

}
