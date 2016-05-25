package answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class AnswerController {

	private Answer[] map;
	private List<Answer> answers;
	private AnswerDbUtil answerDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public AnswerController() throws Exception {
		answers = new ArrayList<>();

		answerDbUtil = AnswerDbUtil.getInstance();
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void loadAnswers(int questionid) {

		logger.info("Chargement des questions");

		answers.clear();

		try {

			// get all students from database
			answers = answerDbUtil.getAnswers(questionid);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	public String addAnswer(AnswerMap answermap) {
		
		Map<Integer, String> text_answers = answermap.getText();
		Map<Integer, String> correct_answers = answermap.getCorrect();

		Map<String, Integer> answers = new HashMap<String, Integer>();
		for (int i = 1; i <= text_answers.size(); i++) {
			String text = text_answers.get(Integer.toString(i));
			String correct = correct_answers.get(Integer.toString(i));
			
			answers.put(text, Integer.parseInt(correct));
		}
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		int fk_question_id = getQuestionIdParam(fc);
		
		try {
		answerDbUtil = AnswerDbUtil.getInstance();
			for (Entry<String, Integer> a : answers.entrySet()) {
				answerDbUtil.addAnswer(a.getKey(), a.getValue(), fk_question_id);
			}
			//answerDbUtil.addAnswer(theAnswer);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/index.xhtml";
	}

	public int getQuestionIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		int fk_question_id = Integer.parseInt(params.get("fk_question_id"));
		return fk_question_id;

	}
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
