package THRProject.client;

public interface ClientObserver {
	
	void onLoginResult(boolean success);

	void onStart();
}
