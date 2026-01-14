package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Card;
import THRProject.poker.Game;
import THRProject.poker.Player;
import THRProject.server.Server;

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

	/*
	 * Metodo per avviare il client
	 */
	public void startClient() {
		player = dbConnection();
		serverConnection();
		// dopo la connessione sarà attivo solo il thread ServerListener, poichè Client
		// è attivo ma ha finito
	}

	/*
	 * Metodo per avviare la partita del client
	 */
	public void startGame() {
		// sarà il main della GUI
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
	public void serverDisconnection() {
		try {
			in.close();
			out.close();
			socket.close();
			serverListener = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Disconnessione riuscita.");
	}

	/*
	 * Metodo che serve per inviare l'invito al server
	 */
	private void invioInvito() {
		Message msg = gameView.getPlayers().get(clientId).invito();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare la punatata al server
	 */
	private void invioPuntata(int puntata) {
		Message msg = gameView.getPlayers().get(clientId).punta(puntata);
		sendMessage(msg);
	}
	
	/*
	 * Metodo che serve per inviare l'invito al server
	 */
	private void invioApertura(int puntata) {
		Message msg = gameView.getPlayers().get(clientId).apri(puntata);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il passa al server
	 */
	private void invioPassa() {
		Message msg = gameView.getPlayers().get(clientId).passa();
		sendMessage(msg);
	}
	
	/*
	 * Metodo che serve per inviare il lascia al server
	 */
	private void invioLascia() {
		Message msg = gameView.getPlayers().get(clientId).lascia();
		sendMessage(msg);
	}
	
	/*
	 * Metodo che serve per inviare il vedi al server
	 */
	private void invioVedi() {
		Message msg = gameView.getPlayers().get(clientId).vedi();
		sendMessage(msg);
	}
	
	/*
	 * Metodo che serve per inviare il cambio al server
	 */
	private void invioCambio(Card[] cards) {
		if (cards.length > 0 && cards.length <= 5) {
			Message msg = gameView.getPlayers().get(clientId).cambio(cards);
			sendMessage(msg);
		} else
			System.out.println("ERRORE! Cambio non valido.");
	}

	/*
	 * Metodo che serve per inviare il servito al server
	 */
	private void invioServito() {
		Message msg = gameView.getPlayers().get(clientId).servito();
		sendMessage(msg);
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
	public void ready() {
		sendMessage(new Message(MessageType.READY, clientId));
	}

	/*
	 * Metodo che permette di uscire dalla partita e disconnettersi dal server
	 */
	public void quit() {
		sendMessage(new Message(MessageType.QUIT, clientId));
		serverDisconnection();
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
