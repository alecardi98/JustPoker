package THRProject.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

import THRProject.player.Player;

class GameTest {

	@Test
	void testCheckFirstTurn() {
		Game game = new Game(10);
		ConcurrentHashMap<Integer, Player> playersAll = new ConcurrentHashMap<Integer, Player>();
		game.setPlayers(playersAll);
		game.setCurrentTurn(5);

		// player 1 ha foldato
		Player p1 = new Player("prova", "prova");
		p1.getStatus().setFold(true);

		// player 2 è attivo
		Player p2 = new Player("prova", "prova");
		p2.getStatus().setFold(false);

		// player 3 è attivo
		Player p3 = new Player("prova", "prova");
		p3.getStatus().setFold(false);

		playersAll.put(1, p1);
		playersAll.put(5, p2);
		playersAll.put(7, p3);
		game.checkFirstTurn();

		assertEquals(5, game.getCurrentTurn());
	}

	@Test
	void testNextTurnLastToFirst() {
		Game game = new Game(10);
		ConcurrentHashMap<Integer, Player> playersAll = new ConcurrentHashMap<Integer, Player>();
		game.setPlayers(playersAll);
		game.setCurrentTurn(5);

		// player 1 ha foldato
		Player p1 = new Player("prova", "prova");
		p1.getStatus().setFold(true);

		// player 2 è attivo
		Player p2 = new Player("prova", "prova");
		p2.getStatus().setFold(false);

		// player 3 è attivo
		Player p3 = new Player("prova", "prova");
		p3.getStatus().setFold(false);

		playersAll.put(1, p1);
		playersAll.put(3, p2);
		playersAll.put(5, p3);
		game.nextTurn();

		/*
		 * Era il turno di p3, quindi toccherebbe p1, ma ha foldato, quindi tocca p2 Si
		 * controlla anche il passaggio dall'ultimo del giro al primo con id disgiunti
		 */
		assertEquals(3, game.getCurrentTurn());
	}

	@Test
	void testNextTurnCurrentFold() {
		Game game = new Game(10);
		ConcurrentHashMap<Integer, Player> playersAll = new ConcurrentHashMap<Integer, Player>();
		game.setPlayers(playersAll);
		game.setCurrentTurn(3);

		Player p1 = new Player("prova", "prova");
		p1.getStatus().setFold(false);

		Player p2 = new Player("prova", "prova");
		p2.getStatus().setFold(true);

		Player p3 = new Player("prova", "prova");
		p3.getStatus().setFold(false);

		Player p4 = new Player("prova", "prova");
		p4.getStatus().setFold(false);

		playersAll.put(1, p1);
		playersAll.put(3, p2);
		playersAll.put(5, p3);
		playersAll.put(9, p4);
		game.nextTurn();

		/*
		 * Controlla che l'attuale che ha foldato non cambi nulla
		 */
		assertEquals(5, game.getCurrentTurn());
	}

}
