package matiere;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import param_matiere.ParamDbUtil;


@ManagedBean
@SessionScoped
public class MatiereController {

	private List<Matiere> matieres;
	private MatiereDbUtil matiereDbUtil;
	private ParamDbUtil paramDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public MatiereController() throws Exception {
		matieres = new ArrayList<>();
		paramDbUtil = ParamDbUtil.getInstance();
		matiereDbUtil = MatiereDbUtil.getInstance();
	}

	public List<Matiere> getMatieres() {
		return matieres;
	}

	public void loadMatieres() {

		logger.info("Chargement des matieres");

		matieres.clear();

		try {

			// get all students from database
			matieres = matiereDbUtil.getMatieres();

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des matieres", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	public String addMatiere(Matiere theMatiere, int fk_user_id) {
		logger.info("Adding matiere: " + theMatiere);
		try {
			matiereDbUtil.addMatiere(theMatiere, fk_user_id);
			paramDbUtil.initNotation(theMatiere.getId());
			paramDbUtil.initTime(theMatiere.getId());
			paramDbUtil.initNbr(theMatiere.getId());
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/index.xhtml";
	}

	public String deleteMatiere() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		int matiereId = Integer.parseInt(params.get("id"));
		try {
			
			matiereDbUtil.deleteMatiere(matiereId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + matiereId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "/test/list-matiere.xhtml";
	}
	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}

