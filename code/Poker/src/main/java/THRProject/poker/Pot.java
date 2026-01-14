package THRProject.poker;

import java.io.Serializable;

import THRProject.server.Server;

public class Pot implements Serializable{

	private int total; // valore totale scommesso dai giocatori
	private int maxBet; // puntata massima attuale

	public Pot() {
		total = 0;
		maxBet = Server.getMinbet();
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

}
