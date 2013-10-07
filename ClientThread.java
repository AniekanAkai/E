import java.net.*;
import java.io.*;

/**
* ClientThread.java
* Threads to manage each user(client) that connects to the server.
* This thread communicates directly with the users(GertBoard and PiFace)
*/
public class ClientThread extends Thread{
	char [] bits = new char[4]; //Bits are transmitted as an array of characters
	RPiServer server;
    public Socket clientSocket = null;
	String configuration;
	private String myIP;
	
    ClientThread(Socket socket, RPiServer server, String socketIP){
		super("ClientThread");
		this.clientSocket =  socket;
		this.server = server;
		myIP = socketIP;
	}

    
    public void run(){
        System.out.println("Connected to "+ clientSocket.toString());
		try{
			  PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		while(in.read(bits) == 0){
			System.out.println(".");			
		}

		configuration = new String(bits);

		  if (configuration != null) {
				server.assignConfig(myIP, configuration);
				System.out.println(configuration);
				
		 } else {
        			System.out.println("bad bad bad");
		 }
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		 
		}
    }

	String getIP(){
		return clientSocket.getInetAddress().toString();
	}
	
	
	String getConfig(){
		return configuration;
	}
	
}
o imp