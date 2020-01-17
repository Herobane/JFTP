package fr.herobane.jftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread implements Runnable 
{
	
	public ServerThread(String name)
	{
		super(name);
	}

	@Override
	public void run() 
	{
		try {
			ServerSocket server = new ServerSocket(12345);
			Socket client = server.accept();
			
			System.out.println(client.getInetAddress().toString());
			
			client.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
