/*package answer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;


@ManagedBean
public class AnswerMap {

	private Answer[] map;

	public AnswerMap() {
		
	}
	
	@PostConstruct
	public void init(){
		map = new Answer[4];
		System.out.println(map[1]);
		}

	public Answer[] getMap() {
		return map;
	}

	public void setMap(Answer[] map) {
		this.map = map;
	}
	
	public void setCorrectIndex(int index) {
		map[index].setCorrect();
	}
}*/