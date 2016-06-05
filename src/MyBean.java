import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean
public class MyBean {

	public UploadedFile uploadedFile;
	public String fileName;

	public void submit() {

		// infos récupérées  
		System.out.println("File type: " + uploadedFile.getContentType());
		System.out.println("File name: " + uploadedFile.getName());
		System.out.println("File size: " + uploadedFile.getSize() + " bytes");

		String prefix = FilenameUtils.getBaseName(uploadedFile.getName());
		String suffix = FilenameUtils.getExtension(uploadedFile.getName());

		// Prepare file and outputstream.
		File file = null;
		OutputStream output = null;

		try {
			System.out.println("test");
			// Creation du fichier unique.
			file = File.createTempFile(prefix + "_", "." + suffix, new File(
					"C:/Users/Thomas/Music"));
			output = new FileOutputStream(file);
			IOUtils.copy(uploadedFile.getInputStream(), output);
			fileName = file.getName();

			// message succes.
			FacesContext.getCurrentInstance().addMessage(
					"uploadForm",
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"File upload succeed!", null));
		} catch (IOException e) {
			// Cleanup.
			if (file != null)
				file.delete();

			// error message.
			FacesContext.getCurrentInstance().addMessage(
					"uploadForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"File upload failed with I/O error.", null));

			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);


		}
	}

	// Getters

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	// Setters

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
		// }
		// Path folder = Paths.get("/path/to/uploads");
		// String filename = FilenameUtils.getBaseName(uploadedFile.getName());
		// String extension =
		// FilenameUtils.getExtension(uploadedFile.getName());
		// Path file = Files.createTempFile(folder, filename + "-", "." +
		// extension);
		//
		// try (InputStream input = uploadedFile.getInputStream()) {
		// Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
		// }
		//
		// System.out.println("Uploaded file successfully saved in " + file);
		//
	}
}