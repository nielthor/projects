package wastewaterTeam7.user;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import wastewaterTeam7.Main;

public class addUserController {
	
	private Main main;
	private int opVal, deVal, adVal;
	
	@FXML
	TextField txtFirstName, txtLastName, txtUserName, txtPassword;
	@FXML
	RadioButton rdoOperator, rdoDeveloper, rdoAdmin;
	@FXML
	private TableColumn<users, String> clmUName;
	@FXML
	private TableColumn<users, String> clmFName;
	@FXML
	private TableColumn<users, String> clmLName;
	@FXML
	private TableColumn<users, String> clmAType;
	@FXML
	private TableColumn<users, String> clmPWord;
	@FXML
	private TableView userTable;
	
	@FXML
	 private void userAdd(ActionEvent event) throws Exception {
		if (rdoAdmin.isSelected() == true) {
			opVal = 0;
			deVal = 0;
			adVal = 1;
			
		}
		else if(rdoDeveloper.isSelected() == true) {
			opVal = 0;
			deVal = 1;
			adVal = 0;
		}
		else {
			opVal = 1;
			deVal = 0;
			adVal = 0;
		}
			
		userDAO.insertUser(txtFirstName.getText(), txtLastName.getText(), txtUserName.getText(), txtPassword.getText(), opVal, deVal, adVal);
		initialize();
	}
	
	private void populateTable(ObservableList<users> usrList){
		userTable.setItems(usrList);
	}
	@FXML
	private void deleteUser(ActionEvent event) throws Exception{
		userDAO.deleteUser(txtUserName.getText());
		initialize();
		
	}
	@FXML 
	void initialize()throws Exception{
		clmUName.setCellValueFactory(cellData -> cellData.getValue().getUsrName());
		clmFName.setCellValueFactory(cellData -> cellData.getValue().getFirstName());
		clmLName.setCellValueFactory(cellData -> cellData.getValue().getLastName());
		clmPWord.setCellValueFactory(cellData -> cellData.getValue().getPasswrd());
		clmAType.setCellValueFactory(cellData -> cellData.getValue().getAccount());
		ObservableList<users> userList = userDAO.getAllRecords();
		populateTable(userList);
		
	}
}
