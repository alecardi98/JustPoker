package THRProject.game;

public enum GamePhase {
	INVITO, // fase iniziale nel quale tutti devono puntare
	APERTURA, // dopo l'invito si può aprire
	ACCOMODO, // dopo l'apertura si possono cambiare le carte
	PUNTATA, // dopo l'accomodo si può puntare
	END, // fase in cui si può decidere di uscire dal server
	ENDPASS // fase raggiunta solo se tutti passano durante l'apertura
}
