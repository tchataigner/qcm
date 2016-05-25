package evaluation;

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

import answer.Answer;
import question.Question;

public class EvaluationDbUtil {

	private static EvaluationDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	private int correct;

	public static EvaluationDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new EvaluationDbUtil();
		}

		return instance;
	}

	private EvaluationDbUtil() throws Exception {
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

			String sql = "select * from question where (fk_matiere_id = ?) order by rand();";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);

			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int id = myRs.getInt("id");
				String text = myRs.getString("text");
				String media = myRs.getString("media");
				int difficulty = myRs.getInt("difficulty");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");
				String commentaire = myRs.getString("commentaire");
				String aide = myRs.getString("aide");

				// create new student object
				Question tempQuestion = new Question(id, text, media, difficulty, fk_matiere_id, commentaire, aide);

				// add it to the list of students
				questions.add(tempQuestion);
			}

			return questions;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public List<Answer> getAnswers(int questionId) throws Exception {

		List<Answer> answers = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from answer where (fk_question_id = ?) order by id;";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, questionId);

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
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public int check(int answer_id) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from answer where (id = ?);";

			myStmt = myConn.prepareStatement(sql);

			// set params
			Answer theAnswer = null;
			myStmt.setInt(1, answer_id);

			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			if (myRs.next()) {
				this.correct = myRs.getInt(3);
			} 
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(myConn, myStmt, myRs);
			return correct;
		}
	}

	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();

		return theConn;
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
