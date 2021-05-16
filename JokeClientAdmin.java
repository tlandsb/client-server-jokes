//JokeClientAdmin.java
//Trey Landsburg 
//CSC 435 Dist. Systems I
//September 2020
/*----------------------------------------------------------------------*/

import java.io.*; 
import java.net.*; 
public class JokeClientAdmin {

	//This code was not finished and implemented... should send out admin signals to the server, which will create threads to assign joke or proverb to client.
	
	public static void main (String args[]){
		System.out.println("Administration Mode");
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));		
		String jokeProverbModeAdmin;
		
		try{
			do{			
			System.out.println("Would you like a joke or proverb? (Administration Mode) \n");
			System.out.flush();			
			jokeProverbModeAdmin = in.readLine();
			
			admin(jokeProverbModeAdmin);
	
			} while(jokeProverbModeAdmin.indexOf("Quit")<0);
			
			System.out.println ("Cancelled by user");
		}catch (IOException x ){
            x.printStackTrace();
	
	}
	}

static void admin (String jokeProverbModeAdmin){
	Socket sock;
	PrintStream toServer;
	
	try
	{
		sock = new Socket("localhost", 1624);
		toServer = new PrintStream(sock.getOutputStream());		
		toServer.println(jokeProverbModeAdmin); 	
		} catch (IOException x) { System.out.println ("socket error."); x.printStackTrace ();}
	}	

}