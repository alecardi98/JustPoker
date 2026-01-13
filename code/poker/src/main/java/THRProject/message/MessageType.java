package THRProject.message;

public enum MessageType {
	PLAYER_JOIN, // primo step dei client, invio del player al server
	CLIENT_ID, // il server invia il clientId al client
	START_GAME, // il server invia il primo game ai client
	YOUR_TURN, // il serve avvisa il client che è il suo turno

	QUIT // messaggio che invia il client al server per uscire dalla partita
}
