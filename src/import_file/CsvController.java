package import_file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;


import import_file.CSVDbUtil;

@ManagedBean(name="cvsController")
public class CsvController {
	private String fileContent;
	private CSVDbUtil cvsDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	
	public void uploadCVS(Csv theFile) throws Exception {
		 Part file = theFile.getFile();


			try {
				cvsDbUtil = CSVDbUtil.getInstance();
				Map<Integer, String> maps = new HashMap<Integer, String>();
				Scanner scanner = new Scanner(file.getInputStream());
				fileContent = scanner.useDelimiter(";").next();
				
				int i = 0;
				while (scanner.hasNextLine()) {
					if (i == 0){
						scanner.nextLine();
						
					} else {
						maps.put(i, scanner.nextLine());
					}
					i++;
				}
				 scanner.close();

				 //
				//loop map

				for (Map.Entry<Integer, String> entry : maps.entrySet()) {
					String[] values = entry.getValue().split(";");
					
					
					
					cvsDbUtil.uploadCVS(values);


				}
				

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// send this to server logs
				logger.log(Level.SEVERE, "Error:", e);

				// add error message for JSF page
				addErrorMessage(e);
			}
	  }
	  
		private void addErrorMessage(Exception exc) {
			FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);

		}

}
