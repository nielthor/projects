package wastewaterTeam7.addContract;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wastewaterTeam7.Connectivity.ConnectionClass;
import wastewaterTeam7.Equipment.equipment;
import wastewaterTeam7.Equipment.equipmentDAO;
import wastewaterTeam7.PIO.pio;
import wastewaterTeam7.plantInfo.plantDAO;

public class contractDAO {
	
	public static void insertContract(String friendlyTag, String owner,  String sub, int start, int end)  throws SQLException, ClassNotFoundException{

		
		int PlantID = plantDAO.getID(friendlyTag);
		
		String sql = "insert into contract (plantID, contactOwner, start, end, subContractor) "
				+ "values("+PlantID+", '"+owner+"', "+start+" , "+ end+", '"+ sub +"')";
		try {
			ConnectionClass.dbExcecuteQuery(sql);
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
	}
	
	
	//get all contracts
	public static ObservableList<contract> getAllRecords()throws ClassNotFoundException, SQLException{
		String sql = "select * from contract";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<contract> conList = getConObjects(rsSet);
			return conList;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static ObservableList<contract> getConObjects(ResultSet rsSet){
		
		try {
			ObservableList<contract> conList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				contract cn = new contract();
				cn.setPID(rsSet.getInt("plantID"));
				cn.setCo(rsSet.getInt("contractID"));
				cn.setSt(rsSet.getInt("start"));
				cn.setEd(rsSet.getInt("end"));
				cn.setOw(rsSet.getString("contactOwner"));
				cn.setSb(rsSet.getString("subContractor"));
				conList.add(cn);
			}
			return conList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
}
