package THRProject.poker;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
	
	private final String username; 
	private String password;
	private boolean isActive; // indica se il player è attivo al tavolo (non ha foldato)
	private boolean quit; //indica quando il giocatore ha lasciato la partita
	private int fiches; 
	private ArrayList<Card> hand; 

	public Player(String userName, String password) {
		this.username = userName;
		this.password = password;
		isActive = true;
		quit = false;
		hand = new ArrayList<Card>();
	}

	public void creaPartita() {
		//METODO CHE NON SERVE! IL SERVER E' GIA' UNA PARTITA! CANCELLARE ANCHE DOCUMENTAZIONE
	}

	public void partecipaPartita() {
		//METODO CHE NON SERVE! IL SERVER E' GIA' UNA PARTITA! CANCELLARE ANCHE DOCUMENTAZIONE
	}

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

	public ArrayList<Card> getHand() {
		return hand;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isQuit() {
		return quit;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

}
