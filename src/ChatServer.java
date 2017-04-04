import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private static final int portNumber = 1234;
	private static final String localHost = "localhost";

	//socket to recieve connection request
	private static ServerSocket serverSocket = null;
	//socket to communicate with client
	private static Socket clientSocket = null;
	
	//chat server can dynamically accept new users
	public static int chatroomCapacity = 10;
	private static final ClientThread[] clients = new ClientThread[chatroomCapacity];
	
	
	public static void main(String[] args) {
		Utils.ClearTerminal();
		Utils.PrintUILine();
		System.out.println("Server "+localHost+" on port "+portNumber +" opened.");
		System.out.println("Server is open for clients to join!");
		Utils.PrintUILine();
		//open server socket so that clients can connect to it
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true){
			try {
				//keep listening for new clients to join the server
				clientSocket = serverSocket.accept();
				for (int i = 0; i < chatroomCapacity + 1; i++) {
					if (clients[i] == null){
						clients[i] = new ClientThread(clientSocket, clients);
						clients[i].start();
						System.out.println("> User "+i+" has joined the server.");
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}
}
