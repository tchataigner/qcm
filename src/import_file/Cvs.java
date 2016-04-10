package import_file;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;

@ManagedBean(name="cvs")
public class Cvs {
	
	  private Part file;
	  
	  public Cvs() {
		  
	  }

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
	  
	  
}

