package matiere;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import login.SessionBean;

@ManagedBean
public class Matiere {

	private int id;
	private String name;

	
	public Matiere() {
	}
	
	public String getIdParam(FacesContext fc){
		
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("id");
		
	}
	
	public String outcome(String destination){
		HttpSession session = SessionBean.getSession();
		FacesContext fc = FacesContext.getCurrentInstance();
		session.setAttribute("matiere_id", Integer.parseInt(getIdParam(fc)));
		return destination;
	}

	
	public Matiere(int id, String name) {
		this.id = id;
		this.name = name;
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

}
