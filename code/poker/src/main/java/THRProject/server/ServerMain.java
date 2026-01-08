package THRProject.server;

public class ServerMain {

	public static void main(String[] args) {

		ServerPoker server = ServerPoker.getServerPoker();
		server.startServer();

	}

}
