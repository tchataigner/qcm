package param_matiere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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

			String sql = "update time_eval set hour = ?, min = ? where fk_matiere_id = ?";
			//String sql = "insert into time_eval (hour, min, fk_matiere_id) values (?,?,?)";
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
	
	/*add number of question*/
	
	public void addParamNbrQuestion(NbrQuestion theNbrQuestion) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			/*String sql = "update nbr_question set nbr = ? where fk_matiere_id = ?";*/
			String sql = "insert into nbr_question (nbr, fk_matiere_id) values (?,?)";
			
			myStmt = myConn.prepareStatement(sql);
			System.out.println(theNbrQuestion.getNbr());
			System.out.println(theNbrQuestion.getFk_matiere_id());
			// set params
			myStmt.setInt(1, theNbrQuestion.getNbr());
			myStmt.setInt(2, theNbrQuestion.getFk_matiere_id());
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
/*add notation*/
	
	public void addParamNotation(Notation theNotation) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			/*String sql = "update notation set correct_value = ?, incorrect_value = ?, noanswer_value = ? where fk_matiere_id = ?";*/
			String sql = "insert into notation (correct_value, incorrect_value, noanswer_value, fk_matiere_id) values (?,?,?,?)";
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, theNotation.getCorrect_value());
			myStmt.setInt(2, theNotation.getIncorrect_value());
			myStmt.setInt(3, theNotation.getNoanswer_value());
			myStmt.setInt(4, theNotation.getFk_matiere_id());
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	/* get duration */
	
	public Duration getDuration(int matiereid) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from time_eval where (fk_matiere_id = ?);";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);
			
			Duration theDuration = null;
			
			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				int hour = myRs.getInt("hour");
				int min = myRs.getInt("min");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");

				// add it to the list of students
				theDuration = new Duration(id, hour, min, fk_matiere_id);
			}
			
			return theDuration;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
/* get number of questions */
	
	public NbrQuestion getNbrQuestion(int matiereid) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from nbr_question where (fk_matiere_id = ?);";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);
			
			NbrQuestion theNbrQuestion = null;
			
			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				int nbr = myRs.getInt("nbr");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");

				// add it to the list of students
				theNbrQuestion = new NbrQuestion(id, nbr, fk_matiere_id);
			}
			
			return theNbrQuestion;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
/* get notation */
	
	public Notation getNotation(int matiereid) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from notation where (fk_matiere_id = ?);";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);
			
			Notation theNotation = null;
			
			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				int correct_value = myRs.getInt("correct_value");
				int incorrect_value = myRs.getInt("incorrect_value");
				int noanswer_value = myRs.getInt("noanswer_value");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");

				// add it to the list of students
				theNotation = new Notation(id, correct_value, incorrect_value, noanswer_value, fk_matiere_id);
			}
			
			return theNotation;		
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
