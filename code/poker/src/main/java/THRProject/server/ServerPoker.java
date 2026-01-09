package THRProject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import THRProject.poker.Game;
import THRProject.poker.Player;

public class ServerPoker {

	private static final int PORT = 443;
	private static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
	private static ServerPoker server;
	private Game game = new Game();
	// private Player currentTurn;

	private ServerPoker() {
	}

	/*
	 * Fa partire il server
	 */
	public void startServer() {

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {

			while (true) {
				System.out.println("In attesa di client...");
				Socket clientSocket = serverSocket.accept(); // accetta nuovo client
				System.out.println("Nuovo Client connesso: " + clientSocket.getInetAddress());

				ClientHandler handler = new ClientHandler(clientSocket);
				clientHandlers.add(handler);
				new Thread(handler).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Fa partire la partita
	 */
	private void startGame(ArrayList<Player> players) {
		game = new Game();
		creaTurnazione(players);

		// TO DO
	}

	/*
	 * Crea la turnazione del gioco, mischiando i player
	 */
	private void creaTurnazione(ArrayList<Player> players) {
		Collections.shuffle(players);
		game.setTurnazione(players);
		game.setTable(players);
	}

	// invia messaggio a tutti i client
	public static void broadcast(Object msg) {
		synchronized (clientHandlers) {
			for (ClientHandler c : clientHandlers) {
				c.sendMessage(msg);
			}
		}
	}

	/*
	 * Getter & Setter
	 */
	public static ServerPoker getServerPoker() {
		if (server == null)
			server = new ServerPoker();

		return server;
	}

}
