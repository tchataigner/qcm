package param_matiere;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ParamController {


	private ParamDbUtil paramDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	/* add duration */
	public String addDuration(Duration theDuration) {
		logger.info("Adding Question: " + theDuration);

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));

		theDuration.setFk_matiere_id(fk_matiere_id);
		System.out.println(theDuration.getHour());
		System.out.println(theDuration.getMin());
		System.out.println(theDuration.getFk_matiere_id());
		try {
			paramDbUtil = ParamDbUtil.getInstance();
			paramDbUtil.addParamDuration(theDuration);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding question", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/gestion/param_matiere.xhtml?faces-redirect=true";
	}
	/* add number of question*/
	public String addNbrQuestion(NbrQuestion theNbrQuestion) {
		logger.info("Adding Question: " + theNbrQuestion);

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));

		theNbrQuestion.setFk_matiere_id(fk_matiere_id);
		try {
			paramDbUtil = ParamDbUtil.getInstance();
			paramDbUtil.addParamNbrQuestion(theNbrQuestion);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding question", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/gestion/param_matiere.xhtml?faces-redirect=true";
	}
	
	/* add notation */
	public String addNotation(Notation theNotation) {
		logger.info("Adding Question: " + theNotation);

		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));

		theNotation.setFk_matiere_id(fk_matiere_id);
		/*System.out.println(theDuration.getHour());
		System.out.println(theDuration.getMin());
		System.out.println(theDuration.getFk_matiere_id());*/
		try {
			paramDbUtil = ParamDbUtil.getInstance();
			paramDbUtil.addParamNotation(theNotation);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding question", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/gestion/param_matiere.xhtml?faces-redirect=true";
	}
	
	/* get param duration value */
	public void loadDuration(int matiereid) {
		logger.info("Chargement des questions");

		try {
			paramDbUtil = ParamDbUtil.getInstance();

			
			Duration theDuration = paramDbUtil.getDuration(matiereid);
			// put in the request attribute ... so we can use it on the form
			// page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("duration", theDuration);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	/* get param number of question value */
	public void loadNbrQuestion(int matiereid) {
		logger.info("Chargement des questions");

		try {
			paramDbUtil = ParamDbUtil.getInstance();

			
			NbrQuestion theNbrQuestion = paramDbUtil.getNbrQuestion(matiereid);
			// put in the request attribute ... so we can use it on the form
			// page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("nbrQuestion", theNbrQuestion);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des questions", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	/* get param notation value */
	public void loadNotation(int matiereid) {
		logger.info("Chargement des questions");

		try {
			paramDbUtil = ParamDbUtil.getInstance();

			
			Notation theNotation = paramDbUtil.getNotation(matiereid);
			// put in the request attribute ... so we can use it on the form
			// page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("notation", theNotation);
			
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

	public String getMatiereIdParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("fk_matiere_id");

	}
}
