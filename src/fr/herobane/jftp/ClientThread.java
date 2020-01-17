package fr.herobane.jftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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

			BufferedOutputStream ostream = new BufferedOutputStream(client.getOutputStream());
			BufferedInputStream istream = new BufferedInputStream(client.getInputStream());

			getResponse(istream);
			sendMessage(ostream, "USER master");
			getResponse(istream);
			sendMessage(ostream, "PASS");
			getResponse(istream);
			sendMessage(ostream, "PWD");
			getResponse(istream);

			ostream.close();
			istream.close();
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

	public String sendMessage(OutputStream ostream, String msg)
	{

		String request = msg + "\r\n";

		System.out.println("[CLIENT] : " + msg);

		int strLength = request.length();
		int index = 0;

		try
		{

			while (index < strLength)
			{

				ostream.write((int) request.charAt(index));
				index++;

			}

			ostream.flush();

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}

		return msg;

	}

	public String getResponse(InputStream istream)
	{

		String response = "";

		try
		{
//			Thread.sleep(1000);

			while (istream.available() > 0)
			{

				response += Character.toString((char) istream.read());

			}

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}

		System.out.println("[SERVER] : " + response);

		return response;

	}

}
