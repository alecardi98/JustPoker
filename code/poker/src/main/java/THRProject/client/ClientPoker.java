package THRProject.client;
import THRProject.player.Player;
import THRProject.poker.Game;

public class ClientPoker {
	
	private Game game;
	private Player player;
	private static ClientPoker client;
	
	
	private ClientPoker() {}
	
	
	public static ClientPoker getClientPoker() {
		if(client == null)
			client = new ClientPoker();
		
		return client;
	}
	
}
