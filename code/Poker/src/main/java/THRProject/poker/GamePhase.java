package THRProject.poker;

public enum GamePhase {
	INVITO, //fase iniziale nel quale tutti devono puntare
	APERTURA, //dopo l'invito si può aprire
	ACCOMODO, //dopo l'apertura si possono cambiare le carte
	PUNTATE, //dopo l'accomodo si può puntare
	SHOWDOWN //fase finale di calcolo del vincitore
}
