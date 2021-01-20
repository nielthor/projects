package wastewaterTeam7.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import wastewaterTeam7.Main;
import wastewaterTeam7.Connectivity.ConnectionClass;
import wastewaterTeam7.user.userDAO;
import wastewaterTeam7.user.users;

public class userViewController {
	
	private Main main;
	private boolean validLogin = false;
	@FXML
	Button btnLogin;
	@FXML
	Button btnPlantInfo;
	@FXML 
	Button btnContract;
	@FXML 
	Button btnEquipment;
	@FXML 
	Button btnPIO;
	@FXML 
	Button btnUser; 
	@FXML
	TextField txtUserName, txtPassword;
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
	@FXML
	 private void login(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		validLogin = false;
		users usr = userDAO.getUser(txtUserName.getText());
		initialize();
		if(usr.getPassword().equals(txtPassword.getText()) && !txtPassword.getText().equals("")) {
			validLogin = true;
			System.out.println(userDAO.getUser(txtUserName.getText()) +" : "+ txtPassword.getText());
			if(usr.getAdInt() == 1) {
				btnPlantInfo.setVisible(true);
				btnContract.setVisible(true);
				btnEquipment.setVisible(true);
				btnPIO.setVisible(true);
				btnUser.setVisible(true);
			}
			else if(usr.getDevInt() == 1) {
				btnPlantInfo.setVisible(true);
				btnContract.setVisible(true);
				btnEquipment.setVisible(true);
				btnPIO.setVisible(true);
				btnUser.setVisible(false);
			}
			else {
				btnPlantInfo.setVisible(true);
				btnContract.setVisible(true);
				btnEquipment.setVisible(false);
				btnPIO.setVisible(false);
				btnUser.setVisible(false);
			}
				
				
		}
					
				
		txtPassword.setText(null);
		
		if (validLogin == true) {
			//main.showSelectionView(validLogin);
			validLogin = false;
			btnLogin.textProperty().set(txtUserName.getText() + " Logout");
			txtUserName.setVisible(false);
			txtPassword.setVisible(false);
		}		 
		else {
			main.showUpdateScene("plantInfo/plant.fxml");
			main.showSelectionView(validLogin);
			btnLogin.textProperty().set("Login");
			txtUserName.setVisible(true);
			txtPassword.setVisible(true);
			btnPlantInfo.setVisible(true);
			btnContract.setVisible(false);
			btnEquipment.setVisible(false);
			btnPIO.setVisible(false);
			btnUser.setVisible(false);
			
		}
					
	 }
	@FXML 
	public void initialize(){
		btnContract.setVisible(false);
		btnEquipment.setVisible(false);
		btnPIO.setVisible(false);
		btnUser.setVisible(false);
		
	}
	

}
