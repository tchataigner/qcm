package knowledge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import answer.Answer;
import answer.AnswerDbUtil;
import evaluation.Evaluation;
import evaluation.EvaluationDbUtil;
import question.Question;

@ManagedBean
@SessionScoped
public class KnowledgeController {

	private int knowledgeid;
	private List<Knowledge> knowledge;
	private List<Question> questions;
	private KnowledgeDbUtil knowledgeDbUtil;
	private AnswerDbUtil answersDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public KnowledgeController() throws Exception {
		knowledge = new ArrayList<>();
		questions = new ArrayList<>();

		knowledgeDbUtil = KnowledgeDbUtil.getInstance();
	}

	public List<Knowledge> getKnowledge() {
		return knowledge;
	}

	public void loadKnowledge(int matiereid) {

		logger.info("Chargement des questions");

		knowledge.clear();

		try {

			// get all students from database
			knowledge = knowledgeDbUtil.getKnowledge(matiereid);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	public String addKnowledge(Knowledge theKnowledge) {
		logger.info("Adding Question: " + theKnowledge);

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));

		theKnowledge.setFk_matiere_id(fk_matiere_id);

		try {
			knowledgeDbUtil.addKnowledge(theKnowledge);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/knowledge/add-question_knowledge-form";
	}

	public String addQuestionKnowledge(Knowledge theKnowledge) {
		logger.info("Adding Question: " + theKnowledge);
		FacesContext fc = FacesContext.getCurrentInstance();
		int id = Integer.parseInt(getKnowledgeIdParam(fc));

		try {
			for (int i : theKnowledge.getQuestions()) {
				knowledgeDbUtil.addQuestionKnowledge(i, id);
			}

		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/index?faces-redirect=true";
	}

	public String deleteKnowledge() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		int knowledgeId = Integer.parseInt(params.get("id"));
		try {

			knowledgeDbUtil.deleteKnowledge(knowledgeId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + knowledgeId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "/question/list-knowledge.xhtml?faces-redirect=true";
	}

	public void startKnowledge() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		int knowledgeId = Integer.parseInt(params.get("id"));
		try {
			knowledgeDbUtil.resetQuestionKnowledge(knowledgeId);
			knowledgeDbUtil.resetKnowledge(knowledgeId);
			knowledgeDbUtil.startKnowledge(knowledgeId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + knowledgeId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

		}

	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void loadQuestions() {

		logger.info("Chargement des questions");

		questions.clear();

		knowledgeid = Integer
				.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
		List<Integer> questions_id = new ArrayList<>();
		try {

			// get all students from database
			questions_id = knowledgeDbUtil.getKnowledgeQuestions(knowledgeid);

			for (int i : questions_id) {
				questions.add(knowledgeDbUtil.getQuestions(i));
			}

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public String updateKnowledge(Evaluation evaluation) {
		List<Integer> good_answers = new ArrayList<>();
		List<Integer> correct_answers = new ArrayList<>();
		List<Integer> wrong_answers = new ArrayList<>();

		try {

			// Create instances of the db to access info
			answersDbUtil = AnswerDbUtil.getInstance();

			knowledgeDbUtil.incrementStudent(knowledgeid);
			// Get the map of the evaluation
			Map<Integer, String[]> eval = evaluation.getEval();

			// For each entry (a question) :
			for (Entry<Integer, String[]> entry : eval.entrySet()) {
				// Get the id of the question (to get the correct answers
				// later)
				int question = entry.getKey();

				// We get all the answers
				if (entry.getValue().length != 0) {
					System.out.println("########################################### " + question
							+ " answered ########################################");

					knowledgeDbUtil.addAnswer(question);

					String[] strArray = (String[]) entry.getValue();

					// Transform them in int value
					int[] test = Arrays.asList(strArray).stream().mapToInt(Integer::parseInt).toArray();

					List<Integer> answers = new ArrayList<Integer>();

					// Transform the array of answers in a list of answers
					for (int index = 0; index < test.length; index++) {
						answers.add(test[index]);
					}

					// Get the answers of a question
					List<Answer> correct_answers_list = answersDbUtil.getAnswers(question);

					List<Integer> correct_answer = new ArrayList<Integer>();
					// For each answers we check if it is true or false
					for (Answer a : correct_answers_list) {
						if (a.getCorrect() == 1) {
							// We add the good answer in the list to compare the
							// student's answer list and the good answer's list
							correct_answer.add(a.getId());

							good_answers.add(a.getId());
						}
					}

					for (int a : answers) {
						if (!(correct_answer.contains(a))) {
							wrong_answers.add(a);
						}
					}
					for (int a : correct_answer) {
						if (answers.contains(a)) {
							correct_answers.add(a);
						}
					}

					// We check if the size is the same (if not question is
					// marked as wrong)
					if (answers.size() == correct_answer.size()) {
						System.out.println(correct_answer.isEmpty());
						int btw = 0;
						/*
						 * For each good answers we check if every one of them
						 * is in the student's answer list if yes we inc btw of
						 * 1 if not the question is wrong
						 */
						for (int a : correct_answer) {
							if (answers.contains(a)) {
								btw++;
							}
						}
						/*
						 * If every thing is fine btw should be the size of
						 * correct_answer So we add 1 too the total of right
						 * questions
						 */
						if (btw == correct_answer.size() && btw != 0) {
							System.out.println("########################################### " + question
									+ " correct answer ########################################");

							knowledgeDbUtil.correctAnswers(question);
						} else {
							System.out.println("########################################### " + question
									+ " incorrect answer ########################################");

							knowledgeDbUtil.incorrectAnswers(question);
						}

					} else {
						System.out.println("########################################### " + question
								+ " incorrect answer ########################################");

						knowledgeDbUtil.incorrectAnswers(question);
					}
				} else {
					System.out.println("########################################### " + question
							+ " no answer ########################################");
					knowledgeDbUtil.noAnswer(question);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/test/auto_evaluation_correction.xhtml?faces-redirect=true";
	}

	public String stopKnowledge(Knowledge theKnowledge) {
		List<List<Integer>> questions_knowledge = new ArrayList<>();
		List<Question> questions = new ArrayList<>();
		
		
		try {
			
			knowledgeDbUtil.stopKnowledge(theKnowledge.getId());
			
			questions_knowledge = knowledgeDbUtil.getKnowledgeQuestionsStats(theKnowledge.getId());
			System.out.println("Siiiiiiiiiiizzzzzzzeeeeeeee : "+questions_knowledge.size());
			System.out.println("111111111111111111111111111111 : "+questions_knowledge.get(1));
			for(List<Integer> question_knowledge : questions_knowledge){
				Question question = knowledgeDbUtil.getQuestions(question_knowledge.get(0));
				questions.add(question);
				System.out.println(question.getId());
				
				int question_knowledge1 = (question_knowledge.get(1)*100)/theKnowledge.getNbr_student();
				question_knowledge.set(1, question_knowledge1);
				System.out.println("question_knowledge[1] " + question_knowledge.get(1));
				int question_knowledge2 = (question_knowledge.get(2)*100)/theKnowledge.getNbr_student();
				question_knowledge.set(2, question_knowledge2);
				System.out.println("question_knowledge[4] " + question_knowledge.get(2));
				int question_knowledge3 = (question_knowledge.get(3)*100)/theKnowledge.getNbr_student();
				question_knowledge.set(3, question_knowledge3);
				System.out.println("question_knowledge[3] " + question_knowledge.get(3));
			}
			
			
			
			
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			// Put the result in request map
			Map<String, Object> requestMap = externalContext.getRequestMap();
			
			requestMap.put("knowledge", theKnowledge);
			requestMap.put("questions", questions);
			requestMap.put("questions_knowledge", questions_knowledge);
			
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + theKnowledge.getId(), exc);

			// add error message for JSF page
			addErrorMessage(exc);

		}
		return "/knowledge/knowledge-correction.xhtml";
	}

	public String getMatiereIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_matiere_id");

	}

	public String getKnowledgeIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_knowledge_id");

	}

}