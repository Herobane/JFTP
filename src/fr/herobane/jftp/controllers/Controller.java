package fr.herobane.jftp.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import fr.herobane.jftp.models.RemoteModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class Controller
{

	@FXML
	protected ListView<String> remoteListView;
	@FXML
	protected MenuBar menuBar;
	@FXML
	protected Menu menuFile;
	@FXML
	protected MenuItem menuItemNConnection;
	
	public ObservableList<String> folders;

	@FXML
	void initialize()
	{
		
		BufferedReader dataReader;
		
		RemoteModel connection = new RemoteModel("127.0.0.1", 21);
		
		String result, currentItem;
		
		MenuController mcontroller = new MenuController();
		RemoteListController rlController = new RemoteListController();
		
		//			mcontroller.newConnection();
		rlController.initialize();
		
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

			parseResponse(result, "\r\n", folders);
			
			
		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		
		remoteListView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent arg0)
			{

				
				if(arg0.getClickCount() == 2) 
				{
					
					StringTokenizer st = new StringTokenizer(remoteListView.getFocusModel().getFocusedItem(), " ");
					String folder = "";
					
					while (st.hasMoreElements())
					{
						folder = (String) st.nextElement();
					}
					
					parseResponse(connection.changeWorkingDirectory(folder), "\r\n", folders);
					
					
				}

			}
		});
		
		
	}
	
	public void parseResponse(String response, String delimiter, ObservableList<String> folders)
	{
		StringTokenizer tokenizer = new StringTokenizer(response, delimiter);
		
		folders = FXCollections.observableArrayList();
		
		folders.add("..");
		
		while (tokenizer.hasMoreElements())
		{
			
				folders.add((String) tokenizer.nextElement());
			

		}
		
		remoteListView.setItems(folders);
	}
	
}
