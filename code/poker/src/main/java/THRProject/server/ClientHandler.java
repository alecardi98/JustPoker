package THRProject.server;

import java.io.*;
import java.net.*;

import THRProject.message.Message;
import THRProject.poker.Player;

/*
 * Classe che rappresenta il thread del server che gestirà la connessione con un client 
 */
class ClientHandler implements Runnable {

	private Socket clientSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static int countId = 1; // assegazione id incrementale a partire da 1 a 
	private final int clientId; // client al quale è connesso il clienthandler (funge anche da id turno)

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		clientId = nextId();
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
			this.in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo per ricevere oggetti dai client: ogni ClientHandler sarà associato ad
	 * un client, e si occuperà di gestire il flusso di messaggi
	 */
	@Override
	public void run() {
		try {
			Object obj;
			while ((obj = in.readObject()) != null) {
				Message msg = (Message) obj;

				// gestione delle richieste del Client
				switch (msg.getType()) {
				case PLAYER_JOIN:
					handlePlayerJoin((Player) msg.getData());
					break;

				default:
					System.out.println("ERRORE! Messaggio sconosciuto.\n");
					break;
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Client disconnesso.");
		}
	}

	/*
	 * Metodo per la gestione del messaggio di PLAYER_JOIN
	 */
	private void handlePlayerJoin(Player player) {
		Server.getServerPoker().registerPlayer(clientId, player);
	}

	/*
	 * Metodo per inviare oggetti al client
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
