package THRProject.message;

public enum ControlType implements MessageType{
	PLAYER_JOIN, // primo step dei client, invio del player al server
	CLIENT_ID, // il server invia il clientId al client
	LOGIN,	// viene confrontata la coppia username password nel database
	REGISTER, //viene creata una tupla con l'utente nuovo
	START_GAME, // il server invia il primo game ai client
	INVALID_ACTION, // il server avvisa il client che l'azione non è valida
	VALID_ACTION, // il server avvisa il client che l'azione è valida
	UPDATE, // il server invia il game aggiornato al client
	READY, // il client invia il ready al server per giocare la prossima mano
	QUIT, // il client il quit al server per uscire dalla partita
	WINNER, // il server invia al client il risultato di vittoria
	LOSER, // il server invia al client il risultato di sconfitta
	ENDGAME // il server invia al client la bancarotta
}
