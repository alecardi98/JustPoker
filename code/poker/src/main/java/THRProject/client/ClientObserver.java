package THRProject.client;

public interface ClientObserver {
	
	void onLoginResult(boolean success);
	
	void onStart();
		
	void onTornaMenu();
	
	void onGameViewUpdate();
}
