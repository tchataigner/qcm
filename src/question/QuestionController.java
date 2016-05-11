package question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class QuestionController {

	private List<Question> questions;
	private QuestionDbUtil questionDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public QuestionController() throws Exception {
		questions = new ArrayList<>();

		questionDbUtil = QuestionDbUtil.getInstance();
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void loadQuestions(int matiereid) {

		logger.info("Chargement des questions");

		questions.clear();

		try {

			// get all students from database
			questions = questionDbUtil.getQuestions(matiereid);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	public String addQuestion(Question theQuestion) {
		logger.info("Adding Question: " + theQuestion);

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));		
		

		
		theQuestion.setFk_matiere_id(fk_matiere_id);
		
		try {
			questionDbUtil.addQuestion(theQuestion);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/reponses/add-reponse-form";
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public String getMatiereIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_matiere_id");

	}

}