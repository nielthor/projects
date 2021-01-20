package wastewaterTeam7;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private static BorderPane mainLayout;
	

	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Teams7 Wastewater Database GUI");
		
		showUserView();
		showPlantScene();
	}
	private void showUserView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/userView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
	public static void showSelectionView(Boolean login) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/selectionView.fxml"));
		BorderPane slcView = loader.load();
		if(login == true)
			mainLayout.setLeft(slcView);


	}
	public static void showPlantScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		//FXMLLoader loader1 = new FXMLLoader();
		loader.setLocation(Main.class.getResource("plantInfo/plant.fxml"));
		BorderPane plt = loader.load();
		//loader1.setLocation(Main.class.getResource("view/selectionView.fxml"));
		//BorderPane slcView = loader1.load();
		//mainLayout.setLeft(slcView);
		mainLayout.setCenter(plt);
	}
	public static void showUpdateScene(String resLoc) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(resLoc));
		BorderPane newScene = loader.load();
		mainLayout.setCenter(newScene);
	}


	public static void main(String[] args) {
		launch(args);
	}
}
