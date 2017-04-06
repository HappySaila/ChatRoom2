import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable{
	
	private final String host = "localhost";
	private final int portNumber = 1234;
	
	//instantiation
	private static Socket clientSocket = null;
	private static DataInputStream input = null;
	private static PrintStream output = null;
	private static BufferedReader message = null;
	private static boolean isClosed = false;
	
	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient();
		
		//Open client streams and socket
		//Attept to connect to the host
		try{
			//clear the terminal
			Utils.ClearTerminal();
			//print heading
			Utils.PrintUILine();
			System.out.println("Connecting...");


			//create socket
			clientSocket = new Socket(chatClient.host, chatClient.portNumber);
			//create streams to write and read from the client socket
			input = new DataInputStream(clientSocket.getInputStream());
			output = new PrintStream(clientSocket.getOutputStream());
			message = new BufferedReader(new InputStreamReader(System.in));
		} catch (UnknownHostException e){
			//unknown host cannot create socket
			Utils.UnableToConnect(e);
		} catch (IOException e){
			//input/output error
			Utils.UnableToConnect(e);
		}
		
		//if the client was instantiated correctly then begin communicating
		//data is read and written using different threads
		//data is written to the socket on a new thread
		//data is read from the socket using the current main thread
		//data is read from client socket using Print stream and a buffered reader
		//data is written to the client socket using dataInputStream
		if (clientSocket!=null && input!=null && output!=null){
			//client is connected
			System.out.println("Connected!");
			System.out.println("Use \"/quit\" to leave the chatroom.");
			System.out.println("Use \"/m Recipient\" to send a private message.");
			System.out.println("Eg. /m Greg Hello greg! -> will send \"Hello greg\" to Greg.");
			System.out.println("Use \"/f \" to upload file to chat server.");
			System.out.println("Use \"/i filename.ext\" to view specific uploaded file.");
			System.out.println("Eg. /i puppy.jpeg! -> will open the image puppy.jpeg.");
			System.out.println("Use \"/v \" to upload file privately to user.");
			Utils.PrintUILine();
			//create a new thread to write data to the client socket
			new Thread(new ChatClient()).start();
			
			try {
				//port has been opened
				while(!isClosed){
					output.println(message.readLine().trim());
				}
				
				//the server has terminated the connection
				//close all ports
				output.close();
				input.close();
				clientSocket.close();
				
			} catch (IOException e) {
				System.out.println("IO Exception!");
				e.printStackTrace();
			}
			
		}
		
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		//keep on reading from the socket until the server tells the server closes the connection
		String reply;
		
		try {
			while((reply = input.readLine()) != null){
				System.out.println(reply);
				if (reply.indexOf("Goodbye") != -1){
					break;
				}
			}
			Utils.PrintUILine();
			isClosed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
