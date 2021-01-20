package wastewaterTeam7.PIO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import wastewaterTeam7.Equipment.equipment;
import wastewaterTeam7.Equipment.equipmentDAO;

public class pioController {

	@FXML
	private ComboBox eqTag;
	@FXML
	private ComboBox pointType; 
	@FXML
	private ComboBox rack; 
	@FXML
	private ComboBox slot; 
	@FXML
	private ComboBox channel;
	@FXML
	private TableColumn<pio, Integer> clmPlantID;
	@FXML
	private TableColumn<pio, Integer> clmRack;
	@FXML
	private TableColumn<pio, Integer> clmSlot;
	@FXML
	private TableColumn<pio, Integer> clmChannel;
	@FXML
	private TableColumn<pio, String> clmPLCID;
	@FXML
	private TableColumn<pio, String> clmRSCH;
	@FXML
	private TableColumn<pio, String> clmEQ;
	@FXML
	private TableColumn<pio, String> clmPIO;
	@FXML
	private TableView pioTable;
	
	@FXML
	private void addPio(ActionEvent event) throws ClassNotFoundException, SQLException, IOException{
		
		int rackVal=0, slotVal=0, channelVal=0;
		String rsch="";
		
		rsch = "R."+rack.getAccessibleText()+"S."+slot.getAccessibleText()+"CH."+channel.getAccessibleText();
		
		try {
			rackVal = Integer.parseInt((String) rack.getValue());
			slotVal = Integer.parseInt((String)slot.getValue());
			channelVal = Integer.parseInt((String)channel.getValue());
			
		}
		catch(Exception e){
			System.out.println("Parse Failed");
		}
		
		pioDAO.insertPIO((String)eqTag.getValue(), (String)pointType.getValue(), rackVal, slotVal, channelVal);
		initialize();
	}
	public void initialize() throws ClassNotFoundException, SQLException {
		
		ObservableList<String> eqTagSelect = FXCollections.observableArrayList();
		eqTagSelect = equipmentDAO.getEQTag();
		ObservableList<String> rackNum = FXCollections.observableArrayList("1","2","3");
		ObservableList<String> slotNum = FXCollections.observableArrayList("1","2","3","4","5");
		ObservableList<String> channelNum = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10");
		ObservableList<String> pType = FXCollections.observableArrayList("Start", "Stop", "RunFB");
		eqTag.setItems(eqTagSelect);
		rack.setValue("1");
		rack.setItems(rackNum);
		slot.setValue("1");
		slot.setItems(slotNum);
		channel.setValue("1");
		channel.setItems(channelNum);
		pointType.setValue("Start");
		pointType.setItems(pType);
		
		
		clmPlantID.setCellValueFactory(cellData -> cellData.getValue().getPlantID().asObject());
		clmRack.setCellValueFactory(cellData -> cellData.getValue().getRack().asObject());
		clmSlot.setCellValueFactory(cellData -> cellData.getValue().getSlot().asObject());
		clmChannel.setCellValueFactory(cellData -> cellData.getValue().getChannel().asObject());
		clmPLCID.setCellValueFactory(cellData -> cellData.getValue().getPLCID());
		clmRSCH.setCellValueFactory(cellData -> cellData.getValue().getRSCH());
		clmEQ.setCellValueFactory(cellData -> cellData.getValue().getEQTage());
		clmPIO.setCellValueFactory(cellData -> cellData.getValue().getDevicePIO());
		
		ObservableList<pio> pioList = pioDAO.getAllRecords();
		populateTable(pioList);
	}
	private void populateTable(ObservableList<pio> pioList){
		pioTable.setItems(pioList);
	}
}
