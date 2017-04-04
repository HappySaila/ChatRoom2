import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {
	private static final int portNumber = 1234;
	private static final String localHost = "localhost";
	//sockets
	private static ServerSocket serverSocket = null;
	private static ChatClient clientSocket = null;
	
	//chat server can dynamically accept new users
	private static final ArrayList<ChatClient> threads = new ArrayList<ChatClient>();
	
	
	public static void main(String[] args) {
		
	}
}
