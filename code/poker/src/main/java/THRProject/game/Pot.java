package THRProject.game;

import java.io.Serializable;

public class Pot implements Serializable {

	private int total; // valore totale scommesso dai giocatori
	private int maxBet; // puntata massima attuale
	private int minBet; // valore puntata minima decisa dal server

	public Pot(int minBet) {
		total = 0;
		this.minBet = minBet;
		maxBet = this.minBet;
	}
	
	public Pot(Pot other) {
		total = other.total;
		maxBet = other.maxBet;
		minBet = other.minBet;
	}

	/*
	 * Getter & Setter
	 */
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(int maxBet) {
		this.maxBet = maxBet;
	}

	public int getMinBet() {
		return minBet;
	}

}
