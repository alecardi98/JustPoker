package THRProject.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import THRProject.card.Card;
import THRProject.card.Suit;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.player.Player;

class ServerTest {

	Server server;
	Game game;

	@BeforeEach
	void setupAll() {
		Server.resetInstance();
		server = Server.getServer();
		
		
		ConcurrentHashMap<Integer, ClientHandler> clientHandlers = new ConcurrentHashMap<Integer, ClientHandler>();
		server.setClientHandlers(clientHandlers);
		
		try {
			for(int i = 0; i<4;i++) {
				Socket fake = new FakeSocketSafe();
				ClientHandler ch = new ClientHandler(fake,i);
				clientHandlers.put(i,ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int minBet = 25;
		game = new Game(minBet);
		server.setGame(game);
		ConcurrentHashMap<Integer, Player> playersAll = new ConcurrentHashMap<Integer, Player>();
		game.setPlayers(playersAll);
		game.setCurrentTurn(0);

		Player p1 = new Player("prova", "prova");
		int fiches1 = 1000;
		p1.getStatus().setFold(false);
		p1.getStatus().setFiches(fiches1);
		p1.getHand().getCards().add(new Card(Suit.CUORI, 7));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 8));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 9));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 10));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 12));

		Player p2 = new Player("prova", "prova");
		int fiches2 = 1000;
		p2.getStatus().setFold(false);
		p2.getStatus().setFiches(fiches2);
		p2.getHand().getCards().add(new Card(Suit.FIORI, 7));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 8));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 9));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 10));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 12));

		Player p3 = new Player("prova", "prova");
		int fiches3 = 1000;
		p3.getStatus().setFold(false);
		p3.getStatus().setFiches(fiches3);
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 7));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 8));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 9));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 10));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 12));

		Player p4 = new Player("prova", "prova");
		int fiches4 = 1000;
		p4.getStatus().setFold(false);
		p4.getStatus().setFiches(fiches4);
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 7));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 8));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 9));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 11));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 11));

		playersAll.put(0, p1);
		playersAll.put(1, p2);
		playersAll.put(2, p3);
		playersAll.put(3, p4);

	}

	@Test
	void testCheckInvito() {
		Player foldedPlayer = game.getPlayers().get(0); //player che folda
		game.foldPlayer(foldedPlayer);
		server.checkInvito(0);
		server.checkInvito(2); // fuori turno
		server.checkInvito(1);
		server.checkInvito(1); // doppio
		server.checkInvito(2);
		server.checkInvito(3);
		
		for (Map.Entry<Integer, Player> p : game.getPlayers().entrySet()) {
			if(!p.getValue().getStatus().isFold()) //controllo solo chi non ha foldato
				assertEquals(975,p.getValue().getStatus().getFiches());
			else
				assertEquals(1000,p.getValue().getStatus().getFiches());
		}
		assertEquals(GamePhase.APERTURA,game.getPhase());
		assertEquals(75,game.getPot().getTotal());
	}

	@Test
	void testCheckApertura() {

	}

	@Test
	void testCheckPassa() {

	}

	@Test
	void testCheckCambio() {

	}

	@Test
	void testCheckWinner() {

	}

	@Test
	void testCheckRestart() {

	}

}
