import java.net.*;
import java.io.*;


public class ClientThread extends Thread{
	char [] bits = new char[4]; 
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
        System.out.println("1");
			  PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("2");
			  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("3");

			int x =0;

			while(in.read(bits) == 0){
        System.out.println(".");
			
			}
        		System.out.println("4");

			configuration = new String(bits);

			  if (configuration != null) {
				server.assignConfig(myIP, configuration);
				System.out.println(configuration);
				
			 } else {
        			System.out.println("bad bad bad");
		
			}
			 // out.close();
			 // in.close();
			System.out.println("from cleint thread is socket closed: " +clientSocket.isClosed());
			  //clientSocket.close();
		}catch(IOException e){
        			System.out.println("bad bad badsss");
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
