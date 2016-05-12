package param_matiere;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Duration {

	private int id;
	private int hour;
	private int min;
	private int fk_matiere_id;

	
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
