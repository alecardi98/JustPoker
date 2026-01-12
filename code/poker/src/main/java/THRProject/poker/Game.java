package THRProject.poker;

import java.util.concurrent.ConcurrentHashMap;

/*
 * Classe che rappresenta lo stato del gioco
*/
public class Game {

	private int currentTurn; // indica a chi tocca (ID del player)
	private ConcurrentHashMap<Integer, Player> players; // rappresenta i player abbinati al loro clientId
	private int bets; // valore totale scommesso dai giocatori
	private Deck deck; // deck che contiene le informazioni sulle carte da gioco

	public Game() {
		players = new ConcurrentHashMap<Integer, Player>();
		currentTurn = 1;
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

}
