package wastewaterTeam7.addContract;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import wastewaterTeam7.Main;
import wastewaterTeam7.Equipment.equipment;
import wastewaterTeam7.Equipment.equipmentDAO;
import wastewaterTeam7.plantInfo.plantDAO;

public class contractController {
	
	@FXML
	DatePicker startDate;
	@FXML
	DatePicker endDate;
	@FXML
	ComboBox friendlyTag;
	@FXML
	TextField txtOwner;
	@FXML
	TextField txtSubContractor;
	@FXML
	private TableColumn<contract, Integer> clmPlantID;
	@FXML
	private TableColumn<contract, Integer> clmContractID;
	@FXML
	private TableColumn<contract, Integer> clmStart;
	@FXML
	private TableColumn<contract, Integer> clmEnd;
	@FXML
	private TableColumn<contract, String> clmOwner;
	@FXML
	private TableColumn<contract, String> clmSub;
	@FXML
	private TableView conTable;
	
	@FXML
	public void addContract(ActionEvent event) throws ClassNotFoundException, SQLException {
		
		
		String startD, endD;
		int stDate = 0000, edDate = 0000, PlantID = 0;
		
		startD = startDate.getValue().toString();
		String[] startPart = startD.split("-");
		stDate = Integer.parseInt(startPart[0].toString());
		endD = endDate.getValue().toString();
		String[] endPart = endD.split("-");
		edDate = Integer.parseInt(endPart[0].toString());
		
		contractDAO.insertContract((String)friendlyTag.getValue(), txtOwner.getText(), txtSubContractor.getText(), stDate, edDate);
		initialize();
	}
	
	
	public void initialize() throws ClassNotFoundException, SQLException {
		
		ObservableList<String> frdlySelect = FXCollections.observableArrayList();
		frdlySelect = plantDAO.getFriendlyTag();
		
		friendlyTag.setItems(frdlySelect);
		friendlyTag.setValue("DV");


		
		clmPlantID.setCellValueFactory(cellData -> cellData.getValue().getPlantID().asObject());
		clmContractID.setCellValueFactory(cellData -> cellData.getValue().getContractNum().asObject());
		clmStart.setCellValueFactory(cellData -> cellData.getValue().getStart().asObject());
		clmEnd.setCellValueFactory(cellData -> cellData.getValue().getEnd().asObject());
		clmOwner.setCellValueFactory(cellData -> cellData.getValue().getOwner());
		clmSub.setCellValueFactory(cellData -> cellData.getValue().getSubcontractor());
		
		ObservableList<contract> conList = contractDAO.getAllRecords();
		populateTable(conList);
	}
	private void populateTable(ObservableList<contract> conList){
		conTable.setItems(conList);
	}
}
