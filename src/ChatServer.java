import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	private static final int portNumber = 1234;
	private static final String localHost = "localhost";
	//sockets
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	
	//chat server can dynamically accept new users
	private static final ClientThread[] clients = new ClientThread[10];
	
	
	public static void main(String[] args) {
		System.out.println("Server "+localHost+" on port "+portNumber +" opened.");
		//open server socket
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			try {
				clientSocket = serverSocket.accept();
				System.out.println(clientSocket);
				for (int i = 0; i < 10 + 1; i++) {
					if (clients[i] == null){
						clients[i] = new ClientThread(clientSocket, clients);
						clients[i].start();
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
}
