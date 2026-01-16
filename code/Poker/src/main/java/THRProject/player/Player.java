package THRProject.player;

import java.io.Serializable;
import java.util.ArrayList;

import THRProject.card.Card;
import THRProject.card.Hand;
import THRProject.message.Message;
import THRProject.message.ActionType;

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
		return new Message(ActionType.INVITO, null);
	}

	/*
	 * Metodo per creare la puntata
	 */
	public Message punta(int puntata) { // metodo per effettuare una puntata
		return new Message(ActionType.PUNTA, puntata);
	}

	/*
	 * Metodo per creare l'apertura
	 */
	public Message apri(int puntata) {
		return new Message(ActionType.APRI, puntata);
	}

	/*
	 * Metodo per creare il passa
	 */
	public Message passa() {
		return new Message(ActionType.PASSA, null);
	}

	/*
	 * Metodo per creare il lascia
	 */
	public Message lascia() {
		return new Message(ActionType.FOLD, null);
	}

	/*
	 * Metodo per creare il cambio
	 */
	public Message cambio() {
		return new Message(ActionType.CAMBIO, changeCards());
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
		return new Message(ActionType.SERVITO, null);
	}

	/*
	 * Getter & Setter
	 */
	public Hand getHand() {
		return hand;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

}
