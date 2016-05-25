package answer;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;


@ManagedBean
public class AnswerMap {
	private Map<Integer, String> text;
	private Map<Integer, String> correct;

	public AnswerMap() {
		text = new HashMap<Integer, String>();
		correct = new HashMap<Integer, String>();
	}

	public Map<Integer, String> getText() {
		System.out.println("Inserted Text get : "+text);

		return text;
	}


	public Map<Integer, String> getCorrect() {
		System.out.println("Inserted Correct get : "+correct);

		return correct;
	}


}
