package matiere;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MatiereController {

	private List<Matiere> matieres;
	private MatiereDbUtil matiereDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public MatiereController() throws Exception {
		matieres = new ArrayList<>();
		
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
				
	public String addMatiere(Matiere theMatiere) {
		logger.info("Adding matiere: " + theMatiere);
		try{
			matiereDbUtil.addMatiere(theMatiere);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding matiere", exc);
			addErrorMessage(exc);
			return null;
		}
		return "list-matiere?faces-redirect=true";
	}
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}
	
}
