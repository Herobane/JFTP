package fr.herobane.jftp;

import fr.herobane.jftp.controllers.Controller;
import fr.herobane.jftp.views.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		
		launch(args);
		
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{

		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(View.class.getResource("frame.fxml"));
		Controller c = new Controller();
		loader.setController(c);
		
		Parent root = loader.load();
		
		Scene scene1 = new Scene(root, 680, 440);
		
		stage.setTitle("JFTP");
		stage.setScene(scene1);
		stage.show();

	}

}
