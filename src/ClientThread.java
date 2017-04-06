import org.apache.commons.io.FileUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.io.DataInputStream;
import javax.swing.JDialog;
import java.io.IOException;
import java.io.PrintStream;
import java.awt.Desktop;
import java.net.Socket;
import java.io.File;
import java.util.*;


public class ClientThread extends Thread{
	public String clientName;
	
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

	synchronized  void messagePrivate(String message, String recipient){
		//will send a private message only to the recipient

		for (int i = 0; i < ChatServer.chatroomCapacity; i++) {
			if (clients[i] != null && clients[i] != this && clients[i].clientName.equals(recipient)){
				clients[i].output.println("<<<Private message from "+clientName+">>>");
				clients[i].output.println("--" + clientName + ": "+message);
			}
		}
	}

	synchronized void CloseThread() throws IOException{
		//terminates thread and removes it from the server so that its space can be reused
		for (int i = 0; i < ChatServer.chatroomCapacity; i++) {
			if (clients[i] == this) {
				clients[i] = null;
			}
		}

		input.close();
		output.close();
		clientSocket.close();
	}
	
	public void moveTheFile (String location) {
        try {
            File destDir = new File("Uploads/");
            File srcFile = new File(location);
            FileUtils.copyFileToDirectory(srcFile, destDir);
        } catch(Exception e) {
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
				//check if the user has quit the program
				if (inLine.equals("/quit")){
					//tell all the other users that the client has left the chat
					messageAll(clientName + " has left the chat.", true);
					break;
				}


				//checks if user has invoked special functionality eg. /f /m /i
				//else it will forward all text typed to all other clients
				if (inLine.startsWith("/f")){
					//Create new File Chooser and initiate its pop up
					final JFileChooser fc = new JFileChooser();
					int option = fc.showDialog(null, "Attach");
          
					if (option == JFileChooser.APPROVE_OPTION) {
						
						//instantiate file
						File selectedFile = fc.getSelectedFile();
						//Get size of file
						double kilobytes = (selectedFile.length())/(1024);
						//Inform Users file has been uploaded
						messageAll(" Uploaded file: "+selectedFile.getName()+" ("+kilobytes+" KB) .", false);            
						//Copy File to Server ("Uploads Folder")
						String path = selectedFile.getAbsolutePath();
						moveTheFile(path);
					}
				} else if(inLine.startsWith("/i")){
					try{  //Gets the file that user wants to open
						  String fileToOpen = inLine;
						  try{
							 fileToOpen = inLine.substring(3);
							 }
							 catch (Exception e)
							 {
								messageAll("Enter valid file!", false);
								e.printStackTrace();  
							 }
							 
							 //File is open using default application
							 File f = new File("Uploads/"+fileToOpen);
							 Desktop dt = Desktop.getDesktop();
							 dt.open(f);
						}
						 catch (Exception ex)
						 {
							 messageAll("Enter valid file!", false);
							 ex.printStackTrace();
						 }   
				} else if (inLine.startsWith("/m")){
					//send the message to a single user as private
					String recipient = inLine.split(" ")[1];
					//edit message, current message = /m name message. we want current message = message
					inLine = inLine.split(" ", 3)[2];
					messagePrivate(inLine, recipient);
				} else{//send the message to all of the users as a public message
					 messageAll(inLine, false);
				}


			}
			
			//close all I/O components
			Utils.PrintUILine(output);
			output.println("Goodbye "+clientName);

			CloseThread();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

}
