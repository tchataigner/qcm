package param_matiere;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Notation {

	private int id;
	private int correct_value;
	private int incorrect_value;
	private int noanswer_value;
	private int fk_matiere_id;

	
	public Notation() {
		
	}

	public Notation(int id, int correct_value, int incorrect_value, int noanswer_value, int fk_matiere_id) {
		this.id = id;
		this.correct_value = correct_value;
		this.incorrect_value = incorrect_value;
		this.noanswer_value = noanswer_value;
		this.fk_matiere_id = fk_matiere_id;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCorrect_value() {
		return correct_value;
	}


	public void setCorrect_value(int correct_value) {
		this.correct_value = correct_value;
	}


	public int getIncorrect_value() {
		return incorrect_value;
	}


	public void setIncorrect_value(int incorrect_value) {
		this.incorrect_value = incorrect_value;
	}


	public int getNoanswer_value() {
		return noanswer_value;
	}


	public void setNoanswer_value(int noanswer_value) {
		this.noanswer_value = noanswer_value;
	}


	public int getFk_matiere_id() {
		return fk_matiere_id;
	}


	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}

	
	
	
}

