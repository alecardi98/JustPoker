package THRProject.poker;
import java.util.ArrayList;

import THRProject.player.Player;

/*
 * Classe che rappresenta lo stato del gioco
*/
public class Game {

	private ArrayList<Player> turnazione; //rappresenta i player nell'ordine di gioco
	private ArrayList<Player> table; //rappresenta i player attivi nell'ordine di gioco

	private Player turn; //player a cui tocca
	private int piatto; //valore totale scommesso dai giocatori
	
	
	public Game() {
		turnazione = new ArrayList<Player>();
		table = new ArrayList<Player>();
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


	public void setTable(ArrayList<Player> table) {
		this.table = table;
		
	}
	
	
	public ArrayList<Player> getTable() {
		return table;
	}


	public Player getTurn() {
		return turn;
	}


	public void setTurn(Player turn) {
		this.turn = turn;
	}


	
	
	
}
