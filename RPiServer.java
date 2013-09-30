import java.net.*;
import java.io.*;

 
public class RPiServer {
    public static void main(String[] args) throws IOException {
 
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

 	ClientThread clientThread;
        Socket clientSocket = null;
	while(true){
	    try {
		System.out.println("Waiting for client...");
		clientSocket = serverSocket.accept();
		clientThread = new ClientThread(clientSocket);
		clientThread.start();
		
	    } catch (IOException e) {
	   }
	}
    }
}

