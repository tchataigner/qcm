package section;

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

import section.Section;
import section.SectionDbUtil;

public class SectionDbUtil {
	private static SectionDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static SectionDbUtil getInstance ()  throws Exception {
		if (instance == null) {
			instance = new SectionDbUtil();
		}
		
		return instance;
	}

	
	private SectionDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Section> getSections(int fk_matiere_id) throws Exception {
 
		List<Section> sections = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from section where (fk_matiere_id = ?) order by id;";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, fk_matiere_id);
			System.out.println(myStmt);
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String name = myRs.getString("name");
				int matiere_id = myRs.getInt("fk_matiere_id");

				// create new student object
				Section tempSection = new Section(id, name, matiere_id);

				// add it to the list of students
				sections.add(tempSection);
			}
			
			return sections;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void addSection(Section theSection) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into section (name,fk_matiere_id) values (?,?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theSection.getName());
			myStmt.setInt(2, theSection.getFk_matiere_id());
			
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	public void deleteSection(int sectionId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from section where id = ? ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, sectionId);
			
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
