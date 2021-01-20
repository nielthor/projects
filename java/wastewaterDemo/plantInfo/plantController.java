package wastewaterTeam7.plantInfo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class plantController{
	
	@FXML
	private TableView plantTable;
	@FXML
	private TableColumn<plant, String> plantName;
	@FXML
	private TableColumn<plant, String> plantOp;
	@FXML
	private TableColumn<plant, String> plantLoc;
	@FXML
	private TableColumn<plant, String> plantAd;
	@FXML
	private TableColumn<plant, String> plantPh;
	@FXML
	private TableColumn<plant, String> plantEm;
	@FXML
	private TableColumn<plant, Integer> plantID;
	
	private void populateTable(ObservableList<plant> plantList){
		plantTable.setItems(plantList);
	}
	@FXML 
	void initialize()throws Exception{
		plantName.setCellValueFactory(cellData -> cellData.getValue().getPlantName());
		plantOp.setCellValueFactory(cellData -> cellData.getValue().getOperationType());
		plantLoc.setCellValueFactory(cellData -> cellData.getValue().getPlantLocation());
		plantAd.setCellValueFactory(cellData -> cellData.getValue().getPlantAddress());
		plantPh.setCellValueFactory(cellData -> cellData.getValue().getPlantPhone());
		plantEm.setCellValueFactory(cellData -> cellData.getValue().getPlantEmail());
		plantID.setCellValueFactory(cellData -> cellData.getValue().getPlantID().asObject());
		ObservableList<plant> plantList = plantDAO.getAllRecords();
		populateTable(plantList);
		
	}
}
