package answer;

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

public class AnswerDbUtil {

	private static AnswerDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static AnswerDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new AnswerDbUtil();
		}
		
		return instance;
	}
	
	private AnswerDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Answer> getAnswers(int questionid) throws Exception {

		List<Answer> answers = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from answer where (fk_question_id = ?) order by rand();";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, questionid);
			
			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String text = myRs.getString("text");
				int correct = myRs.getInt("correct");
				int fk_question_id = myRs.getInt("fk_question_id");
				// create new student object
				Answer tempAnswer = new Answer(id, text, correct, fk_question_id);

				// add it to the list of students
				answers.add(tempAnswer);
			}
			
			return answers;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void addAnswer(Answer theAnswer) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		//System.out.println(theQuestion.getFk_matiere_id());
		try {
			myConn = getConnection();

			String sql = "insert into answer (text, correct, fk_question_id) values (?,?,?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theAnswer.getText());
			myStmt.setInt(2, theAnswer.getCorrect());
			myStmt.setInt(3, theAnswer.getFk_question_id());

			
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
