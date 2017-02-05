package se1;

import java.net.*;
import java.io.*;

public class KWIC_CyberMiner{
	public CircularShift cs;
	public Alphabetizer ab;
	ServerSocket serverSocket = null;

	public KWIC_CyberMiner() {

		try {
			serverSocket = new ServerSocket(1234);
			System.out.println("Server has started");
			while (true)
				new KWIC_Thread(serverSocket.accept()).run();
			
		} catch (IOException e) {
			System.err.println("Could not listen on port 1492.");
			System.exit(-1);
		}

		// serverSocket.close();
	}

	public static void main(String[] args){
		KWIC_CyberMiner server = new KWIC_CyberMiner();
	}
}