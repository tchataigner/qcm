package knowledge;


import javax.faces.bean.ManagedBean;

@ManagedBean
public class Knowledge {

	private int id;
	private String name;
	private int open;
	private int nbr_student;
	private String created_at;
	private int fk_matiere_id;
	private int[] questions;


	
	public Knowledge() {

	}
	
	public Knowledge(int id, String name, int open, int nbr_student, String created_at, int fk_matiere_id) {
		this.id = id;
		this.name = name;
		this.open = open;
		this.nbr_student = nbr_student;
		this.created_at = created_at;
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

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public int getNbr_student() {
		return nbr_student;
	}

	public void setNbr_student(int nbr_student) {
		this.nbr_student = nbr_student;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getFk_matiere_id() {
		return fk_matiere_id;
	}

	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}

	public int[] getQuestions() {
		return questions;
	}

	public void setQuestions(int[] questions) {
		this.questions = questions;
	}

	
	


}
