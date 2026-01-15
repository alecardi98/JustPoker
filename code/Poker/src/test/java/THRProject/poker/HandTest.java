package THRProject.poker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class HandTest {
	
	@Test
	void cartaAltaRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,9));
		cards.add(new Card(Suit.FIORI,11));
		cards.add(new Card(Suit.CUORI,13));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(1,h1.getRank().getLevel());
		assertEquals(13,h1.getRank().getValue());
	}
	
	@Test
	void coppiaRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,7));
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.FIORI,13));
		cards.add(new Card(Suit.CUORI,14));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(2,h1.getRank().getLevel());
		assertEquals(14,h1.getRank().getValue());
	}

	@Test
	void doppiaCoppiaRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,7));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,14));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(3,h1.getRank().getLevel());
		assertEquals(30,h1.getRank().getValue());
	}
	
	@Test
	void trisRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.FIORI,13));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.QUADRI,13));
		cards.add(new Card(Suit.CUORI,14));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(4,h1.getRank().getLevel());
		assertEquals(39,h1.getRank().getValue());
	}
	
	@Test
	void scalaRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,9));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.QUADRI,11));
		cards.add(new Card(Suit.CUORI,10));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(5,h1.getRank().getLevel());
		assertEquals(45,h1.getRank().getValue());
	}
	
	@Test
	void coloreRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.CUORI,11));
		cards.add(new Card(Suit.CUORI,14));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(6,h1.getRank().getLevel());
		assertEquals(58,h1.getRank().getValue());
	}
	
	@Test
	void fullRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.PICCHE,8));
		cards.add(new Card(Suit.PICCHE,12));
		cards.add(new Card(Suit.QUADRI,8));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,12));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(7,h1.getRank().getLevel());
		assertEquals(48,h1.getRank().getValue());
	}
	
	@Test
	void pokerRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.QUADRI,12));
		cards.add(new Card(Suit.FIORI,12));
		cards.add(new Card(Suit.PICCHE,12));
		cards.add(new Card(Suit.CUORI,14));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(8,h1.getRank().getLevel());
		assertEquals(48,h1.getRank().getValue());
	}
	
	@Test
	void scalaColoreRankTest() {
		Hand h1 = new Hand();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.CUORI,14));
		cards.add(new Card(Suit.CUORI,11));
		cards.add(new Card(Suit.CUORI,10));
		h1.setCards(cards);
		
		h1.checkRank();
		
		assertEquals(9,h1.getRank().getLevel());
		assertEquals(60,h1.getRank().getValue());
	}
	
}
