package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Game;
import THRProject.poker.Player;

public class Client {

	ObjectOutputStream out;
	ObjectInputStream in;

	private static final String HOST = "localhost";
//	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private Game game;
	private Player player;

	public Client() {
	}

	public void startClient() {
		dbConnection();
		serverConnection();
		sendPlayerJoin(player);
		
		while(true);
	}

	/*
	 * Metodo che permette di creare un player valido da DB
	 */
	private void dbConnection() {
		// TO DO
		this.player = new Player("userName", "password"); // creazione Player con dati corretti
	}

	/*
	 * Metodo con il quale il client si connette al server
	 */
	private void serverConnection() {
		try {
			Socket socket = new Socket(HOST, PORT);
			System.out.println(player + " : connesso al server");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("ERRORE! Impossibile connettersi alla partita. Disconnessione.\n");
			System.exit(1);
		}
		
	}

	/*
	 * Metodo per inviare il Player al Server
	 */
	public void sendPlayerJoin(Player player) {
		Message msg = new Message(MessageType.PLAYER_JOIN, player);
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	/*
//	 * Metodo chiamato dal ??? che fa partire la partita del client
//	 * */
//	public void startToPlay() {
//	}

	/*
	 * Getter & Setter
	 */
	public void setGame(Game game) {
		this.game = game;
	}

}
