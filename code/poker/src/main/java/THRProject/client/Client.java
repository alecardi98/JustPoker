package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import THRProject.poker.Game;
import THRProject.poker.Player;

public class Client {

	ObjectOutputStream out;
	ObjectInputStream in;
	ServerListener serverListener;

	//private static final String HOST = "localhost";
	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private boolean quit; // indica quando il client ha lasciato la partita
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
			Socket socket = new Socket(HOST, PORT);
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
	 * Metodo chiamato dal serverListener che fa partire la partita del client
	 */
	public void startToPlay() {
		// TO DO

		// do {

		// }while(!player.isQuit());
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
