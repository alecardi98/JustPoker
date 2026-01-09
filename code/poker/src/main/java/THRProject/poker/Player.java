package THRProject.poker;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
	private final String userName; // userName è l'identificativo del Player
	private String password;
	private int fiches; //assegnate dal server
	private Carta[] carte; //assegnate dal server

	public Player(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public void creaPartita() {

	}

	public void partecipaPartita() {

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

	/*
	 * Metodo per confrontare Player
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true; // stesso riferimento
		if (o == null || getClass() != o.getClass())
			return false;

		Player player = (Player) o;
		return Objects.equals(userName, player.userName); // confronto solo username
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName); // deve essere coerente con equals
	}

	@Override
	public String toString() {
		return userName;
	}

	/*
	 * Getter & Setter
	 */
	public String getUserName() {
		return userName;
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

	public Carta[] getCarte() {
		return carte;
	}

	public void setCarte(Carta[] carte) {
		this.carte = carte;
	}

}
