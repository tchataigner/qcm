package answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	public String addAnswer() {
		

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_question_id = Integer.parseInt(getQuestionIdParam(fc));

		
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();		

		try {
			map = new Answer[4];

			
			for(int i = 0;i<=1;i++) {

				
				String text = request.getParameter("form_answer:text"+i);
				
				String correct = request.getParameter("form_answer:correct"+i);

				int correct_int = Integer.parseInt(correct);
				
				map[i] = new Answer();
				
				
				map[i].setText(text);
				
				map[i].setCorrect(correct_int);
				
				map[i].setFk_question_id(fk_question_id);

				answerDbUtil.addAnswer(map[i]);
			}
			//answerDbUtil.addAnswer(theAnswer);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/reponses/add-reponse-form?faces-redirect=true";
	}

	public String getQuestionIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_question_id");

	}
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
