package login;

import java.io.Serializable;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
 
import login.LoginDAO;
 
@ManagedBean
@SessionScoped
public class Login implements Serializable {
 
    private static final long serialVersionUID = 1094801825228386363L;
     
    private int id;
    private String pwd;
    private String msg;
    private String user;
    private int power;
    
    
 
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPwd() {
        return pwd;
    }
 
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public void setMsg(String msg) {
        this.msg = msg;
    }
 
    public String getUser() {
        return user;
    }
 
    public void setUser(String user) {
        this.user = user;
    }
    
    public void setPower(int power) {
		this.power = power;
	}

	public int getPower() {
		return power;
	}

	//validate login
    public String validateUsernamePassword() {
        boolean valid = LoginDAO.validate(user, pwd);
        
        if (valid) {
        	int pow = LoginDAO.getDataPower(user, pwd);
        	int id = LoginDAO.getDataId(user, pwd);
            HttpSession session = SessionBean.getSession();
            session.setAttribute("username", user);
            session.setAttribute("power", pow);
            session.setAttribute("userid", id);
            return "ok";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Passowrd",
                            "Please enter correct username and Password"));
            return "login";
        }
    }
 
    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionBean.getSession();
        session.invalidate();
        return "/login.xhtml?faces-redirect=true";
    }
}