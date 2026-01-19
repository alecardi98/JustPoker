package THRProject.card.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import THRProject.card.logic.Hand;
import THRProject.card.model.Card;
import THRProject.card.model.Suit;

class HandTest {
	
	ArrayList<Card> cards;
	Hand hand;
	
	@BeforeEach
	void setupAll() {
		hand = new Hand();
		cards = new ArrayList<Card>();
	}
	
	@Test
	void cartaAltaRankTest() {
		
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,9));
		cards.add(new Card(Suit.FIORI,11));
		cards.add(new Card(Suit.CUORI,13));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(1,hand.getRank().getLevel());
		assertEquals(13,hand.getRank().getValue());
	}
	
	@Test
	void coppiaRankTest() {
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,7));
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.FIORI,13));
		cards.add(new Card(Suit.CUORI,14));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(2,hand.getRank().getLevel());
		assertEquals(14,hand.getRank().getValue());
	}

	@Test
	void doppiaCoppiaRankTest() {
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,7));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,14));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(3,hand.getRank().getLevel());
		assertEquals(30,hand.getRank().getValue());
	}
	
	@Test
	void trisRankTest() {
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.FIORI,13));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.QUADRI,13));
		cards.add(new Card(Suit.CUORI,14));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(4,hand.getRank().getLevel());
		assertEquals(39,hand.getRank().getValue());
	}
	
	@Test
	void scalaRankTest() {
		cards.add(new Card(Suit.CUORI,7));
		cards.add(new Card(Suit.FIORI,9));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.QUADRI,11));
		cards.add(new Card(Suit.CUORI,10));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(5,hand.getRank().getLevel());
		assertEquals(45,hand.getRank().getValue());
	}
	
	@Test
	void coloreRankTest() {
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.CUORI,8));
		cards.add(new Card(Suit.CUORI,11));
		cards.add(new Card(Suit.CUORI,14));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(6,hand.getRank().getLevel());
		assertEquals(58,hand.getRank().getValue());
	}
	
	@Test
	void fullRankTest() {
		cards.add(new Card(Suit.PICCHE,8));
		cards.add(new Card(Suit.PICCHE,12));
		cards.add(new Card(Suit.QUADRI,8));
		cards.add(new Card(Suit.FIORI,8));
		cards.add(new Card(Suit.CUORI,12));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(7,hand.getRank().getLevel());
		assertEquals(48,hand.getRank().getValue());
	}
	
	@Test
	void pokerRankTest() {
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.QUADRI,12));
		cards.add(new Card(Suit.FIORI,12));
		cards.add(new Card(Suit.PICCHE,12));
		cards.add(new Card(Suit.CUORI,14));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(8,hand.getRank().getLevel());
		assertEquals(48,hand.getRank().getValue());
	}
	
	@Test
	void scalaColoreRankTest() {
		cards.add(new Card(Suit.CUORI,12));
		cards.add(new Card(Suit.CUORI,13));
		cards.add(new Card(Suit.CUORI,14));
		cards.add(new Card(Suit.CUORI,11));
		cards.add(new Card(Suit.CUORI,10));
		hand.setCards(cards);
		
		hand.checkRank();
		
		assertEquals(9,hand.getRank().getLevel());
		assertEquals(60,hand.getRank().getValue());
	}
	
}
