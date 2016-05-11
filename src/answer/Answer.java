package answer;


import javax.faces.bean.ManagedBean;

@ManagedBean
public class Answer {

	private int id;
	private String text;
	private int correct;
	private int fk_question_id;
	
	public Answer() {

	}
	
	public Answer(int id, String text, int correct, int fk_question_id) {
		this.id = id;
		this.text = text;
		this.correct = correct;
		this.fk_question_id = fk_question_id;
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

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getFk_question_id() {
		return fk_question_id;
	}

	public void setFk_question_id(int fk_question_id) {
		this.fk_question_id = fk_question_id;
	}

}
