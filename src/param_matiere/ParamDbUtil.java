package param_matiere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import question.Question;

public class ParamDbUtil {
	
	private static ParamDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static ParamDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new ParamDbUtil();
		}
		
		return instance;
	}
	
	private ParamDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	/*add duration*/
	
	public void addParamDuration(Duration theDuration) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into time_eval (hour, min, fk_matiere_id) values (?,?,?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, theDuration.getHour());
			myStmt.setInt(2, theDuration.getMin());
			myStmt.setInt(3, theDuration.getFk_matiere_id());
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	/* get duration */
	
	public int[] getDuration(int matiereid) throws Exception {

		int[] duration = new int[2];

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from time_eval where (fk_matiere_id = ?);";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);
			
			myStmt.execute();
			myRs = myStmt.executeQuery();
			System.out.println(myRs);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				int hour = myRs.getInt("hour");
				int min = myRs.getInt("min");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");

				// add it to the list of students
				duration[0] = hour;
				duration[1] = min;
			}
			
			return duration;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	

}
