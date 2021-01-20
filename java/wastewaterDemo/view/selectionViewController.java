package wastewaterTeam7.view;

import java.io.IOException;

import javafx.fxml.FXML;
import wastewaterTeam7.Main;
import wastewaterTeam7.user.userDAO;
import wastewaterTeam7.user.users;

public class selectionViewController {
	
	private Main main;
	
	@FXML
	private void goPlantInfo() throws IOException {
		main.showUpdateScene("plantInfo/plant.fxml");
	}
	@FXML
	private void goUser() throws IOException{
		main.showUpdateScene("user/addUser.fxml");
	}
	@FXML
	private void goContract() throws IOException{
		main.showUpdateScene("addContract/Contract.fxml");
	}
	@FXML
	private void goEquipment() throws IOException{
		main.showUpdateScene("Equipment/equipment.fxml");
	}
	@FXML
	private void goPIO() throws IOException{
		main.showUpdateScene("PIO/pio.fxml");	
	}
}
