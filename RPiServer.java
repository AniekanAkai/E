import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
* RPiServer.java
* This is the central Java Server that executes the program such that the 2 users(one connected to
* the GertBoard and one connected to the PiFace but the server doesn't know about this) connect to
* the server, where they each send a 4 bit configuration. 
* The server checks if the bit configuration are the same. 
* If they do, a message is sent to both users from the server, and the user's LEDs flash and shut off,
* if not the server waits for more inputs from the users connected
*/
public class RPiServer {

	ClientThread clientThread;
	ArrayList<Socket> clientSocketList;//List of all the socket information for each user
	ArrayList<ClientThread> threads = new ArrayList<ClientThread>();//List of Threads created
	Socket clientSocket = null;
	ServerSocket serverSocket = null;
	volatile String[][] user = new String[2][2];
	/**
	 * This represents a table containing the user's data(IP & Bit stream)
	 * 				 User 1 		User 2 
	 * IP Addr   	user(0,0)      user(0,1) 
	 * Bit Config   user(1,0)      user(1,1)
	 * This class is volatile because it is subject to change from either of the threads at any
	 * time.
	 */
	int numUsers = 0; //Number of users connected to the server. Maximum is 2
	boolean check = false;//True if both users enter the same bit stream
	int countTries = 0 //Number of tries
	
	synchronized void assignConfig(String IP, String config){
	//Changes the bit configuration corresponding to the user that sends in its config
		
			System.out.println("Changing Configuration of " + IP);
			System.out.println("user 00  " + user[0][0]);
			System.out.println("user 01  " + user[0][1]);

			if (IP.equals(user[0][0])){
				user[1][0] = config;
			}else if(IP.equals(user[0][1])){
				user[1][1] = config;
			}else{
				System.out.println("This user is not registered!");
			}
	}

	boolean checkMatch() {
	//Changes that the bit configurations for each user matches
		if (user[1][0].equals(user[1][1])) {
			return true;
		} else {
			return false;
		}
	}
	
	public void run(){
	//Runs the server
		int loops = 0;
		boolean gameover = false;
		clientSocketList = new ArrayList<Socket>();
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

		while(true){
			try {
 
				if(numUsers<=1){ //Does not allow more than 2 users to connect to the server
						System.out.println("Waiting for client...");
						clientSocket = serverSocket.accept();
						clientSocket.setKeepAlive(true);
						String socketIP = clientSocket.getInetAddress().getHostAddress();
						clientThread = new ClientThread(clientSocket, this, socketIP);
						threads.add(clientThread);
						System.out.println("New Gamer connected.\n ID: "+ clientThread.getId()+" ; IP: "+ clientSocket.getInetAddress()+"\n");
						
						clientThread.start();		
				}
				
				if(numUsers>=2){
					System.out.println("Max Users reached.");
					
				}else if(numUsers==1){
					clientSocketList.add(clientSocket);
					user[0][0] = clientSocket.getInetAddress().getHostAddress();// Gamer 1 IP address
					user[1][0] = "2222";//Default config for gamer 1
					numUsers++;
				}else if(numUsers == 0){
					clientSocketList.add(clientSocket);
					user[0][1] = clientSocket.getInetAddress().getHostAddress(); // Gamer 2 IP address
					user[1][1] = "3333";//Default config for gamer 2
					++numUsers;
				}
					
				
				if(numUsers == 2){					
					if(loops >= 2){
						//This allows the server to listen for more inputs after the first set of input in the case 
						//of no match. It listens until a match is made. 
						for (ClientThread s : threads){
							s.run();
						}
					}
					while(user[1][0] =="2222" || user[1][1] == "3333"){}//Does nothing until both users have changed their bit config
					countTries++;
					System.out.println(checkMatch());
					for (Socket s : clientSocketList){
						if(s.isClosed()){
							System.out.println("closed: " + s.getInetAddress().getHostAddress());
						}
						OutputStream outToClient = s.getOutputStream();
						if(checkMatch()){
							System.out.println("Number of tries: " + Integer.parseInt(countTries));
							outToClient.write("win".getBytes());
							cleanup(s);
							gameover = true;
						} else{
							user[1][0] ="2222";
							user[1][1] = "3333";
							outToClient.write("fail".getBytes());
						}
					}
				}

				if(gameover){
					//There is a match
					break;				
				}
	
			} catch (IOException e) {
				e.printStackTrace();
			}

				++loops;
		}
	}

	private void cleanup(Socket s) {
		try {
			s.close();
		} catch (IOException e) {
			//coulkd not close socket. oh well.
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args) throws IOException {
	
		RPiServer server = new RPiServer();
		server.run();
    }

}
