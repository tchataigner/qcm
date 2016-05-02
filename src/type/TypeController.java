package type;

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
public class TypeController {

	private List<Type> types;
	private TypeDbUtil typeDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public TypeController() throws Exception {
		types = new ArrayList<>();

		typeDbUtil = TypeDbUtil.getInstance();
	}

	public List<Type> getTypes() {
		return types;
	}

	public void loadTypes() {

		logger.info("Chargement des matieres");

		types.clear();

		try {

			// get all students from database
			types = typeDbUtil.getTypes();

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des matieres", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	

	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}

