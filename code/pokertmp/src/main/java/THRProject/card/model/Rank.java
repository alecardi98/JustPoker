package THRProject.card.model;

import java.io.Serializable;

public class Rank implements Serializable{

	private int level; // indica la scala del punteggio
	private int value; // indica il valore del punteggio

	public Rank() {
		//il Rank viene inizializzato solo quando viene calcolato da Hand
		this.level=1;
		this.value=7;
	}

	/*
	 * Getter & Setter
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
