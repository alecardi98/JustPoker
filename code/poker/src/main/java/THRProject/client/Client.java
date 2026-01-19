package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.game.Game;
import THRProject.message.Message;
import THRProject.message.Communicator;
import THRProject.message.ControlType;
import THRProject.player.Player;

public class Client implements Communicator {

	private static final Logger logger = LogManager.getLogger(Client.class);
	private ObjectOutputStream out;
	private ServerListener serverListener;
	private Socket socket;

//	private static final String HOST = "localhost";
	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private Game gameView; // variabile che contiene solo i dati personali del game
	private int clientId;
	private Player player;

	public Client() {
		// il Client viene inizializzato solo dopo la connessione al Server
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
	 * Metodo login
	 */
	public void tryLogin(String username, String password) {
		// Leo metti e implementa questo metodo dove vuoi
	}

	/*
	 * Metodo per avviare la partita del client
	 */
	public void startGame() {
		// TO DO il client ha tutto ciò che gli serve per stampare il campo
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
		ObjectInputStream in;
		try {
			socket = new Socket(HOST, PORT);
			logger.info("Client connesso al server");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			serverListener = new ServerListener(in, this);
			new Thread(serverListener).start();
		} catch (IOException e) {
			logger.error("ERRORE! Impossibile connettersi alla partita. Disconnessione.");
			System.exit(1);
		}
	}

	/*
	 * Metodo con il quale il client si disconnette dal server
	 */
	public void serverDisconnection() {
		try {
			out.close();
			socket.close();
			serverListener = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Disconnessione riuscita.");
	}

	public void checkMoment() {
		logger.info("Fase di " + gameView.getPhase() + " : tocca a Client " + gameView.getCurrentTurn());
	}

	/*
	 * Metodo che serve per inviare l'invito al server
	 */
	public void invioInvito() {
		Message msg = getGameView().getPlayers().get(clientId).invito();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare la puntata al server
	 */
	public void invioPuntata(int puntata) {
		Message msg = getGameView().getPlayers().get(clientId).punta(puntata);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare l'apertura al server
	 */
	public void invioApertura(int puntata) {
		Message msg = getGameView().getPlayers().get(clientId).apri(puntata);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il passa al server
	 */
	public void invioPassa() {
		Message msg = getGameView().getPlayers().get(clientId).passa();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il lascia al server
	 */
	private void invioLascia() {
		Message msg = getGameView().getPlayers().get(clientId).lascia();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il cambio al server
	 */
	public void invioCambio() {
		Message msg = getGameView().getPlayers().get(clientId).cambio();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il servito al server
	 */
	public void invioServito() {
		Message msg = getGameView().getPlayers().get(clientId).servito();
		sendMessage(msg);
	}

	/*
	 * Metodo che permette al client di uscire dalla partita e disconnettersi dal
	 * server
	 */
	public void ready() {
		Message msg = new Message(ControlType.READY, clientId);
		sendMessage(msg);
	}

	/*
	 * Metodo che permette al client di uscire dalla partita e disconnettersi dal
	 * server
	 */
	public void quit() {
		Message msg = new Message(ControlType.QUIT, clientId);
		sendMessage(msg);
		serverDisconnection();
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

	/*
	 * Getter & Setter
	 */
	public Game getGame() {
	    return getGameView();
	}

	public void setGame(Game gameView) {
		this.setGameView(gameView);
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public int getClientId() {
	    return this.clientId;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGameView() {
		return gameView;
	}

	public void setGameView(Game gameView) {
		this.gameView = gameView;
	}
}
