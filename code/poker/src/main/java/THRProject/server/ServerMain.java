package THRProject.server;

public class ServerMain {

	public static void main(String[] args) {

		ServerManager manager = new ServerManager();
		Server.getServer().addObserver(manager);
		manager.startManager();

	}

}
