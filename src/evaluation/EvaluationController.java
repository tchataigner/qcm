package evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import answer.Answer;
import answer.AnswerDbUtil;
import question.Question;

@ManagedBean(name = "evaluationController")
@SessionScoped
public class EvaluationController {
	private List<Answer> answers;
	private List<Question> questions;
	private List<Question> answeredquestions;
	private List<Integer> good_answers;
	private List<Integer> correct_answers;
	private List<Integer> wrong_answers;
	private EvaluationDbUtil evaluationDbUtil;
	private AnswerDbUtil answersDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public EvaluationController() throws Exception {
		questions = new ArrayList<>();
		good_answers = new ArrayList<>();
		correct_answers = new ArrayList<>();
		wrong_answers = new ArrayList<>();
		evaluationDbUtil = EvaluationDbUtil.getInstance();
	}

	public List<Question> getQuestions() {
		System.out.println(questions.size());
		return questions;
		
	}

	public List<Question> getAnsweredquestions() {
		return answeredquestions;
	}

	public String loadEvaluation() {

		logger.info("Chargement des questions");

		try {
			if (questions.isEmpty()) {
				FacesContext fc = FacesContext.getCurrentInstance();
				int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));
				String[] sections = fc.getExternalContext().getRequestParameterValuesMap().get("j_idt19:sections");
				// get all students from database

				System.out.println(sections);
				if (sections == null) {
					questions = evaluationDbUtil.getQuestions(fk_matiere_id);
				} else {
					for (String s : sections) {
						int i = Integer.parseInt(s);
						List<Question> inter = evaluationDbUtil.getQuestionsSectionId(i);
						for (Question q : inter) {
							questions.add(q);
						}
					}
				}
				for (Question q : questions) {
					System.out.println(q.getId());
				}
			}
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
		return "/test/auto-evaluation.xhtml?faces-redirect=true";
	}

	public List<Answer> loadAnswer(int questionId) {

		logger.info("Chargement des questions");

		try {

			evaluationDbUtil = EvaluationDbUtil.getInstance();

			// get all students from database
			answers = evaluationDbUtil.getAnswers(questionId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}

		return answers;
	}

	public String correctEvaluation(Evaluation evaluation) {
		try {
			// Create total + the get the nbr of questions answered
			int total = 0;
			int nbr_question_answered = evaluation.getNbr();

			answeredquestions = questions.subList(0, (int) nbr_question_answered);

			// Create instances of the db to access info
			evaluationDbUtil = EvaluationDbUtil.getInstance();
			answersDbUtil = AnswerDbUtil.getInstance();

			// Get the map of the evaluation
			Map<Integer, String[]> eval = evaluation.getEval();

			// For each entry (a question) :
			for (Entry<Integer, String[]> entry : eval.entrySet()) {

				// We get all the answers
				String[] strArray = (String[]) entry.getValue();

				// Transform them in int value
				int[] test = Arrays.asList(strArray).stream().mapToInt(Integer::parseInt).toArray();

				List<Integer> answers = new ArrayList<Integer>();

				// Transform the array of answers in a list of answers
				for (int index = 0; index < test.length; index++) {
					answers.add(test[index]);
				}

				// Get the id of the question (to get the correct answers later)
				int question = entry.getKey();

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
					 * For each good answers we check if every one of them is in
					 * the student's answer list if yes we inc btw of 1 if not
					 * the question is wrong
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
						System.out.println("inc total");

						total++;
					}

				}

			}
			// Make the result in %
			total = (total * 100) / nbr_question_answered;

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			// Put the result in request map
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("total", total);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/test/auto_evaluation_correction.xhtml?faces-redirect=true";
	}
	
	public void resetEvaluation() {

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("evaluationController");

	}

	public boolean goodAnswer(int answerid) {
		if (good_answers.contains(answerid) && !(correct_answers.contains(answerid))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean correctAnswer(int answerid) {
		if (good_answers.contains(answerid) && correct_answers.contains(answerid)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean wrongAnswer(int answerid) {
		if (wrong_answers.contains(answerid) && !(good_answers.contains(answerid))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean notAnswer(int answerid) {
		if (!(wrong_answers.contains(answerid)) && !(good_answers.contains(answerid))) {
			return true;
		} else {
			return false;
		}
	}

	public String getMatiereIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_matiere_id");

	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}