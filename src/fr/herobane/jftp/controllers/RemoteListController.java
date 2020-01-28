package fr.herobane.jftp.controllers;

import javafx.collections.ObservableList;

public class RemoteListController extends Controller
{

	public void initialize()
	{
		
	}
	
	public void fill(ObservableList<String> items)
	{
		super.remoteListView.setItems(items);
	}
	
}
