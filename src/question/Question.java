package question;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Question {

	private int id;
	private String text;
	private String media;
	private int difficulty;

	
	public Question() {
	}
	
	public Question(int id, String text, String media, int difficulty) {
		this.id = id;
		this.text = text;
		this.media = media;
		this.difficulty = difficulty;
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



}
