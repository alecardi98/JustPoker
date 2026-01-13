package THRProject.message;

public enum MessageType {
	PLAYER_JOIN, // primo step dei client, invio del player al server
	CLIENT_ID, // il server invia il clientId al client
	START_GAME // messaggio che invia il primo game ai client
}
