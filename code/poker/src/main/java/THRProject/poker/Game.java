package THRProject.poker;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Game implements Serializable {

	private int currentTurn; // indica a chi tocca (ID del player)
	private GamePhase phase; // indica la fase di gioco
	private ConcurrentHashMap<Integer, Player> players; // rappresenta i player abbinati al loro clientId
	private Pot pot; // il piatto del tavolo
	private Deck deck; // deck che contiene le informazioni sulle carte da gioco

	public Game() {
		checkCurrentTurn();
		phase = GamePhase.INVITO;
		players = new ConcurrentHashMap<Integer, Player>();
		pot = new Pot();
		deck = new Deck();
	}

	/*
	 * Metodo che modifica currentTurn in base ai clientId dei players
	 */
	public void checkCurrentTurn() {
		synchronized (this) {
			Object[] set = players.keySet().toArray();
			int min = (int) set[0];
			for (int i = 1; i < set.length; i++) {
				if ((int) set[i] < min)
					min = (int) set[i];
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

}
