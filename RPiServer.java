import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class RPiServer {

	ClientThread clientThread;
	ArrayList<Socket> clientSocketList;
	ArrayList<ClientThread> threads = new ArrayList<ClientThread>();
	Socket clientSocket = null;
	ServerSocket serverSocket = null;
	volatile String[][] user = new String[2][2];
	/**
	 * Gamer 1 Gamer 2 IP Addr gamer(0,0) gamer(0,1) Config gamer(1,0)
	 * gamer(1,1)
	 * 
	 */
	int numUsers = 0;
	boolean check = false;

	synchronized void assignConfig(String IP, String config){

System.out.println("changing conf of " + IP);
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
		if (user[1][0].equals(user[1][1])) {
			return true;
		} else {
			return false;
		}

	}
	
	public void run(){
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

if(numUsers<=1){
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
					for (ClientThread s : threads){
					s.run();
					}
					}
					while(user[1][0] =="2222" || user[1][1] == "3333"){}
					System.out.println(checkMatch());
					for (Socket s : clientSocketList){
						if(s.isClosed()){
							System.out.println("cl;osed: " + s.getInetAddress().getHostAddress());
						}
						OutputStream outToClient = s.getOutputStream();
						if(checkMatch()){
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
		}
	
	}

	public static void main(String[] args) throws IOException {
	
		RPiServer server = new RPiServer();
		server.run();
    }

}
