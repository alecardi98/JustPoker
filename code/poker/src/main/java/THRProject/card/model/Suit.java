package THRProject.card.model;


import java.io.Serializable;

/**
 * Enum per rappresentare i semi delle carte
 */
public enum Suit implements Serializable {
	CUORI("♥"),
	QUADRI("♦"),
	FIORI("♣"),
	PICCHE("♠");
	
	private final String symbol;
	
	Suit(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
}
