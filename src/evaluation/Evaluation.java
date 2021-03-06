package evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import question.Question;


@ManagedBean
public class Evaluation {
	private Map<Integer, String[]> eval;
	private Integer[] answers;
	private int[] section;
	private Integer question;
	private int nbr;
	private List<Question> list_questions;

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

	public int[] getSection() {
		return section;
	}

	public void setSection(int[] section) {
		this.section = section;
	}

	public List<Question> getList_questions() {
		return list_questions;
	}

	public void setList_questions(List<Question> list_questions) {
		this.list_questions = list_questions;
	}
	
	
	
	
}
