package THRProject.server;
import java.util.ArrayList;
import java.util.Collections;

import THRProject.poker.Game;
import THRProject.player.Player;

public class ServerPoker {
	
	private static ServerPoker server;
	private Game game;
	
	
	private ServerPoker() {}
	
	
	public void startServer() {
		
		//1055164.ddns.net
		
		
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		//il server ascolta i giocatori che si vogliono unire
		//Player player = new Player();
		//players.add(player);
		
		startGame(players);
		
	}
	
	
	public void startGame(ArrayList<Player> players) {
		game = new Game();
		creaTurnazione(players);
		
		
		game.setTurn(players.get(0)); //il turno iniziale è del primo nella lista

		
	}
	
	
	/*
	 * Crea la turnazione del gioco, mischiando i player
	*/
	private void creaTurnazione(ArrayList<Player> players) {
		Collections.shuffle(players);
		game.setTurnazione(players);
		game.setTable(players);
				
		
	}
	
	
	/*
	 * Getter & Setter 
	*/
	public static ServerPoker getServer() {
		if(server == null)
			server = new ServerPoker();
			
		return server;
	}
	
}
