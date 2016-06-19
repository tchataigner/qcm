package knowledge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import question.Question;

public class KnowledgeDbUtil {

	private static KnowledgeDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/qcm";
	
	public static KnowledgeDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new KnowledgeDbUtil();
		}
		
		return instance;
	}
	
	private KnowledgeDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Knowledge> getKnowledge(int matiereid) throws Exception {

		List<Knowledge> knowledge = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from knowledge where (fk_matiere_id = ?) order by id;";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, matiereid);
			
			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String name = myRs.getString("name");
				int open = myRs.getInt("open");
				int nbr_student = myRs.getInt("nbr_student");
				String created_at = myRs.getString("created_at");
				
				
				// create new student object
				Knowledge tempKnowledge = new Knowledge(id, name, open, nbr_student, created_at, matiereid);

				// add it to the list of students
				knowledge.add(tempKnowledge);
			}
			
			return knowledge;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void addKnowledge(Knowledge theKnowledge) throws Exception {

		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		//System.out.println(theQuestion.getFk_matiere_id());
		try {
			myConn = getConnection(); 

			String sql = "insert into knowledge (name, open, created_at, fk_matiere_id) values (?,?,?,?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// set params
			myStmt.setString(1, theKnowledge.getName());
			myStmt.setInt(2, 0);
			myStmt.setString(3, timeStamp);
			myStmt.setInt(4, theKnowledge.getFk_matiere_id());
			
			myStmt.execute();	

			ResultSet generatedKeys = myStmt.getGeneratedKeys();

			if (generatedKeys.next()) {
				System.out.println(generatedKeys.getInt(1));
				theKnowledge.setId(generatedKeys.getInt(1));
			}
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void addQuestionKnowledge(int i, int id) throws Exception {

		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		//System.out.println(theQuestion.getFk_matiere_id());
		try {
			myConn = getConnection(); 

			String sql = "insert into question_knowledge (fk_knowledge_id, fk_question_id, correct, nbr_answer) values (?,?,?,?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, id);
			myStmt.setInt(2, i);
			myStmt.setInt(3, 0);
			myStmt.setInt(4, 0);
			
			myStmt.execute();	
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteKnowledge(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from knowledge where id = ? ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void resetQuestionKnowledge(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update question_knowledge set correct = 0, nbr_answer = 0 where fk_knowledge_id = ?; ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void resetKnowledge(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update knowledge set nbr_student = 0 where (id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void startKnowledge(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update knowledge set open = 1 where (id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void stopKnowledge(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update knowledge set open = 0 where (id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public List<Integer> getKnowledgeQuestions(int knowledgeid) throws Exception {

		List<Integer> questions_id = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from question_knowledge where (fk_knowledge_id = ?) order by fk_question_id;";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, knowledgeid);

			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int id = myRs.getInt("fk_question_id");

				// add it to the list of students
				questions_id.add(id);
			}

			return questions_id;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}
	
	public Question getQuestions(int questionid) throws Exception {

		Question question = new Question();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from question where (id = ?);";

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
				String media = myRs.getString("media");
				int difficulty = myRs.getInt("difficulty");
				int fk_matiere_id = myRs.getInt("fk_matiere_id");
				String commentaire = myRs.getString("commentaire");
				String aide = myRs.getString("aide");

				// create new student object
				question.setId(id);
				question.setText(text);
				question.setMedia(media);
				question.setDifficulty(difficulty);
				question.setFk_matiere_id(fk_matiere_id);
				question.setCommentaire(commentaire);
				question.setAide(aide);
			}

			return question;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}
	
	public void incrementStudent(int knowledgeId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update knowledge set nbr_student = nbr_student + 1 where (id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, knowledgeId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void addAnswer(int questionId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update question_knowledge set nbr_answer = nbr_answer + 1 where (fk_question_id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, questionId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void correctAnswers(int questionId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update question_knowledge set correct = correct + 1 where (fk_question_id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, questionId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void incorrectAnswers(int questionId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update question_knowledge set incorrect = incorrect + 1 where (fk_question_id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, questionId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public void noAnswer(int questionId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update question_knowledge set no_answer = no_answer + 1 where (fk_question_id = ?) ";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, questionId);

			myStmt.execute();
		} finally {
			close(myConn, myStmt);
		}
	}
	
	public List<List<Integer>> getKnowledgeQuestionsStats(int knowledgeid) throws Exception {
		List<List<Integer>> questions_id = new ArrayList<>();
		

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = "select * from question_knowledge where (fk_knowledge_id = ?) order by fk_question_id;";

			myStmt = myConn.prepareStatement(sql);

			// set params

			myStmt.setInt(1, knowledgeid);

			myStmt.execute();
			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				List<Integer> temp = new ArrayList<>();

				// retrieve data from result set row
				int fk_question_id = myRs.getInt("fk_question_id");
				temp.add(fk_question_id);
				int correct = myRs.getInt("correct");
				temp.add(correct);
				int incorrect  = myRs.getInt("incorrect");
				temp.add(incorrect);
				int no_answer = myRs.getInt("no_answer");
				temp.add(no_answer);
				// add it to the list of students
				questions_id.add(temp);
			}

			return questions_id;
		} finally {
			close(myConn, myStmt, myRs);
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
