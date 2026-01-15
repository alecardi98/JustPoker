package THRProject.poker;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Game implements Serializable {

	private int currentTurn; // indica a chi tocca (ID del player)
	private GamePhase phase; // indica la fase di gioco
	private ConcurrentHashMap<Integer, Player> players; // rappresenta i player abbinati al loro clientId
	private boolean open; //indica che un giocatore ha aperto nella fase di apertura
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
			ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<Integer, Player>(this.players);
			for (Map.Entry<Integer, Player> p : players.entrySet()) {
				if (p.getValue().getStatus().isFold())
					players.remove(p.getKey()); // rimuovo dalla turnazione attuale chi ha foldato
			}

			Set<Integer> turns = players.keySet();
			int min = currentTurn;
			for (int id : turns) {
				if (id < min)
					min = id;
			}
			currentTurn = min;
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

}
