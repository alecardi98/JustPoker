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
		game.setCurrentTurn(0);

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
		playersAll.put(2, p3);
		game.checkFirstTurn();

		assertEquals(2, game.getCurrentTurn());
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
		game.setCurrentTurn(2);

		Player p1 = new Player("prova", "prova");
		p1.getStatus().setFold(false);

		Player p2 = new Player("prova", "prova");
		p2.getStatus().setFold(true);

		Player p3 = new Player("prova", "prova");
		p3.getStatus().setFold(false);

		Player p4 = new Player("prova", "prova");
		p4.getStatus().setFold(false);

		playersAll.put(1, p1);
		playersAll.put(2, p2);
		playersAll.put(3, p3);
		playersAll.put(4, p4);
		game.nextTurn();

		/*
		 * Controlla che l'attuale che ha foldato non cambi nulla
		 */
		assertEquals(3, game.getCurrentTurn());
	}

	@Test
	void testSplitPot() {
		Game game = new Game(10);
		ConcurrentHashMap<Integer, Player> playersAll = new ConcurrentHashMap<Integer, Player>();
		game.setPlayers(playersAll);
		game.getPot().setTotal(1000);

		Player p1 = new Player("prova", "prova");
		p1.getStatus().setFold(false);
		p1.getStatus().setFiches(0);

		Player p2 = new Player("prova", "prova");
		p2.getStatus().setFold(false);
		p2.getStatus().setFiches(0);

		Player p3 = new Player("prova", "prova");
		p3.getStatus().setFold(true);
		p3.getStatus().setFiches(0);

		Player p4 = new Player("prova", "prova");
		p4.getStatus().setFold(false);
		p4.getStatus().setFiches(0);

		playersAll.put(1, p1);
		playersAll.put(2, p2);
		playersAll.put(3, p3);
		playersAll.put(4, p4);
		game.splitPot();

		assertEquals(1000/3, game.getPlayers().get(1).getStatus().getFiches());
		assertEquals(1000/3, game.getPlayers().get(2).getStatus().getFiches());
		assertEquals(1000/3, game.getPlayers().get(4).getStatus().getFiches());
	}

}
