package fr.herobane.jftp.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import fr.herobane.jftp.models.RemoteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;

public class Controller
{

	@FXML
	private ListView<String> remoteListView;
	
	@FXML TextFlow textFlow;
	
	@FXML
	void initialize()
	{
		
		BufferedReader dataReader;
		
		RemoteModel connection = new RemoteModel("127.0.0.1", 21);
		
		String result;
		
		try
		{
			
			connection.getResponse();
			connection.sendCommand("USER master");
			connection.getResponse();
			connection.sendCommand("PASS");
			connection.getResponse();
			connection.sendCommand("TYPE I");
			connection.getResponse();
			
			connection.pasv();
			
			dataReader = connection.getDataReader();
			
			connection.sendCommand("LIST");
			result = connection.getResponse(dataReader);

			
			StringTokenizer tokenizer = new StringTokenizer(result, "\r\n");
			
			ObservableList<String> folders = FXCollections.observableArrayList();
			
			while (tokenizer.hasMoreElements())
			{
				folders.add((String) tokenizer.nextElement());
			}
			
			remoteListView.setItems(folders);
			
		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		
	}
	
}
