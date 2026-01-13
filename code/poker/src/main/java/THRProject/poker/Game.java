package THRProject.poker;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class Game implements Serializable {

	private int currentTurn; // indica a chi tocca (ID del player)
	private GamePhase phase; // indica la fase di gioco
	private ConcurrentHashMap<Integer, Player> players; // rappresenta i player abbinati al loro clientId
	private int bets; // valore totale scommesso dai giocatori
	private Deck deck; // deck che contiene le informazioni sulle carte da gioco

	public Game() {
		currentTurn = 0;
		phase = GamePhase.INVITO;
		players = new ConcurrentHashMap<Integer, Player>();
		bets = 0;
		deck = new Deck();
	}

	/*
	 * Getter & Setter
	 */
	public ConcurrentHashMap<Integer, Player> getPlayers() {
		return players;
	}

	public int getBets() {
		return bets;
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

	public void setBets(int bets) {
		this.bets = bets;
	}

}
