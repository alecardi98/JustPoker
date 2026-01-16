package THRProject.message;

public enum MessageType {
	PLAYER_JOIN, // primo step dei client, invio del player al server
	CLIENT_ID, // il server invia il clientId al client
	START_GAME, // il server invia il primo game ai client
	INVALID_ACTION, // il server avvisa il client che l'azione non è valida
	VALID_ACTION, // il server avvisa il client che l'azione è valida
	UPDATE, // il server invia il game aggiornato al client
	INVITO, // il client invia l'invito al server
	PASSA, // il client invia il passa al server
	APRI, // il client invia l'apertura al server
	CAMBIO, // il client invia il cambio carte al server
	SERVITO, // il client invia il servito al server
	PUNTA, // il client invia la puntata al server
	FOLD, // il client lascia la mano attuale
	READY, // il client invia il ready al server per giocare la prossima mano
	QUIT, // il client il quit al server per uscire dalla partita
	WINNER, // il server invia al client il risultato di vittoria
	LOSER, // il server invia al client il risultato di sconfitta
}
