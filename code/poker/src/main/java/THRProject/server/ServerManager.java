package THRProject.server;

public class ServerManager implements ServerObserver {

	private Server server;
	
	public ServerManager() {
		server = Server.getServer();
	}

	public void startManager() {
		server.startServer();
	}

	private void resetServer() {
		server.reset();
	}

	@Override
	public void onEndGame() {
		resetServer();
		startManager();
	}

}
