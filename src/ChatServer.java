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
	private static final ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	
	
	public static void main(String[] args) {
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
				
				for (int i = 0; i < clients.size(); i++) {
					if (clients.get(i) == null){
						clients.set(i, new ClientThread(clientSocket, clients));
						clients.get(i).start();
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
