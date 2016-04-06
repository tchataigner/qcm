package matiere;

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

import matiere.Matiere;

public class MatiereDbUtil {

	private static MatiereDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static MatiereDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new MatiereDbUtil();
		}
		
		return instance;
	}
	
	private MatiereDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Matiere> getMatieres() throws Exception {

		List<Matiere> matieres = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from matiere order by name";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String name = myRs.getString("name");

				// create new student object
				Matiere tempMatiere = new Matiere(id, name);

				// add it to the list of students
				matieres.add(tempMatiere);
			}
			
			return matieres;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void addMatiere(Matiere theMatiere) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into matiere (name) values (?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theMatiere.getName());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteMatiere(int matiereId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from matiere where id = ? ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, matiereId);
			
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
