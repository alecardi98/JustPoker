package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import THRProject.poker.Game;
import THRProject.poker.Player;
import THRProject.server.ServerPoker;

public class ClientPoker {

	ObjectOutputStream out;
	ObjectInputStream in;

	private static final String HOST = "localhost";
//	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private Game game;
	private Player player;

	public ClientPoker() {
	}

	public void startClient() {
		Player player = dbConnection();
		this.player = player;
		try {
			Socket socket = new Socket(HOST, PORT);
			System.out.println(player + " : connesso al server");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("ERRORE! Impossibile connettersi alla partita.\n");
		}

		while (!player.isQuit()) {
			// TO DO
		}
	}

	/*
	 * Metodo che permette di creare un player valido da DB
	 */
	private Player dbConnection() {
		// TO DO
		return new Player("userName", "password"); // creazione Player con dati corretti
	}

	/*
	 * Getter & Setter
	 */
	public void setGame(Game game) {
		this.game = game;
	}

}
