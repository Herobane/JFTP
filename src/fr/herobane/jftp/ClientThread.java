package fr.herobane.jftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread {
	
	public ClientThread(String name)
	{
		super(name);
	}

	@Override
	public void run() 
	{
		try {
			Socket client = new Socket("127.0.0.1", 21);
			
			BufferedOutputStream ostream = new BufferedOutputStream(client.getOutputStream());
			BufferedInputStream istream = new BufferedInputStream(client.getInputStream());
			
			System.out.println(getResponse(istream));
			
			sendMessage(ostream, "USER master");

			System.out.println(getResponse(istream));
			
			sendMessage(ostream, "PASS");
			
			System.out.println(getResponse(istream));
			
			sendMessage(ostream, "PASV");
			
			System.out.println(getResponse(istream));
			
			ostream.close();
			istream.close();
			client.close();
	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(OutputStream ostream, String msg)
	{
		msg += "\r\n";
		
		int strLength = msg.length();
		int index = 0;
		
		try {
			
			while(index < strLength)
			{
				ostream.write((int) msg.charAt(index));
				index++;
			}
			ostream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponse(InputStream istream)
	{
		String response = "";
		
		try 
		{
			Thread.sleep(1000);
			
			while (istream.available() > 0)
			{
				response += Character.toString((char) istream.read());
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return response;
	}
}
