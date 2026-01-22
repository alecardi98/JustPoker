package THRProject.card.logic;

import java.io.Serializable;
import java.util.ArrayList;

import THRProject.card.model.Card;
import THRProject.card.model.Rank;

public class Hand implements Serializable {

	private ArrayList<Card> cards;
	private Rank rank;

	public Hand() {
		cards = new ArrayList<Card>();
		rank = new Rank();
	}
	
	public Hand(Hand other) {
		cards = new ArrayList<Card>(other.cards);
		rank = new Rank(other.rank);
	}

	/*
	 * Calcola il Rank della mano, ovvero il valore del suo punteggio (carta alta 1,
	 * coppia 2, doppia coppia 3, tris 4, scala 5, colore 6, full 7, poker 8, scala
	 * colore 9)
	 */
	public void checkRank() {

		int[] counters = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 7; i <= 14; i++) {
			for (Card c : cards) {
				if (c.getValore() == i) {
					counters[i - 7]++;
				}
			}
		}
		checkCartaAlta(counters);
		checkCoppiaDoppiaCoppia(counters);
		checkTris(counters);
		checkColore();
		checkScala(counters);
		checkPoker(counters);
	}

	/*
	 * Metodo per valutare il poker
	 */
	private void checkPoker(int[] counters) {
		for (int i = 0; i <= 7; i++) {
			if (counters[i] == 4) {
				rank.setLevel(8);
				rank.setValue((i + 7) * 4);
			}
		}
	}

	/*
	 * Metodo per valutare il tris ed il full
	 */
	private void checkTris(int[] counters) {
		// ricerca tris o full
		for (int i = 0; i <= 7; i++) {
			if (counters[i] == 3) {
				rank.setLevel(4);
				rank.setValue((i + 7) * 3);
				checkFull(counters);
				return;
			}
		}
	}

	/*
	 * Metodo per valutare il full
	 */
	private void checkFull(int[] counters) {
		for (int j = 0; j <= 7; j++) {
			if (counters[j] == 2) {
				rank.setLevel(7);
				rank.setValue(rank.getValue() + ((j + 7) * 2));
				return;
			}
		}
	}

	/*
	 * Metodo per valutare la coppia e la doppia coppia
	 */
	private void checkCoppiaDoppiaCoppia(int[] counters) {
		// ricerca coppia o doppia coppia
		for (int i = 0; i <= 7; i++) {
			if (counters[i] == 2) { // coppia
				rank.setLevel(2);
				rank.setValue((i + 7) * 2);
				for (int j = 0; j <= 7; j++) {
					if (counters[j] == 2 && j != i) { // doppia coppia
						rank.setLevel(3);
						rank.setValue(rank.getValue() + ((j + 7) * 2));
						return;
					}
				}
				return;
			}
		}
	}

	/*
	 * Metodo per valutare il colore
	 */
	private void checkColore() {
		if (isColore()) {
			rank.setLevel(6);

			int sum = 0;
			for (Card c : cards)
				sum = sum + c.getValore();
			rank.setValue(sum);
		}
	}

	/*
	 * Metodo per valutare la scala e la scala colore
	 */
	private void checkScala(int[] counters) {
		for (int i = 0; i <= 3; i++) {
			if (counters[i] != 0 && counters[i] == counters[i + 1] && counters[i + 1] == counters[i + 2]
					&& counters[i + 2] == counters[i + 3] && counters[i + 3] == counters[i + 4]) { // scala
				rank.setLevel(5);
				rank.setValue(45 + (5 * i));
				checkScalaColore();
				return;
			}
		}
	}

	/*
	 * Metodo per valutare la scala colore
	 */
	private void checkScalaColore() {
		if (isColore()) {
			rank.setLevel(9);
		}
	}

	/*
	 * Metodo per valutare la carta alta
	 */
	private void checkCartaAlta(int[] counters) {
		int max = 7;
		for (Card c : cards) {
			if (c.getValore() > max)
				max = c.getValore();
		}
		rank.setLevel(1);
		rank.setValue(max);
	}

	private boolean isColore() {
		return cards.get(0).getSeme() == cards.get(1).getSeme() && cards.get(1).getSeme() == cards.get(2).getSeme()
				&& cards.get(2).getSeme() == cards.get(3).getSeme() && cards.get(3).getSeme() == cards.get(4).getSeme();
	}

	/*
	 * Getter & Setter
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public Rank getRank() {
		return rank;
	}

}
