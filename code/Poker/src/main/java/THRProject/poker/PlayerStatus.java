package THRProject.poker;

import java.io.Serializable;

public class PlayerStatus implements Serializable {

	private int fiches;
	private int lastBet;
	private boolean fold; // indica se il player ha foldato
	private boolean open; // indica se il player ha aperto
	private boolean debt; // indica se il player è in debito con il piatto
	private boolean pass; // indica se il giocatore ha passato durante la fase di apertura
	private boolean end; // indica se il giocatore ha terminato la fase di corrente

	public PlayerStatus() {
		fold = false;
		open = false;
		debt = false;
		pass = false;
	}

	public void checkDebt(int maxBet) {
		if(lastBet < maxBet)
			debt = true;
	}
	
	public void resetStatus() {
		lastBet = 0;
		fold = false;
		open = false;
		debt = false;
		pass = false;
		end = false;
	}

	/*
	 * Getter & Setter
	 */
	public int getFiches() {
		return fiches;
	}

	public void setFiches(int fiches) {
		this.fiches = fiches;
	}

	public int getLastBet() {
		return lastBet;
	}

	public void setLastBet(int lastBet) {
		this.lastBet = lastBet;
	}

	public boolean isFold() {
		return fold;
	}

	public void setFold(boolean fold) {
		this.fold = fold;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isDebt() {
		return debt;
	}

	public void setDebt(boolean debt) {
		this.debt = debt;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

}
