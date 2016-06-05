package section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@ManagedBean
@SessionScoped
public class SectionController {


	private List<Section> sections;
	private SectionDbUtil sectionDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public SectionController() throws Exception {
		sections = new ArrayList<>();

		sectionDbUtil = SectionDbUtil.getInstance();
	}

	public List<Section> getSections() {
		return sections;
	}

	public List<Section> loadSection(int fk_matiere_id) {

		logger.info("Chargement des Sections");

		sections.clear();
		

		try {

			// get all section from database
			sections = sectionDbUtil.getSections(fk_matiere_id);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des Section", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
		return sections;
	}

	public String addSection(Section theSection) {
		logger.info("Adding Section: " + theSection);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		int fk_matiere_id = Integer.parseInt(getMatiereIdParam(fc));
		
		theSection.setFk_matiere_id(fk_matiere_id);
		try {
			sectionDbUtil.addSection(theSection);
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding Section", exc);
			addErrorMessage(exc);
			return null;
		}
		return "/index.xhtml";
	}

	public String deleteSection() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext()
				.getRequestParameterMap();
		int SectionId = Integer.parseInt(params.get("id"));
		try {

			sectionDbUtil.deleteSection(SectionId);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading section id:" + SectionId, exc);

			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}

		return "/test/accueil_list_section.xhtml";
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
