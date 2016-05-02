package question;

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

public class QuestionDbUtil {

	private static QuestionDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static QuestionDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new QuestionDbUtil();
		}
		
		return instance;
	}
	
	private QuestionDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Question> getQuestions(int matiereid) throws Exception {

		List<Question> questions = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from question where (fk_matiere_id = ?) order by id;";

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
				String text = myRs.getString("text");
				String media = myRs.getString("media");
				int difficulty = myRs.getInt("difficulty");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");

				// create new student object
				Question tempQuestion = new Question(id, text, media, difficulty, fk_matiere_id);

				// add it to the list of students
				questions.add(tempQuestion);
			}
			
			return questions;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void addQuestion(Question theQuestion) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		//System.out.println(theQuestion.getFk_matiere_id());
		try {
			myConn = getConnection();

			String sql = "insert into question (text, media, difficulty, fk_matiere_id, fk_type_id, fk_section_id) values (?,?,?,?,?,1)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theQuestion.getText());
			myStmt.setString(2, theQuestion.getMedia());
			myStmt.setInt(3, theQuestion.getDifficulty());
			myStmt.setInt(4, theQuestion.getFk_matiere_id());
			myStmt.setInt(5, theQuestion.getFk_type_id());
			
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
