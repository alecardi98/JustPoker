package THRProject.server;

import java.io.*;
import java.net.*;
import THRProject.player.Player;

/*
 * Classe che rappresenta il thread del server che gestirà la connessione con un client 
 */
class ClientHandler implements Runnable {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Player player;

	public ClientHandler(Socket socket) {
		this.socket = socket;

		// creazione oggetti per i flussi di oggetti tra client/server
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Metodo per ricevere oggetti dal client
	 */
	@Override
	public void run() {
		try {
			Object obj;
			while ((obj = in.readObject()) != null) {
				// clienthandler leggerà le richieste del client e deciderà se eseguirle o no
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Client " + player + " disconnesso.");
		}
	}
}
