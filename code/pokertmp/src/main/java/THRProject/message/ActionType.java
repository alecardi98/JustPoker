package THRProject.message;

public enum ActionType implements MessageType{
	INVITO, // il client invia l'invito al server
	PASSA, // il client invia il passa al server
	APRI, // il client invia l'apertura al server
	CAMBIO, // il client invia il cambio carte al server
	SERVITO, // il client invia il servito al server
	PUNTA, // il client invia la puntata al server
	FOLD, // il client lascia la mano attuale
}
