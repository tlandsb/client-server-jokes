//JokeServer.java
//Trey Landsburg 
//CSC 435 Dist. Systems I
//September 2020
/*----------------------------------------------------------------------*/

import java.io.*;  //These libraries contain the PrintStream and BufferedReader.
import java.net.*; //These libraries contain the sockets.


//This "Worker" class takes the client program input from the accepted socket and then runs a subroutine called printJoke or printProverb, depending on user selection in "jokeProverbMode"
class clientWorker extends Thread {
	Socket sock; 			    /*create a new socket object of the "Socket" data structure*/
	clientWorker (Socket s) { 	/*The worker class contructor that will assign the argument s*/
	sock = s;}

	//The run function executes the desired I/O commands on the Worker thread, as well as calls the printJoke or printProverb subroutine
	public void run() {
		PrintStream out = null;   /*initializing a new PrintStream data structure object "out", that will be used to send data to the socket*/
		BufferedReader in = null; /*initializing a new BufferedReader data structure object "in", that will be used to read data from the socket*/

	//This section gets the input and output data and massages it into readable formats.
	try {
		in = new BufferedReader	(new InputStreamReader(sock.getInputStream())); /*Get the input from the sock object via getInputStream(), create a new InputStreamReader object, assign that to "in" of the BufferedReader data structure*/
		out = new PrintStream(sock.getOutputStream()); /*Get the output from the socket, via getOutputStream(), assign that to a new PrintStream data structure object "out"*/

	//This sections takes the address from the client, reads it in, assigns it to a string and then passes it to the subroutine printJoke() or printProverb to get  information to send back to the client.
	try {
		String jokeProverbMode; /*create a new String object "jokeProverbMode**/
		jokeProverbMode = in.readLine(); /*call the readLine() method on the "in" object that contains the input buffer. put this in the jokeProverbMode object*/
					
		String trackerToken; 
		trackerToken = in.readLine(); /*call the readLine() method on the "in" object that contains the input buffer. put this in the trackerToken object*/
		int trackerTokenInt = Integer.parseInt(trackerToken); //this token is used to ensure that each joke and proverb is sent back before ending and that they are random.
					
		//Is the user requesting a joke or a proverb?
		if (jokeProverbMode.equals("joke")) {
			printJoke(jokeProverbMode, out, trackerTokenInt);} // if the user selected joke call joke
						
		else if (jokeProverbMode.equals("proverb")) {   // if the user selected proverb call proverb
			printProverb(jokeProverbMode, out, trackerTokenInt); 
		}
		
		}
		catch (IOException x) {
			System.out.println("Server read error");
			x.printStackTrace ();
		}
	sock.close();
	}
	catch (IOException ioe) {System.out.println(ioe);}
		}
	
	
	// this function matches tokens with jokes
	static void printJoke (String name, PrintStream out, int trackerTokenInt) {
		//int jokeNumber = (int) getRandomIntegerBetweenRange(1, 4);
		String joke = null;
		
		if (trackerTokenInt == 1){
			joke = "2020 is looking up";}
		else if (trackerTokenInt == 2) {
			joke = "I don't trust stairs. They're always up to something.";}
		else if (trackerTokenInt == 3) {
			joke = "What do you call cheese that isn't yours? Nacho cheese.";}
		else if (trackerTokenInt == 4) {
			joke = "How does a penguin build its house? Igloos it together.";}
		else { joke = null;};		
	
		//out.println("here is what is in name: "+jokeNumber);
		if (name.equals("joke")) {
			out.println("Here's a joke: " + joke + "..."+trackerTokenInt);
			out.flush();}}
	
	
	// this function matches tokens with proverbs
	static void printProverb (String name, PrintStream out, int trackerTokenInt) {
		//out.println("trackerToken "+trackerToken);
		String joke = null;
		
		if (trackerTokenInt == 1){
			joke = "Remember to vote on November 3rd";}
		else if (trackerTokenInt == 2) {
			joke = "Its the eye of the tiger, its the thrill of the fight";}
		else if (trackerTokenInt == 3) {
			joke = "Dr. Fauci will get us through";}
		else if (trackerTokenInt == 4) {
			joke = "Stay calm and carry on";}
		else { joke = null;};		
	
		//out.println("here is what is in name: "+jokeNumber);
		if (name.equals("proverb")) {
			out.println("Here's a proverb: " + joke + "...");
			out.flush();}}
					}


	public class JokeServer{

		public static void main (String a[]) throws IOException {
			int q_len = 6;
			int clientPort = 1635; //port that were communicating with client on
			Socket clientSock;     //create a new socket					
			
			 //MISSING CODE - ADMIN WORKER THREAD: Connection to JokeClientAdmin and executing a worker thread 
			//This section of code SHOULD accept a new connection from the client admin program and
			//start a new admin thread that sets the adminMode to in the client program, a function would then set the 
			//joke proverb setting variable "jokeProverbMode" to either "joke" or "proverb" depending on what the admin 
			//was sending.. however I couldn't quite get this piece as I was getting a message that the socket was being refused.

			ServerSocket clientSocket = new ServerSocket (clientPort, q_len); //create a new server socket to communicate with the client
			System.out.println ("Joke Server Listening.\n");

			/*Keep listening to the socket and sending it to the work thread*/
			while (true) {
				clientSock = clientSocket.accept(); //wait here and block the program, until we get something from the client.
				new clientWorker(clientSock).start(); //start a new  worker thread with the socket we've accepted from the client.
			}
		}
	}
