package wastewaterTeam7.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class addUser {
	
	public static ObservableList<users> getAllRecords() throws ClassNotFoundException, SQLException{
		String sql="select * from user";
		
		try {
			ResultSet rsSet = wastewaterTeam7.Connectivity.ConnectionClass.dbExecute(sql);
			ObservableList<users> useList = getUserObjects(rsSet);
			return useList;
		}
		catch(SQLException se) {
			System.out.println("Error occured while fetching records" + se);
			se.printStackTrace();
			throw se;
		}
	}

	private static ObservableList<users> getUserObjects(ResultSet rsSet) {
		ObservableList<users> usrList = FXCollections.observableArrayList();
		try {
			while(rsSet.next()) {
				users usr = new users();
				usr.setFName(rsSet.getString("firstName"));
				usr.setLName(rsSet.getString("lastName"));
				usrList.add(usr);
				
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return usrList;
	}
	

}
