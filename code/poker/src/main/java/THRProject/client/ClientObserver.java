package THRProject.client;

public interface ClientObserver {
	
	void onLoginResult(boolean success);
	
	void onStart();
	
	void onMessageReceived(String message);
	
	void onTornaMenu();
	
	void onGameViewUpdate();
}
