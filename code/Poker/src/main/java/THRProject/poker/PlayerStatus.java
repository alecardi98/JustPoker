package THRProject.poker;

public class PlayerStatus {

	private boolean fold; // indica se il player ha foldato
	private boolean open; // indica se il player ha aperto
	private boolean debt; // indica se il player è in debito con il piatto
	private boolean pass; // indica se il giocatore ha passato durante la fase di apertura
	

	public PlayerStatus() {
		fold = false;
		open = false;
		debt = false;
		pass = false;
	}

}
