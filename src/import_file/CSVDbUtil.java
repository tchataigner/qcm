package import_file;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import import_file.CSVDbUtil;

public class CSVDbUtil {
	private static CSVDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static CSVDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new CSVDbUtil();
		}
		
		return instance;
	}
	
	private CSVDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	public void uploadCVS(String[] values) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
;
		
		int length = values.length;

		try {
			myConn = getConnection();

			String sql_question = "insert into question (text, media,difficulty) values (?,?,?)";

			myStmt = myConn.prepareStatement(sql_question, Statement.RETURN_GENERATED_KEYS);

			for (int i = 0; i < 3; i++){
			myStmt.setString(i+1, values[i]);
			}
			myStmt.execute();	
			
			ResultSet rs = myStmt.getGeneratedKeys();
			
			 if (rs.next()){
		            System.out.println(rs.getInt(1));
		        }
			 
			 myStmt.close();
			 
			 String sql_reponse = "insert into answer (text, correct, fk_question id) values (?,?,?)";
			 
			 myStmt = myConn.prepareStatement(sql_reponse);
			
				for (int i = 3;  i<length; i++){
					int y = 1;
					myStmt.setString(y, values[i]);
					i++;
					y++;
					myStmt.setString(y, values[i]);
					}
				myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
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
