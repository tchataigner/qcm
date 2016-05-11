package param_matiere;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import param_matiere.ParamController;

@ManagedBean
public class Duration {

	private int id;
	private int hour;
	private int min;
	private int fk_matiere_id;
	private ParamController paramController;

	@PostConstruct
	public void init() {
		
		int matiereId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matiere_id");
		int[] durations = paramController.loadDuration(matiereId);	
		System.out.println(durations[0]);
		setHour(durations[0]);
		setMin(durations[1]);
	}
	
	
	public Duration() {
		
	}

	public Duration(int id, int hour, int min, int fk_matiere_id) {
		this.id = id;
		this.hour = hour;
		this.min = min;
		this.fk_matiere_id = fk_matiere_id;
	}
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getHour() {
		return hour;
	}


	public void setHour(int hour) {
		this.hour = hour;
	}


	public int getMin() {
		return min;
	}


	public void setMin(int min) {
		this.min = min;
	}


	public int getFk_matiere_id() {
		return fk_matiere_id;
	}


	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}

	
	
	
}
