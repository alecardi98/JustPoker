package THRProject.poker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

	private final String username;
	private String password;
	
	private PlayerStatus status;
	private int fiches;
	private ArrayList<Card> hand;

	public Player(String userName, String password) {
		this.username = userName;
		this.password = password;
		hand = new ArrayList<Card>();
		status = new PlayerStatus();
	}

	/*
	 * Metodo che permette di abbandonare la partita dalla fine della mano. Il
	 * client non potrà più connettersi poichè il server non sarà aperto a nuove
	 * connessioni durante la partita
	 */
	public void esciPartita() {

	}

	public void partecipaInvito(int invito) {
		if (invito >= 5 && invito <= 100) { // puntata d'invito tra 5 e 100
			punta(invito);
		} else {
			System.out.println("errore");
		}
	}

	public void nonPartecipa() {
		// turno disabilitato per questa mano
	}

	public void punta(int puntata) { // metodo per effettuare una puntata
		if (fiches >= puntata) {
			fiches -= puntata;
		} else {
			System.out.println("errore");
		}

	}

	public void apri(int puntata) {
		punta(puntata);
	}

	public void passa() {

	}

	public void lascia() {

	}

	public void vedi() {

	}

	public void cambio(int scelta) {

	}

	public void servito() {

	}

	@Override
	public String toString() {
		return username;
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

	public int getFiches() {
		return fiches;
	}

	public void setFiches(int fiches) {
		this.fiches = fiches;
	}

	public List<Card> getHand() {
		return hand;
	}

}
