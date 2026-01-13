package THRProject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Card;
import THRProject.poker.Game;
import THRProject.poker.Player;

public class Server {

	private static Server server;
	private static final int PORT = 443;

	private static final int MAXPLAYERS = 2;
	private static final int MAXFICHES = 1500; // valore fiches iniziali
	private List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
	private Game game = new Game();

	private Server() {
	}

	/*
	 * Metodo che fa partire il Server permettendo la connessione dei Clients
	 */
	public void startServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server avviato sulla porta " + PORT);
			System.out.println("In attesa di client...");

			// attesa unione giocatori
			do {
				Socket clientSocket = serverSocket.accept(); // accetta nuovo client
				System.out.println("\nNuovo client connesso: " + clientSocket.getInetAddress());

				ClientHandler handler = new ClientHandler(clientSocket);
				System.out.println("Creato Handler per Client " + handler.getClientId());
				clientHandlers.add(handler);
				new Thread(handler).start();

			} while (clientHandlers.size() < MAXPLAYERS); // giocano solo MAXPLAYERS giocatori

		} catch (IOException e) {
			System.err.println("ERRORE! Il server non riesce a creare il socket.\n");
			e.printStackTrace();
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Metodo per aggiungere i Player al Game
	 */
	public synchronized void registerPlayer(int clientId, Player player) {
		player.setFiches(MAXFICHES);
		game.getPlayers().put(clientId, player);
		if (game.getPlayers().size() == MAXPLAYERS) {
			startGame(); // sarà il ClientHandler che aggiunge l'ultimo Player a far partire il gioco
		}
	}

	public synchronized void removePlayer(int clientId) {
		game.getPlayers().remove(clientId);
	}

	/*
	 * Fa partire la partita e gestisce il flusso di gioco
	 */
	private void startGame() {
		do {
			manageCards();
			generateSafeView();
			faseDiInvito();
			faseDiApertura();
			faseDiAccomodo();
			faseDiPuntata();
			faseDiShowdown();

			// TO DO

		} while (game.getPlayers().size() > 1);// fine mano
		System.out.println("Partita finita per mancanza di giocatori.");
	}

	/*
	 * Metodo che crea il mazzo di carte, lo mescola e distribuisce
	 */
	private void manageCards() {
		game.getDeck().resetDeck();
		game.getDeck().shuffle();
		assignCards();
	}

	/*
	 * Metodo che distribuisce le carte ai giocatori
	 */
	private void assignCards() {
		for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
			Player p = entry.getValue();
			for (int i = 0; i < 5; i++) {
				Card card = game.getDeck().getCards().get(0);
				p.getHand().add(card);
				game.getDeck().getCards().remove(0);
			}
		}
// stampa delle carte dei giocatori
//		for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
//			Player p = entry.getValue();
//			System.out.println(p.getUsername());
//			for (Card c : p.getHand()) {
//				System.out.println(c);
//			}
//		}
	}

	/*
	 * Metodo per creare una view dello stato di game che non esponga dati sensibili
	 * agli altri player
	 */
	private void generateSafeView() {
		for (ClientHandler c : clientHandlers) {
			Game gameView = new Game();
			gameView.setDeck(null);
			gameView.setCurrentTurn(game.getCurrentTurn());
			gameView.setBets(game.getBets());
			for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
				if (entry.getKey() == c.getClientId()) {
					gameView.getPlayers().put(entry.getKey(), entry.getValue());
				}
			}

			c.sendMessage(new Message(MessageType.START_GAME, gameView));
		}
	}

	/*
	 * Metodo che
	 */
	private void faseDiInvito() {
		
	}

	/*
	 * Metodo che
	 */
	private void faseDiApertura() {

	}

	/*
	 * Metodo che
	 */
	private void faseDiAccomodo() {

	}

	/*
	 * Metodo che
	 */
	private void faseDiPuntata() {

	}

	/*
	 * Metodo che
	 */
	private void faseDiShowdown() {

	}

	/*
	 * Metodo con il quale il server cede il turno al giocatore successivo: gli ID
	 * dei client vengono dati in modo incrementale in base al primo che arriva al
	 * tavolo e questo ordine sarà anche quello di gioco
	 */
	private void nextTurn() {
		Set<Integer> ids = game.getPlayers().keySet();

		int next = Integer.MAX_VALUE;
		int min = Integer.MAX_VALUE;

		for (int id : ids) {
			if (id > game.getCurrentTurn() && id < next) {
				next = id;
			}
			if (id < min) {
				min = id;
			}
		}

		if (next != Integer.MAX_VALUE) {
			game.setCurrentTurn(next);
		} else {
			game.setCurrentTurn(min);
		}
	}

	/*
	 * Metodo con il quale il server invia un messaggio a tutti i client
	 */
	private void broadcast(Object msg) {
		synchronized (clientHandlers) {
			for (ClientHandler c : clientHandlers) {
				c.sendMessage(msg);
			}
		}
	}

	/*
	 * Metodo per rimuovere il clientHandler dalla lista
	 */
	public synchronized void removeClient(ClientHandler clientHandler) {
		clientHandlers.remove(clientHandler);
	}

	/*
	 * Getter & Setter
	 */
	public static Server getServer() {
		if (server == null)
			server = new Server();
		return server;
	}

	public static int getMaxPlayers() {
		return MAXPLAYERS;
	}

}
