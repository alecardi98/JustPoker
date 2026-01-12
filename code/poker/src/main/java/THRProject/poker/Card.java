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
		String nome;
		switch (valore) {
		case 11:
			nome = "J";
			break;
		case 12:
			nome = "Q";
			break;
		case 13:
			nome = "K";
			break;
		case 14:
			nome = "A";
			break;
		default:
			nome = "" + valore;
		}
		return nome + " " + seme.toString();
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
