package THRProject.poker;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	private ArrayList<Card> cards; // carte effettivamente usate
	private ArrayList<Card> sample; // variabile per non generare ogni volta il mazzo da zero

	public Deck() {
		createSample();
		resetDeck();
	}

	/*
	 * Metodo che genera le carte all'interno del deck
	 */
	public void createSample() {
		sample = new ArrayList<Card>();
		for (int i = 7; i <= 13; i++) {
			sample.add(new Card(Suit.CUORI,i));
		}
		sample.add(new Card(Suit.CUORI,1));
		
		for (int i = 7; i <= 13; i++) {
			sample.add(new Card(Suit.QUADRI,i));
		}
		sample.add(new Card(Suit.QUADRI,1));
		
		for (int i = 7; i <= 13; i++) {
			sample.add(new Card(Suit.FIORI,i));
		}
		sample.add(new Card(Suit.FIORI,1));
		
		for (int i = 7; i <= 13; i++) {
			sample.add(new Card(Suit.PICCHE,i));
		}
		sample.add(new Card(Suit.PICCHE,1));
	}

	/*
	 * Metodo che mescola le carte del deck
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/*
	 * Metodo che riempie nuovamente il mazzo di carte all'inizio della mano
	 */
	public void resetDeck() {
		cards = new ArrayList<Card>(sample);
	}

	/*
	 * Getter & Setter
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

}
