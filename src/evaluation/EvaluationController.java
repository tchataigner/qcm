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
	private EvaluationDbUtil evaluationDbUtil;
	private AnswerDbUtil answersDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public EvaluationController() throws Exception {
		questions = new ArrayList<>();

		evaluationDbUtil = EvaluationDbUtil.getInstance();
	}

	public List<Question> getQuestions() {
		return questions;
	}

	@PostConstruct
	public void loadEvaluation() {

		logger.info("Chargement des questions");

		try {

			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			int matiereId = Integer.parseInt(params.get("id"));
			// get all students from database
			questions = evaluationDbUtil.getQuestions(matiereId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
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
			float total = 0;
			float nbr_question_answered = evaluation.getNbr();
			evaluationDbUtil = EvaluationDbUtil.getInstance();
			answersDbUtil = AnswerDbUtil.getInstance();

			Map<Integer, String[]> eval = evaluation.getEval();
			// System.out.println("Int : "+Arrays.toString(eval.get(80)));
			for (Entry<Integer, String[]> entry : eval.entrySet()) {
				String[] strArray = (String[]) entry.getValue();
				int[] test = Arrays.asList(strArray).stream().mapToInt(Integer::parseInt).toArray();
				List<Integer> answers = new ArrayList<Integer>();
				for (int index = 0; index < test.length; index++) {
					answers.add(test[index]);
				}
				int question = entry.getKey();

				if (!(answers.size() == 0)) {

					List<Answer> correct_answers_list = answersDbUtil.getAnswers(question);
					List<Integer> correct_answer = new ArrayList<Integer>();
					for (Answer a : correct_answers_list) {
						if (a.getCorrect() == 1) {
							correct_answer.add(a.getId());
						}
					}
					System.out.println(question + " : " + answers.equals(correct_answer));

					if (answers.size() == correct_answer.size()) {
						int btw = 0;
						for (int a : correct_answer) {
							if (answers.contains(a)) {
								btw++;
							}
						}
						if (btw == correct_answer.size()) {
							total++;
						}
					}

				} else {
					System.out.println("null");
				}

			}
			total = (total * 100) / nbr_question_answered;
			System.out.println(total);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("total", total);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/test/auto_evaluation_correction.xhtml";
	}

	public void resetEvaluation() {

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("evaluationController");

	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}