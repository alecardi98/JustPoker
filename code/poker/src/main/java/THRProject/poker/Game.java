package THRProject.poker;

import java.util.ArrayList;

/*
 * Classe che rappresenta lo stato del gioco
*/
public class Game {

	private int currentTurn; // indica a chi tocca (ID del player)
	private ArrayList<Player> turnOrder; // rappresenta i player nell'ordine di gioco
	private int bets; // valore totale scommesso dai giocatori

	public Game() {
		turnOrder = new ArrayList<Player>();
		currentTurn = 0;
	}

	/*
	 * Getter & Setter
	 */
	public ArrayList<Player> getTurnOrder() {
		return turnOrder;
	}

	public void setTurnOrder(ArrayList<Player> turnOrder) {
		this.turnOrder = turnOrder;
	}

	public int getBets() {
		return bets;
	}

	public void setBets(int bets) {
		this.bets = bets;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

}
