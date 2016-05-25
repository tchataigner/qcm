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

	public void loadSection() {

		logger.info("Chargement des Sections");

		sections.clear();

		try {

			// get all section from database
			sections = sectionDbUtil.getSections();

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erreur lors du chargement des Section", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	public String addSection(Section theSection, int fk_matiere_id) {
		logger.info("Adding Section: " + theSection);
		try {
			sectionDbUtil.addSection(theSection, fk_matiere_id);
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

	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

}
