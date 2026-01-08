package THRProject.poker;
import java.util.ArrayList;

import player.Player;

/*
 * Classe che rappresenta lo stato del gioco
*/
public class Game {

	private ArrayList<Player> turnazione; //rappresenta i player nell'ordine di gioco
	private int piatto; //valore totale scommesso dai giocatori
	
	
	public Game() {
		turnazione = new ArrayList<Player>();
		piatto = 0;
	}

	
	/*
	 * Getter & Setter 
	 */
	public ArrayList<Player> getTurnazione() {
		return turnazione;
	}


	public void setTurnazione(ArrayList<Player> turnazione) {
		this.turnazione = turnazione;
	}


	public int getPiatto() {
		return piatto;
	}


	public void setPiatto(int piatto) {
		this.piatto = piatto;
	}
	
}
