package THRProject.server;

import THRProject.database.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import THRProject.card.model.Card;
import THRProject.card.model.Rank;
import THRProject.card.model.Suit;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.message.Message;
import THRProject.message.ControlType;
import THRProject.player.Player;

public final class Server {

	private static final Logger logger = LogManager.getLogger(Server.class);

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
	private DatabaseManager dbManager = new DatabaseManager();

	private Server() {
	}

	/*
	 * Metodo che fa partire il Server permettendo la connessione dei Clients
	 */
	public void startServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			logger.info("Server avviato sulla porta " + PORT);
			logger.info("In attesa di client...");
			// attesa unione giocatori
			do {
				Socket clientSocket = serverSocket.accept(); // accetta nuovo client
				logger.info("Nuovo client connesso: " + clientSocket.getInetAddress());
				ClientHandler handler = new ClientHandler(clientSocket, nextId());
				logger.info("Creato Handler per Client " + handler.getClientId());
				clientHandlers.put(handler.getClientId(), handler);
				new Thread(handler).start();
			} while (clientHandlers.size() < MAXPLAYERS); // giocano solo MAXPLAYERS giocatori
		} catch (IOException e) {
			logger.warn("ERRORE! Il server non riesce a creare il socket.");
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
	 * Gestisce il Login verificando i dati nel DB
	 */
	public void handleLogin(int clientId, Player player) {
		String response = dbManager.loginUser(player.getUsername(), player.getPassword());
		clientHandlers.get(clientId).sendMessage(new Message(ControlType.LOGIN, response));

		if (response.equals("Login effettuato.")) {
			Player gamePlayer = new Player(player.getUsername());
			registerPlayer(clientId, gamePlayer);
		}
	}

	/*
	 * Gestisce la Registrazione salvando i dati nel DB
	 */
	public void handleRegister(int clientId, Player player) {
		String response = dbManager.registerUser(player.getUsername(), player.getPassword());

		if (response.equals("Registrazione effettuata.")) {
			clientHandlers.get(clientId).sendMessage(new Message(ControlType.VALID_ACTION, response));
		} else {
			clientHandlers.get(clientId).sendMessage(new Message(ControlType.INVALID_ACTION, response));
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
				broadcast(new Message(ControlType.START_GAME, null));
				startGame(); // sarà il ClientHandler che aggiunge l'ultimo Player a far partire il gioco
			}
		}
	}

	/*
	 * Metodo per rimuovere il clientHandler dalla lista
	 */
	public void removeClient(int clientId) {
		synchronized (clientHandlers) {
			clientHandlers.remove(clientId);
			logger.info("ClientHandler " + clientId + " rimosso.");
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
			if (game.getPhase().equals(GamePhase.INVITO) && clientId == game.getCurrentTurn()
					&& player.getStatus().isActive()) {
				int invito = MINBET;
				if (invito >= player.getStatus().getFiches()) {
					logger.info("All-in Client " + clientId + " registrato.");
				} else {
					logger.info("Invito Client " + clientId + " registrato.");
				}
				game.punta(player, invito);
				game.nextTurn();
				clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Invito registrato."));
				checkNextPhase();
				broadcastSafeGameView();
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
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
			if (game.getPhase().equals(GamePhase.APERTURA) && clientId == game.getCurrentTurn()
					&& player.getStatus().isActive()) {
				player.getHand().checkRank();
				if (player.canOpen()) { // controllo almeno coppia di Jack
					if (!valorePuntataValido(puntata, player)) {
						logger.info("ERRORE! Apertura Client " + clientId + " non valida.");
						clientHandler.sendMessage(
								new Message(ControlType.INVALID_ACTION, "ERRORE! Valore apertura errato."));
					} else {
						if (puntata >= player.getStatus().getFiches()) {
							logger.info("All-in Client " + clientId + " registrato.");
						} else {
							logger.info("Apertura Client " + clientId + " registrata.");
						}
						game.punta(player, puntata);
						game.setOpen(true);
						game.nextTurn();
						clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Apertura registrata."));
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
					logger.info("ERRORE! Client " + clientId + " non possiede almeno una coppia di J");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "ERRORE! Non puoi aprire."));
				}
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione PUNTA del client
	 */
	public void checkPuntata(int clientId, int puntata) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if ((game.getPhase().equals(GamePhase.PUNTATA) || game.getPhase().equals(GamePhase.APERTURA))
					&& clientId == game.getCurrentTurn() && player.getStatus().isActive()) {
				if (!valorePuntataValido(puntata, player)) {
					logger.info("ERRORE! Puntata Client " + clientId + " non valida.");
					clientHandler
							.sendMessage(new Message(ControlType.INVALID_ACTION, "ERRORE! Valore puntata errato."));
				} else {
					if (puntata >= player.getStatus().getFiches()) {
						logger.info("All-in Client " + clientId + " registrato.");
					} else {
						logger.info("Puntata Client " + clientId + " registrata.");
					}
					game.punta(player, puntata);
					game.nextTurn();
					clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Puntata registrata."));
					game.resetBet(clientId);
					checkNextPhase();
					broadcastSafeGameView();
				}
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
		}
	}

	/*
	 * Metodo per controllare se la puntata ha un valore esatto
	 */
	public boolean valorePuntataValido(int puntata, Player player) {
		return player.getStatus().getTotalBet() + puntata >= game.getPot().getMaxBet();
	}

	/*
	 * Metodo per validare l'azione PASSA del client
	 */
	public void checkPassa(int clientId) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (game.getPhase().equals(GamePhase.APERTURA) && clientId == game.getCurrentTurn()
					&& player.getStatus().isActive()) {
				// registra passa
				logger.info("Passa Client " + clientId + " registrato.");
				player.getStatus().setEnd(true);
				player.getStatus().setPass(true);
				game.nextTurn();
				clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Passa registrato."));
				restartPass();
				checkNextPhase();
				broadcastSafeGameView();
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione CAMBIO del client
	 */
	public void checkCambio(int clientId, ArrayList<Card> cardsToRemove) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (game.getPhase().equals(GamePhase.ACCOMODO) && clientId == game.getCurrentTurn()
					&& player.getStatus().isActive()) {
				if (cardsToRemove.isEmpty() || cardsToRemove.size() > 3) {
					logger.info("ERRORE! Cambio Client " + clientId + " non valido.");
					clientHandler.sendMessage(new Message(ControlType.INVALID_ACTION, "ERRORE! Cambio errato."));
				} else {
					logger.info("Cambio Client " + clientId + " registrato.");

					// rimuovo le carte
					for (Card c : cardsToRemove) {
						Suit seme = c.getSeme();
						int valore = c.getValore();

						for (int i = player.getHand().getCards().size() - 1; i >= 0; i--) {
							if (player.getHand().getCards().get(i).getSeme().equals(seme)
									&& player.getHand().getCards().get(i).getValore() == valore) {
								player.getHand().getCards().remove(i);
							}
						}
					}

					// pesca le carte
					for (int i = 0; i < cardsToRemove.size(); i++) {
						Card c = game.getDeck().getCards().get(0);
						player.getHand().getCards().add(c);
						game.getDeck().getCards().remove(0);
					}

					player.getStatus().setEnd(true);
					game.nextTurn();
					clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Cambio registrato."));
					checkNextPhase();
					broadcastSafeGameView();
				}
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");

		}
	}

	/*
	 * Metodo per validare l'azione Servito del client
	 */
	public void checkServito(int clientId) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (game.getPhase().equals(GamePhase.ACCOMODO) && clientId == game.getCurrentTurn()
					&& player.getStatus().isActive()) {
				logger.info("Servito Client " + clientId + " registrato.");
				player.getStatus().setEnd(true);
				game.nextTurn();
				clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Servito registrato."));
				checkNextPhase();
				broadcastSafeGameView();
			} else
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
		}
	}

	/*
	 * Metodo per validare l'azione Fold del client
	 */
	public void checkFold(int clientId) {
		synchronized (game) {
			Player player = game.getPlayers().get(clientId);
			ClientHandler clientHandler = clientHandlers.get(clientId);
			if (clientId == game.getCurrentTurn() && player.getStatus().isActive()) {
				logger.info("Fold Client " + clientId + " registrato.");
				game.foldPlayer(player);
				clientHandler.sendMessage(new Message(ControlType.VALID_ACTION, "Fold registrato."));
				checkNextPhase();
				broadcastSafeGameView();
			} else {
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
			}

		}
	}

	/*
	 * Metodo per gestire la fase di showdown
	 */
	private void startShowdown() {
		checkWinner();
		checkBankrupt();
		game.setPhase(GamePhase.END);
		broadcastSafeGameView();
	}

	/*
	 * Metodo per dichiare il vincitore della mano
	 */
	private void checkWinner() {
		synchronized (game) {
			Map<Integer, Player> activePlayers = getActivePlayers();
			evaluateHands(activePlayers);
			Map<Integer, Player> winners = determineWinners(activePlayers);
			splitPot(activePlayers, winners);
		}
	}

	/*
	 * Metodo che restituisce i giocatori attivi (che non hanno foldato)
	 */
	private Map<Integer, Player> getActivePlayers() {
		Map<Integer, Player> activePlayers = new ConcurrentHashMap<Integer, Player>();

		for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
			if (!p.getValue().getStatus().isFold()) {
				activePlayers.put(p.getKey(), p.getValue());
			}
		}
		return activePlayers;
	}

	/*
	 * Metodo che calcola il punteggio delle mani dei giocatori
	 */
	private void evaluateHands(Map<Integer, Player> players) {
		for (Player p : players.values()) {
			p.getHand().checkRank();
		}
	}

	/*
	 * Metodo che determina i vincitori potenziali
	 */
	private Map<Integer, Player> determineWinners(Map<Integer, Player> players) {
		int maxLevel = 1;
		int maxValue = 7;

		for (Player p : players.values()) {
			Rank r = p.getHand().getRank();

			if (r.getLevel() > maxLevel || (r.getLevel() == maxLevel && r.getValue() > maxValue)) {
				maxLevel = r.getLevel();
				maxValue = r.getValue();
			}
		}

		Map<Integer, Player> winners = new ConcurrentHashMap<Integer, Player>();
		for (Map.Entry<Integer, Player> p : players.entrySet()) {
			Rank r = p.getValue().getHand().getRank();
			if (r.getLevel() == maxLevel && r.getValue() == maxValue) {
				winners.put(p.getKey(), p.getValue());
			}
		}
		return winners;
	}

	/*
	 * Metodo che distribuisce il piatto ai vincitori
	 */
	private void splitPot(Map<Integer, Player> activePlayers, Map<Integer, Player> winners) {
		int potShare = game.getPot().getTotal() / winners.size();

		for (Map.Entry<Integer, Player> entry : activePlayers.entrySet()) {
			int playerId = entry.getKey();

			if (winners.containsKey(playerId)) {
				clientHandlers.get(playerId).sendMessage(new Message(ControlType.WINNER, null));
				logger.info("Client " + playerId + " ha vinto!");
				game.getPlayers().get(playerId).getStatus()
						.setFiches(game.getPlayers().get(playerId).getStatus().getFiches() + potShare);
			} else {
				clientHandlers.get(playerId).sendMessage(new Message(ControlType.LOSER, null));
				logger.info("Client " + playerId + " ha perso!");

			}
		}
	}

	/*
	 * Metodo per controllare che il player non sia in bancarotta, e che quindi
	 * possa continuare a giocare
	 */
	private void checkBankrupt() {
		synchronized (game) {
			for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
				if (p.getValue().getStatus().getFiches() <= 0) {
					logger.info("Client " + p.getKey() + " in bancarotta.");
					clientHandlers.get(p.getKey()).sendMessage(new Message(ControlType.ENDGAME, "Bancarotta! Hai perso."));
					clientHandlers.get(p.getKey()).cleanUp();
				}
			}
		}
	}

	/*
	 * Metodo che controlla quando cambiare la fase di gioco e la cambia solo se
	 * tutti gli attivi hanno fatto una scelta valida
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
			if (count == active) {
				if(count == 1) {
					game.setPhase(GamePhase.ENDPASS);
				} 
				nextPhase();
			}
		}
	}

	/*
	 * Metodo che controlla quando hanno passato tutti l'apertura e riavvia
	 */
	private void restartPass() {
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
			case ENDPASS:
				game.resetPhase();
				broadcastSafeGameView();
				break;
			default:
				logger.error("ERRORE! Fase di gioco non esistente.");
			}
		}
	}

	/*
	 * Metodo per incrementare il contatore dei client pronti per la prossima mano
	 * in modo atomico
	 */
	public void countReady(int clientId) {
		synchronized (game) {
			if (!game.getPlayers().get(clientId).getStatus().isEnd()) {
				game.getPlayers().get(clientId).getStatus().setEnd(true);
				clientHandlers.get(clientId).sendMessage(new Message(ControlType.VALID_ACTION, "Ready registrato."));
				readyCount++;
				checkStart();
			} else {
				logger.info("ERRORE! Client " + clientId + " non può giocare.");
			}
		}
	}

	/*
	 * Metodo per iniziare una partita se l'ultimo client a scegliere si disconnette
	 */
	public void checkStart() {
		synchronized (game) {
			if (readyCount == game.getPlayers().size()) {
				if (readyCount == 1) {
					logger.warn("ERRORE! Partita terminata per mancanza di giocatori.");
					clientHandlers.get(0).sendMessage(new Message(ControlType.ENDGAME, "Giocatori non sufficenti!"));
					System.exit(1);
				} else {
					readyCount = 0;
					if (game.getPhase().equals(GamePhase.END)) {
						game.setPhase(GamePhase.INVITO);
						logger.info("Partita ricominciata: mano finita.");
						startGame();
					}
					if (game.getPhase().equals(GamePhase.ENDPASS)) {
						game.setPhase(GamePhase.INVITO);
						logger.info("Partita ricominciata: tutti hanno passato.");
						splitPot(getActivePlayers(), getActivePlayers());
						startGame();
					}
				}
			} else
				game.nextTurn();

		}
	}

	/*
	 * Metodo per creare ed inviare broadcast a tutti i client una view dello stato
	 * di game che non esponga dati sensibili agli altri client
	 */
	public void broadcastSafeGameView() {
		synchronized (game) {
			for (Map.Entry<Integer, ClientHandler> c : clientHandlers.entrySet()) {
				Game gameView = new Game(game);
				gameView.setDeck(null);

				for (Map.Entry<Integer, Player> p : gameView.getPlayers().entrySet()) {
					if (!Objects.equals(p.getKey(), c.getKey())) {
						gameView.getPlayers().remove(p.getKey());
					}
				}
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

	public ConcurrentHashMap<Integer, ClientHandler> getClientHandlers() {
		return clientHandlers;
	}
}
