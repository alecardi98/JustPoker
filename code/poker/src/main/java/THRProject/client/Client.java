package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Game;
import THRProject.poker.Player;

public class Client {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ServerListener serverListener;
	private Socket socket;

	private static final String HOST = "localhost";
//	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private boolean quit; // indica quando il client ha lasciato la partita disconnettendosi dal server
	private Game gameView; // variabile che contiene solo i dati personali del game
	private int clientId;
	private Player player;

	public Client() {
		quit = false;
	}

	public void startClient() {
		player = dbConnection();
		serverConnection();
	}

	/*
	 * Metodo che permette di creare un player valido da DB
	 */
	private Player dbConnection() {
		// TO DO
		return new Player("userName", "password"); // creazione Player con dati corretti
	}

	/*
	 * Metodo con il quale il client si connette al server
	 */
	private void serverConnection() {
		try {
			socket = new Socket(HOST, PORT);
			System.out.println("Client connesso al server");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("ERRORE! Impossibile connettersi alla partita. Disconnessione.\n");
			System.exit(1);
		}
		serverListener = new ServerListener(in, this);
		new Thread(serverListener).start();
	}

	/*
	 * Metodo con il quale il client si disconnette dal server
	 */
	private void serverDisconnection() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Disconnessione riuscita.");
	}

	/*
	 * Metodo chiamato dal serverListener che fa partire la partita del client
	 */
	public void startToPlay() {

		do {
			
			
			
		} while (!isQuit());
		serverDisconnection();

	}

	/*
	 * Metodo per inviare messaggi al server
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
	 * Metodo che permette di uscire dalla partita e disconnettersi dal server
	 */
	public void quit() {
		sendMessage(new Message(MessageType.QUIT, clientId));
		quit = true;
	}

	/*
	 * Getter & Setter
	 */
	public void setGame(Game gameView) {
		this.gameView = gameView;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

}
