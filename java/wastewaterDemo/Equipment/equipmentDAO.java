package wastewaterTeam7.Equipment;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wastewaterTeam7.Connectivity.ConnectionClass;
import wastewaterTeam7.plantInfo.plant;
import wastewaterTeam7.plantInfo.plantDAO;
import wastewaterTeam7.user.users;

public class equipmentDAO {

	public static void insertEqupment(int plantID, String plcID, String process, String deviceType, String deviceNum)  throws SQLException, ClassNotFoundException{
		
		plant plt = plantDAO.getPlant(plantID);
		
		String eqTag = plt.getFT() + "-" + plcID +"-"+ deviceType +"-" + deviceNum;
		
		String sql = "insert into user (plantID, plcID, process, eqTag) "
				+ "values("+plantID+", '"+plcID+"', '"+process+"' , '"+ eqTag+"')";
		try {
			ConnectionClass.dbExcecuteQuery(sql);
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
	}
	
	
	//get records
	public static ObservableList<equipment> getAllRecords()throws ClassNotFoundException, SQLException{
		String sql = "select * from plantequipment";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<equipment> eqList = getEQObjects(rsSet);
			return eqList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	

	private static ObservableList<equipment> getEQObjects(ResultSet rsSet){
		
		try {
			ObservableList<equipment> eqList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				equipment eq = new equipment();
				eq.setPID(rsSet.getInt("plantID"));
				eq.setEQ(rsSet.getString("eqTag"));
				eq.setPLC(rsSet.getString("plcID"));
				eq.setPName(rsSet.getString("process"));
				eqList.add(eq);
			}
			return eqList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	
	//EQTag records
	public static ObservableList<String> getEQTag()throws ClassNotFoundException, SQLException{
		String sql = "select eqTag from plantequipment";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<String> eqList = getTagObjects(rsSet);
			return eqList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	

	private static ObservableList<String> getTagObjects(ResultSet rsSet){
		
		try {
			ObservableList<String> eqList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				
				eqList.add(rsSet.getString("eqTag"));
			}
			return eqList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	
	//equipment info
	public static equipment getEqipment(String eqTag)throws ClassNotFoundException, SQLException{
		String sql = "select * from plantequipment where eqTag='"+eqTag+"'";
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			equipment eq = getEQData(rsSet);
			return eq;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static equipment getEQData(ResultSet rsSet) {
		try {
			equipment eq = new equipment();
			while(rsSet.next()) {	
				eq.setPID(rsSet.getInt("plantID"));
				eq.setEQ(rsSet.getString("eqTag"));
				eq.setPLC(rsSet.getString("plcID"));
				eq.setPName(rsSet.getString("process"));
			}
			return eq;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}

}
