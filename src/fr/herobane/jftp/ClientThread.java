package fr.herobane.jftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class ClientThread extends Thread
{

	public ClientThread(String name)
	{

		super(name);

	}

	@Override
	public void run()
	{

		try
		{

			Socket client = new Socket("127.0.0.1", 21);
			
			BufferedWriter ostream = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			BufferedWriter dataWriter;
			BufferedReader istream = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedReader dataReader;
			
			getResponse(istream);
			sendMessage(ostream, "USER master");
			getResponse(istream);
			sendMessage(ostream, "PASS");
			getResponse(istream);
			sendMessage(ostream, "TYPE I");
			getResponse(istream);
			
			Socket dataChannel = pasv(ostream, istream);
			
			dataWriter = new BufferedWriter(new OutputStreamWriter(dataChannel.getOutputStream()));
			dataReader = new BufferedReader(new InputStreamReader(dataChannel.getInputStream()));
			
			sendMessage(ostream, "RETR test.txt");
			getResponse(dataReader);
			
			ostream.close();
			istream.close();
			dataWriter.close();
			dataReader.close();
			dataChannel.close();
			client.close();

		}
		catch (UnknownHostException e)
		{

			e.printStackTrace();

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}

	}

	public Socket pasv(BufferedWriter ostream, BufferedReader istream) throws IOException
	{
		ostream.write("PASV\r\n");
		ostream.flush();
		
		String response = istream.readLine();
		String ipAddress;
		String[] tokens = new String[8];
		
		int index = 0;
		int port;
		
		StringTokenizer tokenizer;
		
		System.out.println("[SERVER] : " + response);
		
		while (response.charAt(index) != '(')
		{
			index++;
		}
		
		response = response.substring(index + 1, response.length() - 1);
		
		tokenizer = new StringTokenizer(response, ",");
		
		index = 0;
		
		while (tokenizer.hasMoreElements())
		{
			tokens[index] = (String) tokenizer.nextElement();
			index++;
		}
		
		ipAddress = tokens[0] + "." + tokens[1] + "." + tokens[2] + "." + tokens[3];
		port = (Integer.parseInt(tokens[4]) * 256) + Integer.parseInt(tokens[5]);
		
		return new Socket(ipAddress, port);
	}
	
	public String sendMessage(BufferedWriter ostream, String msg)
	{	
		
		try
		{
			ostream.write(msg + "\r\n");
			ostream.flush();
			
			System.out.println("[CLIENT] : " + msg);

		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

		return msg;

	}

	public void getResponse(BufferedReader istream)
	{	

		try
		{

			Thread.sleep(1000);
			
			while (istream.ready())
			{
				String response = istream.readLine();
				System.out.println("[SERVER] : " + response);
			}
			
			
			
			// return response;

		}
		catch (IOException e)
		{
			e.printStackTrace();
			// return "0";

		}
		catch (InterruptedException e)
		{

			e.printStackTrace();
			// return "0";
		}
		

		

	}

}
