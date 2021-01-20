package wastewaterTeam7.plantInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wastewaterTeam7.Connectivity.ConnectionClass;
import wastewaterTeam7.user.users;

public class plantDAO {
	
	public static ObservableList<plant> getAllRecords()throws ClassNotFoundException, SQLException{
		String sql = "select * from plantinformation";
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<plant> plantList = getPlantObjects(rsSet);
			return plantList;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static ObservableList<plant> getPlantObjects(ResultSet rsSet){
		
		try {
			ObservableList<plant> plantList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				plant plt = new plant();
				plt.setPID(rsSet.getInt("plantID"));
				plt.setName(rsSet.getString("plantName"));
				plt.setOp(rsSet.getString("opType"));
				plt.setLoc(rsSet.getString("location"));
				plt.setAd(rsSet.getString("address"));
				plt.setPh(rsSet.getString("phone"));
				plt.setEm(rsSet.getString("email"));
				plantList.add(plt);
			}
			return plantList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	
	//plant info
	public static plant getPlant(int plcID)throws ClassNotFoundException, SQLException{
		String sql = "select * from user where plantID="+plcID+"";
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			plant plt = getPlantData(rsSet);
			return plt;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static plant getPlantData(ResultSet rsSet) {
		try {
			plant plt = new plant();
			while(rsSet.next()) {	
				plt.setPID(rsSet.getInt("plantID"));
				plt.setName(rsSet.getString("plantName"));
				plt.setOp(rsSet.getString("opType"));
				plt.setLoc(rsSet.getString("location"));
				plt.setFT(rsSet.getString("friendlyName"));
				plt.setAd(rsSet.getString("address"));
				plt.setPh(rsSet.getString("phone"));
				plt.setEm(rsSet.getString("email"));
			}
			return plt;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	//Friendly records
	public static ObservableList<String> getFriendlyTag()throws ClassNotFoundException, SQLException{
		String sql = "select friendlyName from plantinformation";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<String> ftList = getTagObjects(rsSet);
			return ftList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	

	private static ObservableList<String> getTagObjects(ResultSet rsSet){
		
		try {
			ObservableList<String> ftList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				
				ftList.add(rsSet.getString("friendlyName"));
			}
			return ftList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}

	//get plant id
	public static Integer getID(String fTag)throws ClassNotFoundException, SQLException{
		String sql = "select plantId from plantinformation where friendlyName='"+fTag+"'";
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			return getPlantID(rsSet);
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static Integer getPlantID(ResultSet rsSet) {
		try {
			while(rsSet.next()) {	
				return rsSet.getInt("plantID");
			}
	
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	
}
