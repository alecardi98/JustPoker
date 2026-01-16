package THRProject.poker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import THRProject.message.Message;
import THRProject.message.MessageType;

public class Player implements Serializable {

	private final String username;
	private String password;
	private PlayerStatus status;
	private Hand hand;

	public Player(String userName, String password) {
		this.username = userName;
		this.password = password;
		hand = new Hand();
		status = new PlayerStatus();
	}

	/*
	 * Metodo per creare l'invito (fisso a MINBET)
	 */
	public Message invito() {
		return new Message(MessageType.INVITO, null);
	}

	/*
	 * Metodo per creare la puntata
	 */
	public Message punta(int puntata) { // metodo per effettuare una puntata
		return new Message(MessageType.PUNTA, puntata);
	}

	/*
	 * Metodo per creare l'apertura
	 */
	public Message apri(int puntata) {
		return new Message(MessageType.APRI, puntata);
	}

	/*
	 * Metodo per creare il passa
	 */
	public Message passa() {
		return new Message(MessageType.PASSA, null);
	}

	/*
	 * Metodo per creare il lascia
	 */
	public Message lascia() {
		return new Message(MessageType.FOLD, null);
	}

	/*
	 * Metodo per creare il cambio
	 */
	public Message cambio() {
		return new Message(MessageType.CAMBIO, changeCards());
	}

	/*
	 * Metodo per scegliere le carte da cambiare
	 */
	public ArrayList<Card> changeCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		// TO DO scelta carte
		return cards;
	}

	/*
	 * Metodo per creare il servito
	 */
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

	public Hand getHand() {
		return hand;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

}
