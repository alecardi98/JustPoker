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
	
	public void partecipaPartita(){
		
	}
	
	public void esciPartita(){
		
	}
	
	public void partecipaInvito(int invito){
		if(invito>=5 && invito<=100) {			//puntata d'invito tra 5 e 100 
			punta(invito);
		}else {
			System.out.println("errore");
		}
	}
	
	public void nonPartecipa(){
												//turno disabilitato per questa mano
	}
	
	public void punta(int puntata){				// metodo per effettuare una puntata
		if(fiches>=puntata) {
			fiches-=puntata;
		}else {
			System.out.println("errore");
		}
			
	}
	
	public void apri(int puntata){
		punta(puntata);
	}
	
	public void passa(){
		
	}
	
	public void lascia(){
		
	}
	
	public void vedi(){
		
	}
	
	public void cambio(int scelta){
		
	}
	
	public void servito(){
		
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
