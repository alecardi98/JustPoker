package THRProject.game;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import THRProject.card.Card;
import THRProject.card.Deck;
import THRProject.card.Hand;
import THRProject.player.Player;

public class Game implements Serializable {

	private int currentTurn; // indica a chi tocca (ID del player)
	private GamePhase phase; // indica la fase di gioco
	private ConcurrentHashMap<Integer, Player> players; // rappresenta i player abbinati al loro clientId
	private boolean open; // indica che un giocatore ha aperto nella fase di apertura
	private Pot pot; // il piatto del tavolo
	private Deck deck; // deck che contiene le informazioni sulle carte da gioco

	public Game(int minBet) {
		phase = GamePhase.INVITO;
		players = new ConcurrentHashMap<Integer, Player>();
		open = false;
		pot = new Pot(minBet);
		deck = new Deck();
	}

	/*
	 * Metodo che modifica currentTurn in base ai clientId dei players
	 */
	public void checkFirstTurn() {
		synchronized (this) {
			Set<Integer> turns = getPossibleTurns();
			int min = Integer.MAX_VALUE;
			for (int id : turns) {
				if (id < min)
					min = id;
			}
			currentTurn = min;
		}
	}

	/*
	 * Metodo con il quale il server cede il turno al giocatore successivo: gli ID
	 * dei client vengono dati in modo incrementale in base al primo che arriva al
	 * tavolo e questo ordine sarà anche quello di gioco
	 */
	public void nextTurn() {
		synchronized (this) {
			Set<Integer> turns = getPossibleTurns();
			int max = currentTurn;
			for (int id : turns) {
				if (id > max)
					max = id;
			}
			if (max == currentTurn) { // se il massimo è il turno corrente devo tornare al minimo
				int min = currentTurn;
				for (int id : turns) {
					if (id < min)
						min = id;
				}
				currentTurn = min;
				return;
			}
			for (int id : turns) {
				if (id > currentTurn && id < max)
					max = id;
			}
			currentTurn = max;
		}
	}

	/*
	 * Metodo che restituisce il set di turni sul quale turnare
	 */
	public Set<Integer> getPossibleTurns() {
		ConcurrentHashMap<Integer, Player> activePlayers = new ConcurrentHashMap<Integer, Player>(players);
		for (Map.Entry<Integer, Player> p : players.entrySet()) {
			if (p.getValue().getStatus().isFold())
				activePlayers.remove(p.getKey()); // rimuovo dalla turnazione attuale chi ha foldato
		}
		return activePlayers.keySet();
	}

	/*
	 * Metodo che serve per registrare la puntata nel game
	 */
	public void punta(Player player, int puntata) {
		pot.setTotal(pot.getTotal() + puntata);
		player.getStatus().setTotalBet(player.getStatus().getTotalBet() + puntata);
		player.getStatus().setFiches(player.getStatus().getFiches() - puntata);
		player.getStatus().setEnd(true);
	}

	/*
	 * Metodo che resetta lo stato dei player in debito dopo un rilancio
	 */
	public void resetBet(int clientId) {
		synchronized (this) {
			Player player = players.get(clientId);
			if (player.getStatus().getTotalBet() > pot.getMaxBet()) {
				pot.setMaxBet(player.getStatus().getTotalBet());
				Set<Integer> ids = players.keySet();
				for (int id : ids) {
					if (players.get(id).getStatus().getTotalBet() < pot.getMaxBet()
							&& !players.get(id).getStatus().isFold()) { // solo se attivi
						players.get(id).getStatus().setEnd(false);
					}
				}
			}
		}
	}

	/*
	 * Metodo per resettare il game per la prossima fase
	 */
	public void resetPhase() {
		synchronized (this) {
			checkFirstTurn();
			pot.setMaxBet(pot.getMinBet());
			for (Map.Entry<Integer, Player> p : players.entrySet()) {
				p.getValue().getStatus().resetStatusForPhase();
			}
		}
	}

	/*
	 * Metodo che gestisce le carte
	 */
	public void manageCards() {
		synchronized (this) {
			deck.resetDeck();
			deck.shuffle();
			for (Map.Entry<Integer, Player> entry : players.entrySet()) {
				Player p = entry.getValue();
				for (int i = 0; i < 5; i++) {
					Card card = deck.getCards().get(0);
					p.getHand().getCards().add(card);
					deck.getCards().remove(0);
				}
			}
//			// stampa delle carte dei giocatori
//			for (Map.Entry<Integer, Player> entry : players.entrySet()) {
//				Player p = entry.getValue();
//				System.out.println(p.getUsername());
//				for (Card c : p.getHand()) {
//					System.out.println(c);
//				}
//			}
		}
	}

	/*
	 * Metodo per dividere il piatto in caso di parità o di fold generale
	 */
	public void splitPot() {
		synchronized (this) {
			int active = 0;
			for (Map.Entry<Integer, Player> p : players.entrySet()) {
				if (!p.getValue().getStatus().isFold())
					active++;
			}

			for (Map.Entry<Integer, Player> p : players.entrySet()) {
				if (!p.getValue().getStatus().isFold())
					p.getValue().getStatus()
							.setFiches(p.getValue().getStatus().getFiches() + (pot.getTotal() / active));
			}
		}
	}

	/*
	 * Metodo per far foldare il player
	 */
	public void foldPlayer(Player player) {
		synchronized (this) {
			player.getStatus().setEnd(true);
			player.getStatus().setFold(true);
			checkFirstTurn();
		}
	}

	/*
	 * Metodo per rimuovere un Player dal Game
	 */
	public void removePlayer(int clientId) {
		synchronized (this) {
			players.remove(clientId);
		}
	}

	/*
	 * Metodo per resettare game per la prossima mano
	 */
	public void resetGame() {
		synchronized (this) {
			pot.setTotal(0);
			for (Map.Entry<Integer, Player> p : players.entrySet()) {
				p.getValue().setHand(new Hand());
				p.getValue().getStatus().resetStatusForGame();
			}
			open = false;
			resetPhase();
			manageCards();
		}
	}

	/*
	 * Getter & Setter
	 */
	public ConcurrentMap<Integer, Player> getPlayers() {
		return players;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Pot getPot() {
		return pot;
	}

	public void setPot(Pot pot) {
		this.pot = pot;
	}

	public GamePhase getPhase() {
		return phase;
	}

	public void setPhase(GamePhase phase) {
		this.phase = phase;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setPlayers(ConcurrentHashMap<Integer, Player> players) {
		this.players = players;
	}

}
