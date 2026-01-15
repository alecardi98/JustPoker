package THRProject.poker;

import java.io.Serializable;

public class PlayerStatus implements Serializable {

	private int fiches;
	private int totalBet;
	private boolean fold; // indica se il player ha foldato
	private boolean open; // indica se il player ha aperto (serve solo per l'apertura)
	private boolean pass; // indica se il giocatore ha passato (serve solo per l'apertura)
	private boolean end; // indica se il giocatore ha terminato il proprio turno nella fase corrente

	public PlayerStatus() {
		totalBet = 0;
		fold = false;
		open = false;
		pass = false;
		end = false;
	}

	/*
	 * Metodo che serve per resettare tra una fase e l'altra le fiches puntate nella
	 * fase precedente
	 */
	public void resetStatus() {
		totalBet = 0;
		resetEnd();
	}

	/*
	 * Metodo che rimette in gioco il player nella fase, poichè qualcuno ha puntato
	 * di più ed è quindi in debito
	 */
	public void resetEnd() {
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

	public int getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(int totalBet) {
		this.totalBet = totalBet;
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
