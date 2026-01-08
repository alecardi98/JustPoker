package THRProject.server;
import java.util.ArrayList;
import java.util.Collections;

import THRProject.poker.Game;
import player.Player;

public class ServerPoker {
	
	private static ServerPoker server;
	private Game game;
	
	
	private ServerPoker() {}
	
	
	public void startServer() {
		
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		//il server ascolta i giocatori che si vogliono unire
		//Player player = new Player();
		
		players.add(player);
		
		
	}
	
	
	public void startGame(ArrayList<Player> players) {
		game = new Game();
		game.setTurnazione(Collections.shuffle(players));
		
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
