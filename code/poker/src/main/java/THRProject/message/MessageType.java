package THRProject.message;

public enum MessageType {
	PLAYER_JOIN, // primo step dei client, invio del player al server
	CLIENT_ID, // il server invia il clientId al client
	START_GAME, // il server invia il primo game ai client
	INVALID_ACTION, // il server avvisa il client che l'azione non è valida
	VALID_ACTION, // il server avvisa il client che l'azione è valida
	UPDATE, // il server invia il game aggiornato al client
	INVITO, // il client invia l'invito al server
	
	APRI, // il client invia l'apertura al server
	PASSA, //
	CAMBIO, // il client invia il cambio carte al server
	SERVITO, // il client invia il servito al server
	PUNTA, //
	VEDI, //
	FOLD, // il client lascia la mano attuale
	READY, // il client invia il ready al server per la giocare prossima mano
	QUIT // messaggio che invia il client al server per uscire dalla partita
}
