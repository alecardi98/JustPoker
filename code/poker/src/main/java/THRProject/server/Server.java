package THRProject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.poker.Card;
import THRProject.poker.Game;
import THRProject.poker.GamePhase;
import THRProject.poker.Player;
import THRProject.poker.PlayerStatus;
import THRProject.poker.Pot;
import THRProject.poker.Rank;

public class Server {

	private static Server server; // singleton Server
	private static final int PORT = 443; // porta per la connessione

	// private static final int MAXPLAYERS = 4; // numero massimo di player
	private static final int MAXPLAYERS = 2; // numero massimo di player
	private static final int MAXFICHES = 1500; // valore fiches iniziali
	private static final int MINBET = 25; // valore puntata minima

	private ConcurrentHashMap<Integer, ClientHandler> clientHandlers = new ConcurrentHashMap<Integer, ClientHandler>();
	private static int countId = 0; // assegazione id incrementale a partire da 0
	private static int readyCount = 0; // indica i giocatori pronti per la prossima mano (alla fine del turno)
	private Game game = new Game(MINBET);

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

				ClientHandler handler = new ClientHandler(clientSocket, nextId());
				System.out.println("Creato Handler per Client " + handler.getClientId());
				clientHandlers.put(handler.getClientId(), handler);
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
	public void registerPlayer(int clientId, Player player) {
		synchronized (game) {
			player.getStatus().setFiches(MAXFICHES);
			game.getPlayers().put(clientId, player);
			if (game.getPlayers().size() == MAXPLAYERS) {
				startGame(); // sarà il ClientHandler che aggiunge l'ultimo Player a far partire il gioco
				broadcast(new Message(MessageType.START_GAME, null));

			}
		}
	}

	/*
	 * Metodo per rimuovere un Player dal Game
	 */
	public void removePlayer(int clientId) {
		synchronized (game) {
			game.getPlayers().remove(clientId);
		}
	}

	/*
	 * Metodo per rimuovere il clientHandler dalla lista
	 */
	public void removeClient(int clientId) {
		synchronized (clientHandlers) {
			clientHandlers.remove(clientId);
			System.out.println("ClientHandler " + clientId + " rimosso.");
		}
	}

	/*
	 * Fa partire la partita dalla fase di invito
	 */
	private void startGame() {
		// resetGame(); //per ricominciare un'altra mano finita la precedente
		resetPhase();
		manageCards();
		broadcastSafeGameView();
	}

