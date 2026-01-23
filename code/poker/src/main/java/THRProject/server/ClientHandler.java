package THRProject.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import THRProject.card.model.Card;
import THRProject.message.ActionType;
import THRProject.message.Communicator;
import THRProject.message.ControlType;
import THRProject.message.Message;
import THRProject.player.Player;

/*
 * Classe che rappresenta il thread del server che gestirà la connessione con un client 
 */
class ClientHandler implements Runnable, Communicator {

	private static final Logger logger = LogManager.getLogger(ClientHandler.class);
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private final int clientId; // client al quale è connesso il clienthandler (funge anche da id turno)

	public ClientHandler(Socket clientSocket, int clientId) {
		this.clientId = clientId;
		socket = clientSocket;
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
			this.in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendMessage(new Message(ControlType.CLIENT_ID, clientId)); // invio clientId
	}

	/*
	 * Metodo per ricevere oggetti dai client: ogni ClientHandler sarà associato ad
	 * un client, e si occuperà di gestire il flusso di messaggi
	 */
	@Override
	public void run() {
		try {
			Object obj;
			while (true) {
				obj = in.readObject();
				Message msg = (Message) obj;

				if (msg.getType() instanceof ActionType action) {
					switch (action) {
					case INVITO -> Server.getServer().checkInvito(clientId);
					case APRI -> Server.getServer().checkApertura(clientId, (Integer) msg.getData());
					case PASSA -> Server.getServer().checkPassa(clientId);
					case CAMBIO -> Server.getServer().checkCambio(clientId, (ArrayList<Card>) msg.getData());
					case SERVITO -> Server.getServer().checkServito(clientId);
					case PUNTA -> Server.getServer().checkPuntata(clientId, (Integer) msg.getData());
					case FOLD -> Server.getServer().checkFold(clientId);
					default -> logger.error("ERRORE! Messaggio sconosciuto.");
					}
				}

				if (msg.getType() instanceof ControlType control) {
					switch (control) {
					case LOGIN -> Server.getServer().handleLogin(clientId, (Player) msg.getData());
					case REGISTER -> Server.getServer().handleRegister(clientId, (Player) msg.getData());
					case READY -> Server.getServer().countReady(clientId);
					case QUIT -> {
						cleanUp();
						logger.info("Quit Client " + clientId + " registrato.");
						Server.getServer().checkStart();
						return;
					}
					default -> logger.error("ERRORE! Messaggio sconosciuto.");
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			logger.error("ERRORE! Comunicazione con il Client " + clientId + " persa.");
			cleanUp();
		}
	}

	/*
	 * Metodo per chiudere correttamente la connessione con il client - rimuove il
	 * player - chiude il clientHandler
	 */
	public void cleanUp() {
		Server.getServer().getGame().removePlayer(clientId);
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Server.getServer().removeClient(clientId);
	}

	@Override
	public void sendMessage(Object msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Getter & Setter
	 */
	public int getClientId() {
		return clientId;
	}

}
