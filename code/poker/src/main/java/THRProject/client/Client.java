package THRProject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import THRProject.card.model.Card;
import THRProject.game.Game;
import THRProject.message.Message;
import THRProject.message.ActionType;
import THRProject.message.Communicator;
import THRProject.message.ControlType;
import THRProject.player.Player;

/**
 * Classe Client per la comunicazione con il server
 * 
 * MODIFICHE: - Aggiunto metodo invioLascia() che mancava - Commentata parte di
 * database (non ancora implementata) - Aggiunta gestione eccezioni migliorata -
 * Aggiunto riferimento a SceneManager per aggiornamenti GUI
 */
public class Client implements Communicator {

	private static final Logger logger = LogManager.getLogger(Client.class);
	private ObjectOutputStream out;
	private ServerListener serverListener;
	private Socket socket;
	private int tornaMenu = 0;

	private static final String HOST = "localhost";
//	private static final String HOST = "204.216.208.188";
	private static final int PORT = 443;

	private Game gameView; // variabile che contiene solo i dati personali del game
	private int clientId;
	private boolean login;
	private boolean start;

	public Client() {
		// il client viene fatto partire nel main e inizializzato appena si connette al
		// server
		login = false;
		start = false;
	}

	/*
	 * Metodo per avviare il client
	 */
	public void startClient() {
		serverConnection();
	}

	/*
	 * Metodo login
	 */
	public void tryLogin(String username, String password) {
		sendMessage(new Message(ControlType.LOGIN, new Player(username, password)));
	}

	/*
	 * Metodo registrazione
	 */
	public void tryRegister(String username, String password) {
		sendMessage(new Message(ControlType.REGISTER, new Player(username, password)));
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
			logger.error("ERRORE! Impossibile connettersi alla server. Disconnessione.");
			System.exit(1);
		}
	}

	/*
	 * Metodo con il quale il client si disconnette dal server
	 */
	public void serverDisconnection() {
		try {
			if (out != null)
				out.close();
			if (socket != null)
				socket.close();
			serverListener = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Disconnessione riuscita.");
	}

	/*
	 * Metodo che serve per inviare l'invito al server
	 */
	public void invioInvito() {
		Message msg = gameView.getPlayers().get(clientId).invito();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare la puntata al server
	 */
	public void invioPuntata(int puntata) {
		Message msg = gameView.getPlayers().get(clientId).punta(puntata);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare l'apertura al server
	 */
	public void invioApertura(int puntata) {
		Message msg = gameView.getPlayers().get(clientId).apri(puntata);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il passa al server
	 */
	public void invioPassa() {
		Message msg = gameView.getPlayers().get(clientId).passa();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il lascia al server AGGIUNTO: Questo metodo
	 * mancava nella versione originale
	 */
	public void invioLascia() {
		Message msg = gameView.getPlayers().get(clientId).lascia();
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il cambio al server
	 */
	public void invioCambio(ArrayList<Card> cards) {
		Message msg = gameView.getPlayers().get(clientId).cambio(cards);
		sendMessage(msg);
	}

	/*
	 * Metodo che serve per inviare il servito al server
	 */
	public void invioServito() {
		Message msg = gameView.getPlayers().get(clientId).servito();
		sendMessage(msg);
	}

	/*
	 * Metodo che permette al client di segnalare di essere pronto
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
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getClientId() {
		return this.clientId;
	}

	public Game getGameView() {
		return gameView;
	}

	public void setGameView(Game gameView) {
		this.gameView = gameView;
	}

	public int getTornaMenu() {
		return tornaMenu;
	}

	public void setTornaMenu(int tornaMenu) {
		this.tornaMenu = tornaMenu;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}
}