	/*
	 * Metodo per validare l'azione INVITO del client
	 */
	public void checkInvito(int clientId) {
		synchronized (game) {
			if (clientId == game.getCurrentTurn() && !game.getPlayers().get(clientId).getStatus().isFold()
					&& !game.getPlayers().get(clientId).getStatus().isEnd()) {

				int invito = MINBET;
				Player player = game.getPlayers().get(clientId);
				ClientHandler clientHandler = clientHandlers.get(clientId);

				if (invito > player.getStatus().getFiches()) {
					System.out.println("ERRORE! Invito Client " + clientId + " non valido.");
					clientHandler.sendMessage(new Message(MessageType.INVALID_ACTION, "invito"));
				} else {
					System.out.println("Invito Client " + clientId + " registrato.");
					game.getPot().setTotal(game.getPot().getTotal() + invito);
					player.getStatus().setEnd(true);
					player.getStatus().setFiches(player.getStatus().getFiches() - invito);
					nextTurn();

					clientHandler.sendMessage(new Message(MessageType.VALID_ACTION, "invito"));
					checkNextPhase();
					broadcastSafeGameView();
				}
			} else
				System.out.println("ERRORE! Non puoi giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione APRI del client
	 */
	public void checkApertura(int clientId, int puntata) {
		synchronized (game) {
			if (clientId == game.getCurrentTurn() && !game.getPlayers().get(clientId).getStatus().isFold()
					&& !game.getPlayers().get(clientId).getStatus().isEnd()) {

				Player player = game.getPlayers().get(clientId);
				player.getHand().checkRank();
				ClientHandler clientHandler = clientHandlers.get(clientId);

				if ((player.getHand().getRank().getLevel() == 2 && player.getHand().getRank().getValue() >= 22)
						|| player.getHand().getRank().getLevel() > 2) { // controllo almeno coppia di Jack
					if (puntata > player.getStatus().getFiches() || puntata < game.getPot().getMaxBet()) {
						System.out.println("ERRORE! Apertura Client " + clientId + " non valida.");
						clientHandler.sendMessage(new Message(MessageType.INVALID_ACTION, "apertura"));
					} else {
						System.out.println("Apertura Client " + clientId + " registrata.");
						game.setOpen(true);
						game.getPot().setTotal(game.getPot().getTotal() + puntata);
						player.getStatus().setTotalBet(player.getStatus().getTotalBet() + puntata);
						player.getStatus().setEnd(true);
						player.getStatus().setFiches(player.getStatus().getFiches() - puntata);
						nextTurn();

						if (puntata > game.getPot().getMaxBet()) {
							game.getPot().setMaxBet(puntata);

						}

						clientHandler.sendMessage(new Message(MessageType.VALID_ACTION, "apertura"));
						checkNextPhase();
						broadcastSafeGameView();
					}
				} else {
					System.out.println("ERRORE! Client " + clientId + " non possiede almeno una coppia di J");
					clientHandler.sendMessage(new Message(MessageType.INVALID_ACTION, "apertura"));
				}
			}
		}
	}

	/*
	 * Metodo per validare l'azione CAMBIO del client
	 */
	public void checkCambio(int clientId, Card[] cards) {
		synchronized (game) {
			if (clientId == game.getCurrentTurn() && !game.getPlayers().get(clientId).getStatus().isFold()
					&& !game.getPlayers().get(clientId).getStatus().isEnd()) {

				Player player = game.getPlayers().get(clientId);
				ClientHandler clientHandler = clientHandlers.get(clientId);

				if (cards.length <= 0 || cards.length > 5) {
					System.out.println("ERRORE! Cambio Client " + clientId + " non valido.");
					clientHandler.sendMessage(new Message(MessageType.INVALID_ACTION, "cambio"));
				} else {
					System.out.println("Cambio Client " + clientId + " registrato.");

					// rimuovo le carte
					for (int i = 0; i < cards.length; i++) {
						player.getHand().getCards().remove(cards[i]);
					}

					// pesca le carte
					for (int i = 0; i < cards.length; i++) {
						Card c = game.getDeck().getCards().get(0);
						player.getHand().getCards().add(c);
						game.getDeck().getCards().remove(c);
					}
					player.getStatus().setEnd(true);
					nextTurn();
					clientHandler.sendMessage(new Message(MessageType.VALID_ACTION, "cambio"));
					checkNextPhase();
					broadcastSafeGameView();
				}
			}
		}
	}

	/*
	 * Metodo per validare l'azione Servito del client
	 */
	public void checkServito(int clientId) {
		Player player = game.getPlayers().get(clientId);
		ClientHandler clientHandler = clientHandlers.get(clientId);
		System.out.println("Servito Client " + clientId + " registrato.");
		player.getStatus().setEnd(true);
		nextTurn();
		clientHandler.sendMessage(new Message(MessageType.VALID_ACTION, "servito"));
		checkNextPhase();
		broadcastSafeGameView();
	}

	/*
	 * Metodo che controlla quando cambiare la fase di gioco e la cambia
	 */
	private void checkNextPhase() {
		synchronized (game) {
			int count = 0;
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (p.getValue().getStatus().isEnd())
					count++; // conta chi ha finito il proprio turno
			}
			if (count == game.getPlayers().size())
				nextPhase();
		}
	}

	/*
	 * Metodo per resettare il game per la prossima fase
	 */
	private void resetPhase() {
		game.checkFirstTurn();
		game.getPot().setMaxBet(game.getPot().getMinBet());
		for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet())
			p.getValue().getStatus().resetStatus();
	}

	/*
	 * Metodo che gestisce le carte
	 */
	private void manageCards() {
		synchronized (game) {
			game.getDeck().resetDeck();
			game.getDeck().shuffle();
			for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
				Player p = entry.getValue();
				for (int i = 0; i < 5; i++) {
					Card card = game.getDeck().getCards().get(0);
					p.getHand().getCards().add(card);
					game.getDeck().getCards().remove(0);
				}
			}
//			// stampa delle carte dei giocatori
//			for (Map.Entry<Integer, Player> entry : game.getPlayers().entrySet()) {
//				Player p = entry.getValue();
//				System.out.println(p.getUsername());
//				for (Card c : p.getHand()) {
//					System.out.println(c);
//				}
//			}
		}
	}

