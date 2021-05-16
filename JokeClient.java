//JokeClient.java
//Trey Landsburg 
//CSC 435 Dist. Systems I
//September 2020
/*----------------------------------------------------------------------*/
import java.io.*; 			//These libraries contain the PrintStream and BufferedReader.
import java.net.*; 		   //These libraries contain the sockets.
import java.util.Random;  //Just used as part of int array shuffle

public class JokeClient{

 public static void main (String args[]) {
 String serverName;                              //Create a string object named "serverName"
 if (args.length < 1) serverName = "localhost"; //If the length of the args array is less than one, set the serverName to "localHost"
 else serverName = args[0];                    //otherwise set it to the first command line argument

 String adminMode=null; //This should have been used to denote if the client was in "admin mode", however it is not used.
 
 System.out.println("You have started Joke Client\n");	
 System.out.println("Written by Trey Landsburg (Based on Dr. Clark Elliot's Original coding for Inet), 1.8.\n");
 
 //Create a new object "in" of the BufferedReader data structure, set this to input from the user via System.in
 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 
 try {
 String userName = null; //Create a new String object to hold user's name "userName", that will be used to house the value from the user.
 String jokeProverbMode; //Create a new String object to hold user's selection "jokeProverbMode".

 do {

 //get the user's name from System.in
 if (userName == null) {
	 System.out.print ("Hello, what is your name? ");
	 System.out.flush ();
	 userName = in.readLine (); //read the input buffer from the user	 
 }
 
 /************Admin Mode******************/
 /****************************************/
 //Non functional call, please see below:
// getAdminMode(serverName, adminMode);
 
 if (adminMode != null) {
	 System.out.print ("Administration Mode Activated \n");
	 System.out.flush ();
	 jokeProverbMode = adminMode;
	 //getAdminMode();
	 }
 else {
 System.out.print ("Would you like a joke or proverb? ");
 jokeProverbMode = in.readLine (); //read the input buffer from the user, into the string object "jokeProverbMode"
	 }
 /****************************************/
 /****************************************/ 
 
 //This int array is used to ensure that EVERY joke or proverb is given back to the client
 int[] tokenArray = {1,2,3,4}; 
 intArrayShuffle(tokenArray); //the array is shuffled before every execution to ensure that jokes and proverbs are random
  
 //Call the function that gets the proverb from the server 4 times, so that each client connection returns all 4 proverbs or all 4 jokes, depending on user selection.
 if (jokeProverbMode.indexOf("quit") < 0)
	 for (int i = 0; i < 4; i++){
		 getJokeProverb(userName, jokeProverbMode, serverName, tokenArray[i]); 
		 if (i<4) enterToContinue(); //hit enter to get next joke or proverb, stop when you get to iteration 3
	 	 }
 System.out.println("ALL " + jokeProverbMode.toUpperCase() + " CYCLES HAVE COMPLETED");
 jokeProverbMode="quit"; //when we get all 4 jokes or proverbs, quit the client
 } while (jokeProverbMode.indexOf("quit") < 0);
 System.out.println ("Client has ended, Please restart");
 } 
 catch (IOException x) {x.printStackTrace ( );}
 }

 
 /************Utility functions************/
 /****************************************/
 //Function to hit enter after each joke or proverb
 private static void enterToContinue() { 
	    //int letter;
        System.out.println("Press enter for next next one \n");
        try
        {
        	System.in.read();
        }  
        catch(Exception e)
        {}  
 }
 
 //Fisher–Yates shuffle
 //the function that shuffles the int array that contains the tokens for each round.
 private static void intArrayShuffle(int[] array)
 {
     int indexVar, tempVar;
     Random random = new Random();
     for (int i = array.length - 1; i > 0; i--){    	 
    	 indexVar = random.nextInt(i + 1);
         tempVar = array[indexVar];
         array[indexVar] = array[i];
         array[i] = tempVar;
     }
 }

/****************************************/
 /****************************************/  
 //This section of code is NON-FUNCTIONAL. It should receive the socket connection from the admin worker thread 
 //and adminMode to a null null value that will indicate in the code above that the client is actually taking orders
 //from the admin program and that "jokeProverbMode" will just take either "joke" or "proverb" from the admin program, not 
 //read in by the user via System.in. However, getting a socket refused error so I commented this out//
 
 
// static void getAdminMode (String serverName, String adminMode){
//	 Socket sock; //create a new Socket object "sock"
//	 BufferedReader fromServer; //For reading data
//	 PrintStream toServer; //for sending data
//	 String textFromServer; //String object to hold server data in a clean format.
//
//	 try{
//	 sock = new Socket(serverName, 1620); //create a new socket object, this will be used to send and receive data streams.
//
//	 fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Receives the data
//	 toServer = new PrintStream(sock.getOutputStream()); //Sends the data
//	 	 
//	 //give the server a random array
//	 //for each element in the array give me  
//	 textFromServer = fromServer.readLine(); //read in the data from the server and put it in the textFromServer object of the String data structure
//	 if (textFromServer != null)
//		 adminMode = textFromServer; //print out the data that we got from the server program
//	 //sock.close(); //close the socket
//	 } catch (IOException x) {
//	 System.out.println ("Socket error.");
//	 x.printStackTrace ();
//	 }
//	 }
 
 
 static void getJokeProverb (String userName, String jokeProverbMode, String serverName, int tokenArray){
 Socket sock; //create a new Socket object "sock"
 BufferedReader fromServer; //For reading data
 PrintStream toServer; //for sending data
 String textFromServer; //String object to hold server data in a clean format.

 try{
 sock = new Socket(serverName, 1635); //create a new socket object, this will be used to send and receive data streams.

 fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Receives the data
 toServer = new PrintStream(sock.getOutputStream()); //Sends the data
  

 toServer.println(jokeProverbMode); //send the "jokeProverbMode" object to the server. This "jokeProverbMode" the users selection
 toServer.flush(); //flush the stream
 
 toServer.println(tokenArray); //send the "tokenArray" object to the server. This "tokenArray" contains the unique numeric id for each joke/proverb.
 toServer.flush(); //flush the stream
 
 //pull back all the jokes and proverbs from the server
 textFromServer = fromServer.readLine(); //read in the data from the server and put it in the textFromServer object of the String data structure
 if (textFromServer != null) System.out.println(userName+", "+textFromServer); //print out the data that we got from the server program
 System.out.println("("+jokeProverbMode+" cycle completed)\n");
 System.out.flush ();
 //sock.close(); //close the socket
 } catch (IOException x) {
 System.out.println ("Socket error.");
 x.printStackTrace ();
 }
 }
}
