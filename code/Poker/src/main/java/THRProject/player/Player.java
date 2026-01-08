package THRProject.player;

import THRProject.poker.Carta;

public class Player {
	private String userName;
	private String password;
	private int fiches;
	private Carta [] carte;

	public Player(String userName, String password, int fiches, Carta[] carte) {
		this.userName = userName;
		this.password = password;
		this.fiches = fiches;
		this.carte = carte;
	}
	
	public void creaPartita(){
		
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
