package param_matiere;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class NbrQuestion {

	private int id;
	private int nbr;
	private int fk_matiere_id;

	
	public NbrQuestion() {
		
	}

	public NbrQuestion(int id, int nbr, int fk_matiere_id) {
		this.id = id;
		this.nbr = nbr;
		this.fk_matiere_id = fk_matiere_id;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getNbr() {
		return nbr;
	}


	public void setNbr(int nbr) {
		this.nbr = nbr;
	}


	public int getFk_matiere_id() {
		return fk_matiere_id;
	}


	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}
	
	
	
	
}
