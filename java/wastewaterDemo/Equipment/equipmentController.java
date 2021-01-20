package wastewaterTeam7.Equipment;

import javafx.event.ActionEvent;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import wastewaterTeam7.plantInfo.plantDAO;
import wastewaterTeam7.user.userDAO;
import wastewaterTeam7.user.users;

public class equipmentController {
	
	@FXML
	ComboBox plantId;
	@FXML
	ComboBox plcID;
	@FXML
	ComboBox process;
	@FXML
	ComboBox friendlyTag;
	@FXML
	ComboBox deviceType;
	@FXML
	TextField deviceNum;
	@FXML
	private TableColumn<equipment, Integer> clmPlantID;
	@FXML
	private TableColumn<equipment, String> clmPLCID;
	@FXML
	private TableColumn<equipment, String> clmProcess;
	@FXML
	private TableColumn<equipment, String> clmEQTag;
	@FXML
	private TableView eqTable;
	
	@FXML
	public void addDevice(ActionEvent event) throws ClassNotFoundException, SQLException {
		int pID = Integer.parseInt((String) plantId.getValue());
		equipmentDAO.insertEqupment(pID, (String)plcID.getValue(), (String)process.getValue(), (String)deviceType.getValue(), deviceNum.getText());
	}
	
	
	public void initialize() throws ClassNotFoundException, SQLException {
		
		ObservableList<String> frdlySelect = FXCollections.observableArrayList();
		frdlySelect = plantDAO.getFriendlyTag();
		
		ObservableList<String> pltSelect = FXCollections.observableArrayList("1","2","3");
		ObservableList<String> pltIDSelect = FXCollections.observableArrayList("1","2","3","4","5");
		ObservableList<String> plcSelect = FXCollections.observableArrayList("1","2","3");
		ObservableList<String> processSelect = FXCollections.observableArrayList("Inluent", "Grit", "Sedimetation");
		ObservableList<String> deviceTypeSelect = FXCollections.observableArrayList("PMP","VLV","FCV","MTR","LIT","AIT","PIT");
	
		friendlyTag.setItems(frdlySelect);
		friendlyTag.setValue("DV");
		plcID.setItems(pltIDSelect);
		plcID.setValue("1");
		plantId.setValue("1");
		plantId.setItems(pltSelect);
		process.setValue("Inluent");
		process.setItems(processSelect);
		deviceType.setValue("PMP");
		deviceType.setItems(deviceTypeSelect);

		
		clmPlantID.setCellValueFactory(cellData -> cellData.getValue().getPantID().asObject());
		clmPLCID.setCellValueFactory(cellData -> cellData.getValue().getPLCID());
		clmProcess.setCellValueFactory(cellData -> cellData.getValue().getProcessName());
		clmEQTag.setCellValueFactory(cellData -> cellData.getValue().getEqTag());
		ObservableList<equipment> eqList = equipmentDAO.getAllRecords();
		populateTable(eqList);
	}
	private void populateTable(ObservableList<equipment> eqList){
		eqTable.setItems(eqList);
	}

}
