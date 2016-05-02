package question;


import javax.faces.bean.ManagedBean;

@ManagedBean
public class Question {

	private int id;
	private String text;
	private String media;
	private int difficulty;
	private int fk_matiere_id;
	private int fk_type_id;
	
	
	public Question() {

	}
	
	public Question(int id, String text, String media, int difficulty, int fk_matiere_id) {
		this.id = id;
		this.text = text;
		this.media = media;
		this.difficulty = difficulty;
		this.fk_matiere_id = fk_matiere_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getFk_matiere_id() {
		return fk_matiere_id;
	}

	public void setFk_matiere_id(int fk_matiere_id) {
		this.fk_matiere_id = fk_matiere_id;
	}

	public int getFk_type_id() {
		return fk_type_id;
	}

	public void setFk_type_id(int fk_type_id) {
		this.fk_type_id = fk_type_id;
	}


}
