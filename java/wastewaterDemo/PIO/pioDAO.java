package wastewaterTeam7.PIO;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wastewaterTeam7.Connectivity.ConnectionClass;
import wastewaterTeam7.Equipment.equipment;
import wastewaterTeam7.Equipment.equipmentDAO;
import wastewaterTeam7.user.users;

public class pioDAO {
	
	
	public static void insertPIO(String eqTag, String pointType,  int rack, int slot, int channel)  throws SQLException, ClassNotFoundException{
		
		String rsch, eqPIO;
	
		equipment eq = equipmentDAO.getEqipment(eqTag);
		
		rsch = "R."+rack+"S."+slot+"CH."+channel;
		eqPIO = eq.getEQ() + "." + pointType;
		
		String sql = "insert into plcpio (plantID, plcID, rack, slot, channel, rsch, eqTag, eqPIO) "
				+ "values("+eq.getPID()+", '"+eq.getPLC()+"', "+rack+" , "+ slot+", "+ channel +", '" +rsch+"', '"+eq.getEQ()+"', '"+eqPIO+"')";
		try {
			ConnectionClass.dbExcecuteQuery(sql);
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
	}
	
	
	//get all pio
	public static ObservableList<pio> getAllRecords()throws ClassNotFoundException, SQLException{
		String sql = "select * from plcpio";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<pio> pioList = getPioObjects(rsSet);
			return pioList;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static ObservableList<pio> getPioObjects(ResultSet rsSet){
		
		try {
			ObservableList<pio> pioList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				pio ps = new pio();
				ps.setPID(rsSet.getString("plcID"));
				ps.setRS(rsSet.getString("rsch"));
				ps.setEQ(rsSet.getString("eqTag"));
				ps.setPio(rsSet.getString("eqPIO"));
				ps.setPlt(rsSet.getInt("plantID"));
				ps.setRK(rsSet.getInt("rack"));
				ps.setST(rsSet.getInt("slot"));
				ps.setCH(rsSet.getInt("channel"));
				pioList.add(ps);
			}
			return pioList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	
	
}
