package evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;

import answer.Answer;
import question.Question;

@ManagedBean(name="evaluationController")
@SessionScoped
public class EvaluationController {
	private List<Answer> answers;
	private List<Question> questions;
	private EvaluationDbUtil evaluationDbUtil;
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
			Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
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
	
	public void correctEvaluation() {
		
		
		
	}
	
	public void resetEvaluation() {

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("evaluationController");
			
			
	}

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}