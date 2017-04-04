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

	//will send a message to all other clients
	synchronized void messageAll(String message, boolean isStatement){
		//if a message is a statement then there client name will not get printed with the output
		//eg. Tim has joined the chat -> is a statement
		//eg. Tim: Tim has joined the chat -> is not a statement
		for (int i = 0; i < ChatServer.chatroomCapacity; i++) {
			if (clients[i] != null && clients[i] != this){
				if (isStatement){
					clients[i].output.println("<<<" + message + ">>>");
				} else {
					clients[i].output.println(clientName + ": "+message);
				}

			}
		}
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
			messageAll(clientName + " has joined the chatroom.", true);


			while(true){
				//reads the clients input from the socket
				String inLine = input.readLine();

				if (inLine.equals("/quit")){
					//tell all the other users that the client has left the chat
					messageAll(clientName + " has left the chat.", true);
					break;
				}
				
				//send the message to all of the users
				messageAll(inLine, false);
			}
			
			//close all I/O components
			Utils.PrintUILine(output);
			output.println("Goodbye "+clientName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
