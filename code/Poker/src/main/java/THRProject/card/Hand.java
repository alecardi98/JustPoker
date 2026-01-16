package THRProject.card;

import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable{

	private ArrayList<Card> cards;
	private Rank rank;

	public Hand() {
		cards = new ArrayList<Card>();
		rank = new Rank();
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

		// ricerca poker
		for (int i = 0; i <= 7; i++) {
			if (counters[i] == 4) {
				rank.setLevel(8);
				rank.setValue((i + 7) * 4);
				return;
			}
		}

		// ricerca tris o full
		for (int i = 0; i <= 7; i++) {
			if (counters[i] == 3) { // tris
				rank.setLevel(4);
				rank.setValue((i + 7) * 3);

				for (int j = 0; j <= 7; j++) {
					if (counters[j] == 2) { // full
						rank.setLevel(7);
						rank.setValue(rank.getValue() + ((j + 7) * 2));
						return;
					}
				}
				return;
			}
		}

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

		// ricerca scala o scala colore
		for (int i = 0; i <= 3; i++) {
			if (counters[i] == counters[i + 1] && counters[i + 1] == counters[i + 2]
					&& counters[i + 1] == counters[i + 2] && counters[i + 2] == counters[i + 3]
					&& counters[i + 3] == counters[i + 4]) { // scala
				rank.setLevel(5);
				rank.setValue(45 + (5 * i));
				if (cards.get(0).getSeme() == cards.get(1).getSeme() && cards.get(1).getSeme() == cards.get(2).getSeme()
						&& cards.get(2).getSeme() == cards.get(3).getSeme()
						&& cards.get(3).getSeme() == cards.get(4).getSeme()) {// scala colore
					rank.setLevel(9);
				}
				return;
			}
		}

		// ricerca colore
		boolean color = false;
		if (cards.get(0).getSeme() == cards.get(1).getSeme() && cards.get(1).getSeme() == cards.get(2).getSeme()
				&& cards.get(2).getSeme() == cards.get(3).getSeme()
				&& cards.get(3).getSeme() == cards.get(4).getSeme()) {
			rank.setLevel(6);

			int sum = 0;
			for (Card c : cards)
				sum = sum + c.getValore();
			rank.setValue(sum);
			return;
		}

		// ricerca carta alta
		int max = 7;
		for (Card c : cards) {
			if (c.getValore() > max)
				max = c.getValore();
		}
		rank.setLevel(1);
		rank.setValue(max);
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
