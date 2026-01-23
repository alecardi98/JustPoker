package THRProject.card.model;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

	public CardComparator() {
		
	}
	
	@Override
	public int compare(Card o1, Card o2) {
		if (o1.getValore() == o2.getValore())
			return 0;
		else if (o1.getValore() > o2.getValore())
			return 1;
		else
			return -1;
	}

}
