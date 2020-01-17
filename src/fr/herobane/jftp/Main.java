package fr.herobane.jftp;

public class Main {

	public static void main(String[] args) {
		
		ClientThread client = new ClientThread("client1");

		client.start();		
		
	}

}
