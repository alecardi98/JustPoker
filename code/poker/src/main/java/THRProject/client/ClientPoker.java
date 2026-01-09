package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import THRProject.poker.Game;
import THRProject.poker.Player;

public class ClientPoker {

	private static final String HOST = "localhost";
//	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private Game game;
	private Player player;

	public ClientPoker() {
	}

	public void startClient() {
//		Player player = dbConnection();
		try {
			Socket socket = new Socket(HOST, PORT);
			System.out.println("Connesso al server");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

//		this.player = player;
//		out.writeObject(player); // invia subito il player al server
//		out.flush();
//		System.out.println("Player inviato al server: " + player);
	}

	/*
	 * Metodo che permette di creare un player valido da DB
	 */
	private Player dbConnection() {
		// controllo dati
		return new Player("userName", "password"); // creazione Player con dati corretti
	}

	/*
	 * Getter & Setter
	 */
	public void setGame(Game game) {
		this.game = game;
	}

}
