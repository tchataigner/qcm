package section;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;



@ManagedBean
public class Section {

	private int id;
	private String name;
	private int fk_matiere_id;
	private List<Section> section_options;
	private SectionController sectionController;

	
	public Section() {
		/*section_options = new ArrayList<>();
		sectionController.loadSection();
		section_options = sectionController.getSections();*/
	}
	
		
	public Section(int id, String name, int fk_matiere_id) {
		this.id = id;
		this.name = name;
		this.fk_matiere_id = fk_matiere_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFk_matiere_id() {
		return fk_matiere_id;
	}

	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}


	public List<Section> getSection_options() {
		return section_options;
	}
	
	

}
