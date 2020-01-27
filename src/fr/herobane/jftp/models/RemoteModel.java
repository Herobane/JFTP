package fr.herobane.jftp.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class RemoteModel
{

	private String remoteAddress;
	private int primaryPort, dataPort;
	private Socket primarySocket, dataSocket;
	private BufferedWriter primaryWriter, dataWriter;
	private BufferedReader primaryReader, dataReader;
	
	public RemoteModel(String ipAddress, int port)
	{

		this.remoteAddress = ipAddress;
		this.primaryPort = port;
		
		try
		{

			this.primarySocket = new Socket(ipAddress, port);
			this.primaryWriter = new BufferedWriter(new OutputStreamWriter(this.primarySocket.getOutputStream()));
			this.primaryReader = new BufferedReader(new InputStreamReader(this.primarySocket.getInputStream()));

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
	
	// Enter passive mode and return a Socket with the matching IP address and port
	public Socket pasv() throws IOException
	{
		
		
		this.primaryWriter.write("PASV\r\n");
		this.primaryWriter.flush();
		
		String response = this.primaryReader.readLine();
		String ipAddress;
		String[] tokens = new String[8];
		
		int index = 0;
		
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
		this.dataPort = (Integer.parseInt(tokens[4]) * 256) + Integer.parseInt(tokens[5]);
		
		this.dataSocket = new Socket(ipAddress, this.dataPort);
		
		this.dataReader = new BufferedReader(new InputStreamReader(this.dataSocket.getInputStream()));
		this.dataWriter = new BufferedWriter(new OutputStreamWriter(this.dataSocket.getOutputStream()));
		
		return this.dataSocket;
	}
	
	// Send raw command to the remote host
	// Return a String containing the command sent
	// Can throw an IOException
	public String sendCommand(String msg)
	{	
		
		try
		{
			this.primaryWriter.write(msg + "\r\n");
			this.primaryWriter.flush();
			
			System.out.println("[CLIENT] : " + msg);

		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

		return msg;

	}
	
	public String sendCommand(BufferedWriter outputStream, String msg)
	{	
		
		try
		{
			outputStream.write(msg + "\r\n");
			outputStream.flush();
			
			System.out.println("[CLIENT] : " + msg);

		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

		return msg;

	}

	// Get the remote host's last response 
	// Return a String containing that response
	// Can throw an IOException and/or InterruptedException
	public String getResponse()
	{	

		try
		{

			Thread.sleep(1000);
			
			String fResponse = "";
			
			while (this.primaryReader.ready())
			{
				String response = this.primaryReader.readLine();
				fResponse = fResponse + response + "\n";
				System.out.println("[SERVER] : " + response);
			}
			
			
			
			return fResponse;

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "0";

		}
		catch (InterruptedException e)
		{

			e.printStackTrace();
			return "0";
		}
		

		

	}
	
	public String getResponse(BufferedReader inputStream)
	{	

		try
		{

			Thread.sleep(1000);
			
			String fResponse = "";
			
			while (inputStream.ready())
			{
				String response = inputStream.readLine();
				fResponse = fResponse + response + "\r\n";
				System.out.println("[SERVER] : " + response);
			}
			
			
			
			return fResponse;

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "0";

		}
		catch (InterruptedException e)
		{

			e.printStackTrace();
			return "0";
		}
		

	}
	
	public String getRemoteAddress()
	{
		return this.remoteAddress;
	}
	
	public int getPrimaryPort()
	{
		return this.primaryPort;
	}
	
	public int getDataPort()
	{
		return this.dataPort;
	}
	
	public Socket getPrimarySocket()
	{
		return this.primarySocket;
	}
	
	public Socket getDataSocket()
	{
		return this.dataSocket;
	}
	
	public BufferedWriter getPrimaryWriter()
	{
		return this.primaryWriter;
	}
	
	public BufferedWriter getDataWriter()
	{
		return this.dataWriter;
	}
	
	public BufferedReader getPrimaryReader()
	{
		return this.primaryReader;
	}
	
	public BufferedReader getDataReader()
	{
		return this.dataReader;
	}

}
