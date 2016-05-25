package evaluation;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;


@ManagedBean
public class Evaluation {
	private Map<Integer, String[]> eval;
	private Integer[] answers;
	private Integer question;
	private int nbr;

	public Evaluation() {
		eval = new HashMap<Integer, String[]>();
	}
	
	public Integer[] getAnswers() {
		return answers;
	}

	public void setAnswers(Integer[] answers) {
			this.answers = answers;
	}

	public Integer getQuestion() {
		return question;
	}

	public void setQuestion(Integer question) {
		this.question = question;
	}

	public Map<Integer, String[]> getEval() {
		return eval;
	}

	public void setEval(Map<Integer, String[]> eval) {
		this.eval = eval;
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(int nbr) {
		this.nbr = nbr;
	}
	
	
}
