package THRProject.server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import THRProject.card.model.Card;
import THRProject.card.model.Suit;
import THRProject.game.Game;
import THRProject.game.GamePhase;
import THRProject.player.Player;

class ServerTest {
	Logger logger = LogManager.getLogger("debug");

	@Test
	void testFlow1() {
		Server server = Server.getServer();
		Game game;

		ConcurrentHashMap<Integer, ClientHandler> clientHandlers = new ConcurrentHashMap<Integer, ClientHandler>();
		server.setClientHandlers(clientHandlers);

		try {
			for (int i = 0; i < 4; i++) {
				Socket fake = new FakeSocketSafe();
				ClientHandler ch = new ClientHandler(fake, i);
				clientHandlers.put(i, ch);
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
		p1.getStatus().setFiches(fiches1);
		p1.getHand().getCards().add(new Card(Suit.CUORI, 7));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 8));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 9));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 10));
		p1.getHand().getCards().add(new Card(Suit.CUORI, 12));

		Player p2 = new Player("prova", "prova");
		int fiches2 = 25; // all-in invito
		p2.getStatus().setFiches(fiches2);
		p2.getHand().getCards().add(new Card(Suit.FIORI, 7));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 8));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 9));
		p2.getHand().getCards().add(new Card(Suit.FIORI, 10));
		p2.getHand().getCards().add(new Card(Suit.PICCHE, 12));

		Player p3 = new Player("prova", "prova");
		int fiches3 = 1000;
		p3.getStatus().setFiches(fiches3);
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 7));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 8));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 9));
		p3.getHand().getCards().add(new Card(Suit.PICCHE, 11));
		p3.getHand().getCards().add(new Card(Suit.QUADRI, 11));

		Player p4 = new Player("prova", "prova");
		int fiches4 = 1000;
		p4.getStatus().setFiches(fiches4);
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 7));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 8));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 9));
		p4.getHand().getCards().add(new Card(Suit.QUADRI, 12));
		p4.getHand().getCards().add(new Card(Suit.PICCHE, 14));

		playersAll.put(0, p1);
		playersAll.put(1, p2);
		playersAll.put(2, p3);
		playersAll.put(3, p4);

		logger.info("INIZIO FASE DI INVITO");
		server.checkInvito(0);
		server.checkInvito(2); // fuori turno
		server.checkInvito(1); // all-in
		server.checkInvito(1); // doppio
		server.checkInvito(2);
		server.checkInvito(3);

		assertEquals(975, game.getPlayers().get(0).getStatus().getFiches()); // fold
		assertEquals(0, game.getPlayers().get(1).getStatus().getFiches());// all-in
		assertEquals(975, game.getPlayers().get(2).getStatus().getFiches());
		assertEquals(975, game.getPlayers().get(3).getStatus().getFiches());

		assertEquals(GamePhase.APERTURA, game.getPhase());
		assertEquals(100, game.getPot().getTotal());
		logger.info("FINE FASE DI INVITO");

		logger.info("INIZIO FASE DI APERTURA");
		server.checkPassa(0);
		server.checkPassa(1);
		server.checkPassa(2);
		server.checkPassa(3);
		logger.info("FINE FASE DI APERTURA");

		server.countReady(0);
		server.countReady(1);
		server.getClientHandlers().get(2).cleanUp();
		server.checkStart();

		server.getClientHandlers().get(3).cleanUp();
		server.checkStart();
		// si ricomincia dopo che tutti hanno passato
	}
}
