package wastewaterTeam7.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wastewaterTeam7.Connectivity.ConnectionClass;

public class userDAO {
	
	public static void insertUser(String fName, String lName, String userName, String uPassword, int operator, int developer, int admin)  throws SQLException, ClassNotFoundException{
		String sql = "insert into user (firstName, lastName, userName, password, operator, developer, admin) "
				+ "values('"+fName+"', '"+lName+"', '"+userName+"' , '"+ uPassword+"', "+ operator +", " +developer+", "+admin+")";
		try {
			ConnectionClass.dbExcecuteQuery(sql);
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
	}
	public static ObservableList<users> getAllRecords()throws ClassNotFoundException, SQLException{
		String sql = "select * from user";
		
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			ObservableList<users> userList = getUserObjects(rsSet);
			return userList;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	

	private static ObservableList<users> getUserObjects(ResultSet rsSet){
		
		try {
			ObservableList<users> userList = FXCollections.observableArrayList();
			while(rsSet.next()) {
				users usr = new users();
				usr.setUserName(rsSet.getString("userName"));
				usr.setFName(rsSet.getString("firstName"));
				usr.setLName(rsSet.getString("lastName"));
				usr.setPassword(rsSet.getString("password"));
				usr.setOpInt(rsSet.getInt("operator"));
				usr.setDevInt(rsSet.getInt("developer"));
				usr.setAdInt(rsSet.getInt("admin"));
				userList.add(usr);
			}
			return userList;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
	public static void deleteUser(String userName)throws ClassNotFoundException, SQLException{
		String sql="delete from user where userName='"+userName+"'";
		try {
			ConnectionClass.dbExcecuteQuery(sql);
		}
		catch(SQLException se) {
			System.out.println("Exception while deleting data" +se);
			se.printStackTrace();
		}
	}
	
	
	//user validation
	public static users getUser(String uname)throws ClassNotFoundException, SQLException{
		String sql = "select password, operator, developer, admin from user where userName='"+uname+"'";
		try {
			ResultSet rsSet = ConnectionClass.dbExecute(sql);
			users usr = getUserPassword(rsSet);
			return usr;
		}
		catch(SQLException se) {
			System.out.println("Exception while inserting data" +se);
			se.printStackTrace();
		}
		return null;
	}
	private static users getUserPassword(ResultSet rsSet) {
		try {
			users usr = new users();
			while(rsSet.next()) {	
				usr.setPassword(rsSet.getString("password"));
				usr.setOpInt(rsSet.getInt("operator"));
				usr.setDevInt(rsSet.getInt("developer"));
				usr.setAdInt(rsSet.getInt("admin"));

			}
			return usr;
		}
		catch(SQLException se) {
			System.out.println("Exception while fetching data" +se);
			se.printStackTrace();
		}
		return null;
	}
}
