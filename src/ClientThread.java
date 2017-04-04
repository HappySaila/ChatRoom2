import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread{
	private String clientName;
	
	private ClientThread[] clients;
	private Socket clientSocket = null;
	private PrintStream output = null;
	private DataInputStream input = null;
	

	public ClientThread(Socket clientSocket, ClientThread[] clients) {
		this.clientSocket = clientSocket;
		this.clients = clients;
	}
	
	public void run(){
		try {
			//create output and input streams
			//streams will write to another socket in the server
			//the socket has aceess to all other client sockets on the server side
			input = new DataInputStream(clientSocket.getInputStream());
			output = new PrintStream(clientSocket.getOutputStream());
			while(true){
				output.println("Enter your username: ");
				clientName = input.readLine().trim();

				//check if the clients name is valid
				//eg. (John3 = not valid)
				//(John = valid)
				if (Utils.nameIsValid(clientName)){
					break;
				}
				output.println("Your name is invalid. Please try again.");
			}
			
			//new client has entered the room
			//inform all other users that the client has joined the chat room
			output.println("Welcome to the chat "+clientName+"!");
			
			//inform the chat room that this client has joined
			for (int i = 0; i < 10; i++) {
				if (clients[i] != null && clients[i] != this){
					clients[i].output.println(clientName + " has joined the chatroom."); 
				}
			}
			
			
			while(true){
				String inLine = input.readLine();
				//TODO: user leaves the chat room here
				
				//sending public message
				synchronized(this){
					for (int i = 0; i < 10; i++) {
						if (clients[i] != null && clients[i] != this){
							clients[i].output.println(clientName + ": "+inLine);
						}
					}
				}
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
