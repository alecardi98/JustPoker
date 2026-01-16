package THRProject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import THRProject.card.Card;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.message.Message;
import THRProject.message.ControlType;
import THRProject.player.Player;

public class Server {

	private static Server server; // singleton Server
	private static final int PORT = 443; // porta per la connessione

	// private static final int MAXPLAYERS = 4; // numero massimo di player
	private static final int MAXPLAYERS = 2; // numero massimo di player
	private static final int MAXFICHES = 1500; // valore fiches iniziali
	private static final int MINBET = 25; // valore puntata minima

	private ConcurrentHashMap<Integer, ClientHandler> clientHandlers = new ConcurrentHashMap<Integer, ClientHandler>(); // concorrente
	private static int countId = 0; // assegazione id incrementale a partire da 0
	private static int readyCount = 0; // giocatori pronti per il prossimo game
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
				broadcast(new Message(ControlType.START_GAME, null));
			}
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
		game.resetGame();
		broadcastSafeGameView();
	}

	/*
	 * Metodo per validare l'azione INVITO del client
	 */
	public void checkInvito(int clientId) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (clientId == game.getCurrentTurn() && !player.getStatus().isFold() && !player.getStatus().isEnd()) {
				int invito = MINBET;
				if (invito > player.getStatus().getFiches()) {
					System.out.println("ERRORE! Invito Client " + clientId + " non valido.");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "invito"));
				} else {
					// registra invito
					System.out.println("Invito Client " + clientId + " registrato.");
					game.getPot().setTotal(game.getPot().getTotal() + invito);
					player.getStatus().setEnd(true);
					player.getStatus().setFiches(player.getStatus().getFiches() - invito);
					game.nextTurn();
					clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "invito"));
					checkNextPhase();
					broadcastSafeGameView();
				}
			} else
				System.out.println("ERRORE! Non puoi giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione APRI del client: la prima apertura deve essere
	 * controllata, mentre le altre no
	 */
	public void checkApertura(int clientId, int puntata) {
		synchronized (game) {
			if (!game.isOpen()) {
				doApertura(clientId, puntata);
			} else {
				checkPuntata(clientId, puntata);
			}
		}
	}

	/*
	 * Metodo per il primo client che apre
	 */
	private void doApertura(int clientId, int puntata) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (clientId == game.getCurrentTurn() && !player.getStatus().isFold() && !player.getStatus().isEnd()) {
				player.getHand().checkRank();
				if ((player.getHand().getRank().getLevel() == 2 && player.getHand().getRank().getValue() >= 22)
						|| player.getHand().getRank().getLevel() > 2) { // controllo almeno coppia di Jack
					if (puntata > player.getStatus().getFiches()
							|| player.getStatus().getTotalBet() + puntata < game.getPot().getMaxBet()) {
						System.out.println("ERRORE! Apertura Client " + clientId + " non valida.");
						clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "apertura"));
					} else {
						// registra apertura
						System.out.println("Apertura Client " + clientId + " registrata.");
						game.setOpen(true);
						game.getPot().setTotal(game.getPot().getTotal() + puntata);
						player.getStatus().setTotalBet(player.getStatus().getTotalBet() + puntata);
						player.getStatus().setEnd(true);
						player.getStatus().setFiches(player.getStatus().getFiches() - puntata);
						game.nextTurn();
						clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "apertura"));
						for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
							if (p.getKey() != clientId) {
								p.getValue().getStatus().resetOpen();
							}
						}
						game.resetBet(clientId);
						checkNextPhase();
						broadcastSafeGameView();
					}
				} else {
					System.out.println("ERRORE! Client " + clientId + " non possiede almeno una coppia di J");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "apertura"));
				}
			}
		}
	}

	/*
	 * Metodo per validare l'azione PUNTA del client
	 */
	public void checkPuntata(int clientId, int puntata) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (clientId == game.getCurrentTurn() && !player.getStatus().isFold() && !player.getStatus().isEnd()) {

				if (puntata > player.getStatus().getFiches()
						|| player.getStatus().getTotalBet() + puntata < game.getPot().getMaxBet()) {
					System.out.println("ERRORE! Puntata Client " + clientId + " non valida.");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "puntata"));
				} else {
					// registra puntata
					System.out.println("Puntata Client " + clientId + " registrata.");
					game.getPot().setTotal(game.getPot().getTotal() + puntata);
					player.getStatus().setEnd(true);
					player.getStatus().setFiches(player.getStatus().getFiches() - puntata);
					game.nextTurn();
					clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "puntata"));
					game.resetBet(clientId);
					checkNextPhase();
					broadcastSafeGameView();
				}
			} else
				System.out.println("ERRORE! Non puoi giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione PASSA del client
	 */
	public void checkPassa(int clientId) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (clientId == game.getCurrentTurn() && !player.getStatus().isFold() && !player.getStatus().isEnd()) {
				// registra passa
				System.out.println("Passa Client " + clientId + " registrato.");
				player.getStatus().setEnd(true);
				player.getStatus().setPass(true);
				game.nextTurn();
				clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "passa"));
				checkRestart();
				checkNextPhase();
				broadcastSafeGameView();
			} else
				System.out.println("ERRORE! Non puoi giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione CAMBIO del client
	 */
	public void checkCambio(int clientId, ArrayList<Card> cardsToRemove) {
		synchronized (game) {
			if (clientId == game.getCurrentTurn() && !game.getPlayers().get(clientId).getStatus().isFold()
					&& !game.getPlayers().get(clientId).getStatus().isEnd()) {
				Player player = game.getPlayers().get(clientId);
				ClientHandler clientHandler = clientHandlers.get(clientId);
				if (cardsToRemove.isEmpty() || cardsToRemove.size() > 5) {
					System.out.println("ERRORE! Cambio Client " + clientId + " non valido.");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "cambio"));
				} else {
					System.out.println("Cambio Client " + clientId + " registrato.");
					// rimuovo le carte
					for (Card c : cardsToRemove) {
						player.getHand().getCards().remove(c);
					}
					// pesca le carte
					for (int i = 0; i < cardsToRemove.size(); i++) {
						Card c = game.getDeck().getCards().get(0);
						player.getHand().getCards().add(c);
						game.getDeck().getCards().remove(c);
					}
					player.getStatus().setEnd(true);
					game.nextTurn();
					clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "cambio"));
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
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			System.out.println("Servito Client " + clientId + " registrato.");
			player.getStatus().setEnd(true);
			game.nextTurn();
			clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "servito"));
			checkNextPhase();
			broadcastSafeGameView();
		}
	}

	/*
	 * Metodo per gestire la fase di showdown
	 */
	private void startShowdown() {
		checkWinner();
		game.setPhase(GamePhase.END);
		broadcastSafeGameView();
	}

	/*
	 * Metodo per dichiare il vincitore della mano
	 */
	private void checkWinner() {
		synchronized (game) {
			// estraggo solo i player attivi
			ConcurrentHashMap<Integer, Player> activePlayers = new ConcurrentHashMap<Integer, Player>();
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (!p.getValue().getStatus().isFold())
					activePlayers.put(p.getKey(), p.getValue());
			}
			// calcolo del rank
			for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
				p.getValue().getHand().checkRank();
			}
			// calcolo del massimo level
			int maxLevel = 1;
			for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
				if (p.getValue().getHand().getRank().getLevel() > maxLevel)
					maxLevel = p.getValue().getHand().getRank().getLevel();
			}
			// calcolo quanti hanno il massimo level
			int countMaxLevel = 0;
			for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
				if (p.getValue().getHand().getRank().getLevel() == maxLevel)
					countMaxLevel++;
			}
			if (countMaxLevel == 1) {// un solo vincitore
				for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
					if (p.getValue().getHand().getRank().getLevel() == maxLevel) {
						clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.WINNER, null));
						game.getPlayers().get(p.getKey()).getStatus().setFiches(
								game.getPlayers().get(p.getKey()).getStatus().getFiches() + game.getPot().getTotal());
					} else {
						clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.LOSER, null));
					}
				}
			} else { // spareggio level
				// calcolo massimo valore di value
				int maxValue = 0;
				for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
					if (p.getValue().getHand().getRank().getValue() > maxValue)
						maxValue = p.getValue().getHand().getRank().getValue();
				}
				// calcolo quanti hanno massimo valore di value
				int countMaxValue = 0;
				for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
					if (p.getValue().getHand().getRank().getValue() == maxValue)
						countMaxValue++;
				}
				if (countMaxValue == 1) { // un solo vincitore
					for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
						if (p.getValue().getHand().getRank().getValue() == maxValue) {
							clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.WINNER, null));
							game.getPlayers().get(p.getKey()).getStatus()
									.setFiches(game.getPlayers().get(p.getKey()).getStatus().getFiches()
											+ game.getPot().getTotal());
						} else {
							clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.LOSER, null));
						}
					}
				} else { // pareggio
					for (Map.Entry<Integer, Player> p : activePlayers.entrySet()) {
						if (p.getValue().getHand().getRank().getLevel() == maxLevel && countMaxValue != 0) {
							clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.WINNER, null));
							game.getPlayers().get(p.getKey()).getStatus()
									.setFiches(game.getPlayers().get(p.getKey()).getStatus().getFiches()
											+ (game.getPot().getTotal() / countMaxValue));
						} else {
							clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.LOSER, null));
						}
					}
				}
			}
		}
	}

	/*
	 * Metodo che controlla quando cambiare la fase di gioco e la cambia
	 */
	private void checkNextPhase() {
		synchronized (game) {
			int count = 0;
			int active = 0;
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (p.getValue().getStatus().isEnd() && !p.getValue().getStatus().isFold())
					count++; // conta chi ha finito il proprio turno e non ha foldato

				if (!p.getValue().getStatus().isFold())
					active++; // conta chi non ha foldato
			}
			if (count == active)
				nextPhase();
		}
	}

	/*
	 * Metodo che controlla quando hanno passato tutti l'apertura e riavvia
	 */
	private void checkRestart() {
		synchronized (game) {
			int count = 0;
			int active = 0;
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (p.getValue().getStatus().isPass() && !p.getValue().getStatus().isFold())
					count++; // conta chi ha passato e non ha foldato
				if (!p.getValue().getStatus().isFold())
					active++; // conta chi non ha foldato
			}
			if (count == active)
				game.setPhase(GamePhase.ENDPASS);
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
				game.resetPhase();
				broadcastSafeGameView();
				break;
			case APERTURA: // preparazione alla fase di Accomodo
				game.setPhase(GamePhase.ACCOMODO);
				game.resetPhase();
				broadcastSafeGameView();
				break;
			case ACCOMODO: // preparazione alla fase di Puntata
				game.setPhase(GamePhase.PUNTATA);
				game.resetPhase();
				broadcastSafeGameView();
				break;
			case PUNTATA: // preparazione alla fase di Showdown
				game.setPhase(GamePhase.SHOWDOWN);
				game.resetPhase();
				broadcastSafeGameView();
				startShowdown();
				break;
			default:
				System.out.println("ERRORE! Fase di gioco non esistente.");
			}
		}
	}

	/*
	 * Metodo per incrementare il contatore dei client pronti per la prossima mano
	 * in modo atomico
	 */
	public synchronized void countReady(int clientId) {
		if (!game.getPlayers().get(clientId).getStatus().isEnd()) {
			game.getPlayers().get(clientId).getStatus().setEnd(true);
			clientHandlers.get(clientId).sendMessage(new Message(ControlType.VALID_ACTION, "ready"));
			readyCount++;
			checkStart();
		} else {
			clientHandlers.get(clientId).sendMessage(new Message(ControlType.INVALID_ACTION, "ready"));
		}
	}

	/*
	 * Metodo per iniziare una partita se l'ultimo client a scegliere si disconnette
	 */
	public synchronized void checkStart() {
		if (readyCount == game.getPlayers().size()) {
			if (readyCount == 1) {
				System.out.println("ERRORE! Partita terminata per mancanza di giocatori.");
				clientHandlers.get(0).sendMessage(new Message(ControlType.WINNER, null));
				System.exit(1);
			} else {
				readyCount = 0;
				if (game.getPhase().equals(GamePhase.END)) {
					game.setPhase(GamePhase.INVITO);
					startGame();
				}
				if (game.getPhase().equals(GamePhase.ENDPASS)) {
					game.setPhase(GamePhase.INVITO);
					game.splitPot();
					startGame();
				}
			}
		}
	}

	/*
	 * Metodo per creare ed inviare broadcast a tutti i client una view dello stato
	 * di game che non esponga dati sensibili agli altri client
	 */
	public void broadcastSafeGameView() {
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
				c.getValue().sendMessage(new Message(ControlType.UPDATE, gameView));
			}
		}
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
	 * Metodo per incrementare il contatore dei client in modo atomico
	 */
	public synchronized int nextId() {
		return countId++;
	}

	/*
	 * Metodo per il testing
	 */
	static void resetInstance() {
		server = null;
	}

	/*
	 * Getter & Setter
	 */
	public static Server getServer() {
		if (server == null)
			server = new Server();
		return server;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setClientHandlers(ConcurrentHashMap<Integer, ClientHandler> clientHandlers) {
		this.clientHandlers = clientHandlers;
	}
}
