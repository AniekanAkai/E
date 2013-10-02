import java.net.*;
import java.io.*;


public class ClientThread extends Thread{

	private Socket clientSocket = null;
	String configuration = null;

	ClientThread(Socket socket){
		super("ClientThread");
		this.clientSocket = socket;
	    }

	    
	public void run(){
		System.out.println("Connected to "+ clientSocket.toString());
		try{
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		while ((configuration = in.readLine()) != null) {
			System.out.println(configuration);
			out.println("WIN!!!");	
			break;
			
		}
			
		out.close();
		in.close();
		
		clientSocket.close();

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
