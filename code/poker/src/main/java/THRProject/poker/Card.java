package THRProject.poker;

import java.io.Serializable;

public class Card implements Serializable {
	private Suit seme;
	private int valore;

	public Card(Suit seme, int valore) {
		this.seme = seme;
		this.valore = valore;
	}

	public String toString() {
		return valore + " " + seme.toString();
	}

	/*
	 * Getter & Setter
	 */
	public int getValore() {
		return valore;
	}

	public void setValore(int valore) {
		this.valore = valore;
	}

	public Suit getSeme() {
		return seme;
	}

	public void setSeme(Suit seme) {
		this.seme = seme;
	}

}
