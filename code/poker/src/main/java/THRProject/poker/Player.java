package THRProject.poker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import THRProject.message.Message;
import THRProject.message.MessageType;
import THRProject.server.Server;

public class Player implements Serializable {

	private final String username;
	private String password;
	private PlayerStatus status;
	private ArrayList<Card> hand;

	public Player(String userName, String password) {
		this.username = userName;
		this.password = password;
		hand = new ArrayList<Card>();
		status = new PlayerStatus();
	}

	/*
	 * Metodo per creare l'invito (fisso a MINBET)
	 */
	public Message invito() {
		return new Message(MessageType.INVITO, null);
	}

	public Message punta(int puntata) { // metodo per effettuare una puntata
		return new Message(MessageType.PUNTA, puntata);
	}

	public Message apri(int puntata) {
		return new Message(MessageType.APRI, puntata);
	}

	public Message passa() {
		return new Message(MessageType.PASSA, null);
	}

	public Message lascia() {
		return new Message(MessageType.FOLD, null);
	}

	public Message vedi() {
		return new Message(MessageType.VEDI, null);
	}

	public Message cambio(Card[] cards) {
		return new Message(MessageType.CAMBIO, cards);
	}

	public Message servito() {
		return new Message(MessageType.SERVITO, null);
	}

	/*
	 * Getter & Setter
	 */
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Card> getHand() {
		return hand;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

}
