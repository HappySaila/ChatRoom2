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
			System.out.println("Connection...");


			//create socket
			clientSocket = new Socket(chatClient.host, chatClient.portNumber);
			//create streams
			input = new DataInputStream(clientSocket.getInputStream());
			output = new PrintStream(clientSocket.getOutputStream());
			//create message reader
			message = new BufferedReader(new InputStreamReader(System.in));
		} catch (UnknownHostException e){
			//unknown host cannot create socket
			System.out.println("Error:"+e.getMessage());
			System.exit(0);
		} catch (IOException e){
			//input/output error
			System.out.println("Error:"+e.getMessage());
			System.exit(0);
		}
		
		//if all streams and the socket has been created successfully, write data to the open connection and port
		if (clientSocket!=null && input!=null && output!=null){
			System.out.println("Connecting...");
			new Thread(new ChatClient()).start();
			
			try {
				//for an open port
				while(!isClosed){
					output.println(message.readLine().trim());
				}
				
				//close streams and socket
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
		//keep on reading from the socket until the server tells the client to "terminate"
		String reply;
		
		try {
			while((reply = input.readLine()) != null){//user has entered a valid reply
				System.out.println(reply);
				if (reply.indexOf("*** terminated") != -1){
					break;
				}
			}
			isClosed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
