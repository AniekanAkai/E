import java.net.*;
import java.io.*;


public class ClientThread extends Thread{

    private Socket clientSocket = null;

    ClientThread(Socket socket){
	super("ClientThread");
	this.clientSocket =  socket;
    }

    
    public void run(){
        System.out.println("Connected to "+ clientSocket.toString());
	try{
	      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	      out.println("Connection successful");
	      String inputLine, outputLine;
	      while ((inputLine = in.readLine()) != null) {
		  System.out.println(inputLine);
		  out.println("Received: "+ inputLine);

	      }
	      out.close();
	      in.close();
	      clientSocket.close();
	}catch(IOException e){
	    System.err.println("Accept failed.");
	    System.exit(1);
	 
	}
    }

}
