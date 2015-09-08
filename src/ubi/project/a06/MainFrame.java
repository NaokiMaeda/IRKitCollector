package ubi.project.a06;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainFrame extends Application{
	//UI関係
	private	FrameController	controller;
	private	FXMLLoader			loader;
	private	GridPane				content;
	private	Scene					scene;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		loader		= new FXMLLoader(getClass().getResource("MainFrame.fxml"));
		content	= loader.load();
		controller	= (FrameController)loader.getController();
		scene		= new Scene(content , 800 , 600);
		stage.setTitle("IRKit Collector");
		stage.setScene(scene);
		stage.show();
	}

}
