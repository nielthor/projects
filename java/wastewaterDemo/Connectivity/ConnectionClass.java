package wastewaterTeam7.Connectivity;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import com.mysql.cj.jdbc.result.ResultSetImpl;

/*
 * B2 tech
 * https://www.youtube.com/watch?v=L8iuBXl-F8U
 * christian hur
 * https://www.youtube.com/watch?v=S64-VWUmO4U
 * Genius Coders
 * https://www.youtube.com/watch?v=20U-XnY4Opo
 * 
 */

public class ConnectionClass {
	
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static Connection connection = null;
	private static final String connStr = "jdbc:mysql://localhost/team7";
	
	public static void dbConnect() throws SQLException, ClassNotFoundException{
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(connStr, "root", "");
			System.out.println("JDBC Driver Found");
			
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println("Driver Missing");
			cnfe.printStackTrace();
			throw cnfe;
		}
		catch(SQLException se) {
			System.out.println("Connection Failed");
			throw se;
		}
		
	}

	public static void dbDisconnect()throws SQLException{
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		catch(SQLException se) {
			System.out.println("Connection Failed");
			throw se;
		}
	}
	//add, update, delete records
	public static void dbExcecuteQuery(String sqlStmt)throws SQLException, ClassNotFoundException{
		Statement stmt = null;
		try {
			dbConnect();
			stmt = connection.createStatement();
			stmt.executeUpdate(sqlStmt);
			
		}
		catch(SQLException se) {
			System.out.println("Execute Faild " + se );
			throw se;
		}
		finally {
			if(stmt != null)
					stmt.close();
			dbDisconnect();			
		}
	}
	
	//get records
	public static ResultSet dbExecute(String sqlQuery)throws SQLException, ClassNotFoundException{
		Statement stmt = null;
		ResultSet rs = null;
		CachedRowSet crs = null;
		
		try {
			dbConnect();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlQuery);
			crs = RowSetProvider.newFactory().createCachedRowSet();
			crs.populate(rs);
		}
		catch(SQLException se) {
			System.out.println("Execute Faild " + se );
			throw se;
		}
		finally {
			if(rs !=null)
				rs.close();
			if (stmt != null)
				stmt.close();
			dbDisconnect();					
		}	
		return crs;
	}
}