	/*
	 * Metodo per creare ed inviare broadcast a tutti i client una view dello stato
	 * di game che non esponga dati sensibili agli altri client
	 */
	private void broadcastSafeGameView() {
		synchronized (game) {
			for (Map.Entry<Integer, ClientHandler> c : clientHandlers.entrySet()) {
				Game gameView = new Game(MINBET);
				gameView.setCurrentTurn(game.getCurrentTurn());
				gameView.setPhase(game.getPhase());
				for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
					if (Objects.equals(p.getKey(), c.getKey())) {
						gameView.getPlayers().put(p.getKey(), p.getValue());
					}
				}
				gameView.setPot(game.getPot());
				gameView.setDeck(null);

				c.getValue().sendMessage(new Message(MessageType.UPDATE, gameView));
			}
		}
	}

	/*
	 * Metodo con il quale il server cede il turno al giocatore successivo: gli ID
	 * dei client vengono dati in modo incrementale in base al primo che arriva al
	 * tavolo e questo ordine sarà anche quello di gioco
	 */
	private void nextTurn() {
		synchronized (game) {

			ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<Integer, Player>(game.getPlayers());
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (p.getValue().getStatus().isFold())
					players.remove(p.getKey()); // rimuovo dalla turnazione attuale chi ha foldato
			}

			Set<Integer> turns = players.keySet();

			int max = game.getCurrentTurn();
			for (int id : turns) {
				if (id > max)
					max = id;
			}

			if (max == game.getCurrentTurn()) { // se il massimo è il turno corrente devo tornare al minimo
				int min = game.getCurrentTurn();
				for (int id : turns) {
					if (id < min)
						min = id;
				}
				game.setCurrentTurn(min);
				return;
			}

			int minGreat = max; // è il minore tra i valori maggiori del turno corrente
			for (int id : turns) {
				if (id > game.getCurrentTurn() && id < max)
					max = id;
			}
			game.setCurrentTurn(max);
		}
	}

	/*
	 * Metodo per avanzare nelle fasi del gioco
	 */
	public void nextPhase() {
		synchronized (game) {
			switch (game.getPhase()) {
			case INVITO: // preparazione alla fase di Apertura
				game.setPhase(GamePhase.APERTURA);
				resetPhase();
				broadcastSafeGameView();
				break;

			case APERTURA: // preparazione alla fase di Accomodo
				game.setPhase(GamePhase.ACCOMODO);
				break;

			case ACCOMODO: // preparazione alla fase di Puntata
				game.setPhase(GamePhase.PUNTATA);
				break;

			case PUNTATA: // preparazione alla fase di Showdown
				game.setPhase(GamePhase.SHOWDOWN);
				break;

			case SHOWDOWN: // preparazione alla fase di End
				game.setPhase(GamePhase.END);
				break;

			case END: // preparazione alla fase di Invito
				game.setPhase(GamePhase.INVITO);
				startGame();
				break;
			}
		}
	}

	/*
	 * Metodo per rimuovere il clientHandler dalla lista
	 */
	public void foldPlayer(int clientId) {
		synchronized (game) {
			game.getPlayers().get(clientId).getStatus().setEnd(true);
			game.getPlayers().get(clientId).getStatus().setFold(true);
		}
	}

//	/*
//	 * Metodo per incrementare il contatore dei client pronti per la prossima mano
//	 * in modo atomico
//	 */
//	public synchronized void countReady() {
//		readyCount++;
//		checkStart();
//	}
//
//	/*
//	 * Metodo per iniziare una partita se l'ultimo client a scegliere si disconnette
//	 */
//	public synchronized void checkStart() {
//		if (readyCount == game.getPlayers().size()) {
//			readyCount = 0;
//			startInvito();
//		}
//	}

	/*
	 * Metodo per incrementare il contatore dei client in modo atomico
	 */
	public synchronized int nextId() {
		return countId++;
	}

	/*
	 * Metodo con il quale il server invia un messaggio a tutti i client
	 */
	private void broadcast(Object msg) {
		synchronized (clientHandlers) {
			for (Map.Entry<Integer, ClientHandler> c : clientHandlers.entrySet()) {
				c.getValue().sendMessage(msg);
			}
		}
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

	public Game getGame() {
		return game;
	}

	public static int getMinbet() {
		return MINBET;
	}

}
