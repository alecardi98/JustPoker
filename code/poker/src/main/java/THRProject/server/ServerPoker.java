package THRProject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import THRProject.poker.Game;

public class ServerPoker {

	private static ServerPoker server;
	private static final int PORT = 443;

	private static final int PLAYERSNUM = 2;
	private List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
	private Game game;

	private ServerPoker() {
	}

	/*
	 * Fa partire il server
	 */
	public void startServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server avviato sulla porta " + PORT);

			// attesa unione giocatori
			do {
				System.out.println("In attesa di client...\n");
				Socket clientSocket = serverSocket.accept(); // accetta nuovo client
				System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress() + "\n");

				ClientHandler handler = new ClientHandler(clientSocket);
				System.out.println("Creato Handler per Client " + handler.getClientId());
				clientHandlers.add(handler);
				new Thread(handler).start();

			} while (clientHandlers.size() < PLAYERSNUM); // giocano solo PLAYERSNUM giocatori

		} catch (IOException e) {
			System.err.println("ERRORE! Il server non riesce a creare il socket.\n");
			e.printStackTrace();
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// TO DO
		startGame();
		while(true) {
			
		}
	}

	/*
	 * Fa partire la partita
	 */
	private void startGame() {
		game = new Game();
	}

	/*
	 * Metodo con il quale il server cede il turno al giocatore successivo: gli ID
	 * dei client vengono dati in modo incrementale in base al primo che arriva al
	 * tavolo e questo ordine sarà anche quello di gioco
	 */
	private void nextTurn() {
		if (game.getCurrentTurn() == getPlayersnum() - 1) {
			game.setCurrentTurn(0);
		} else {
			game.setCurrentTurn(game.getCurrentTurn() + 1);
		}
	}

	/*
	 * Invia messaggio a tutti i client
	 */
	public void broadcast(Object msg) {
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

	public List<ClientHandler> getClientHandlers() {
		return clientHandlers;
	}

	public void setClientHandlers(List<ClientHandler> clientHandlers) {
		this.clientHandlers = clientHandlers;
	}

	public static int getPlayersnum() {
		return PLAYERSNUM;
	}

}
