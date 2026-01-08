package THRProject.poker;

public class Carta {
	private Seme seme ;
	private int valore;
	
	public Carta(Seme seme, int valore) {
		this.seme = seme;
		this.valore = valore;
	}
	
	public int getValore() {
		return valore;
	}
	public void setValore(int valore) {
		this.valore = valore;
	}
	public Seme getSeme() {
		return seme;
	}
	public void setSeme(Seme seme) {
		this.seme = seme;
	}
}
