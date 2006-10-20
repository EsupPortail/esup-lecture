/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.controllers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.esupportail.lecture.web.beans.PrintableFile;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.commons.utils.ExternalContextUtils;

/**
 * A bean to manage files.
 */
public class FilesController extends AbstractContextAwareController {

	/**
	 * The filename to download.
	 */
	private String filenameToDownload;

	/**
	 * Constructor.
	 */
	public FilesController() {
		super();
	}

	/**
	 * @see org.esupportail.commons.web.controllers.Resettable#reset()
	 */
	public void reset() {
		filenameToDownload = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return true;
//		User currentUser = getCurrentUser();
//		return currentUser != null;
	}
	
	/**
	 * @return the name of the directory.
	 */
	public String getDirectory() {
		return "c:/php";
	}

	/**
	 * @return the files cached.
	 */
	@SuppressWarnings("unchecked")
	private List<PrintableFile> getCachedPrintableFiles() {
		return (List<PrintableFile>) ExternalContextUtils.getRequestVar("files");
	}

	/**
	 * Cache printable files.
	 * @param printableFiles 
	 */
	private void cachePrintableFiles(List<PrintableFile> printableFiles) {
		ExternalContextUtils.setRequestVar("files", printableFiles);
	}

	/**
	 * Uncache printable files.
	 */
	private void uncachePrintableFiles() {
		cachePrintableFiles(null);
	}

	/**
	 * @return the printable files.
	 */
	public List<PrintableFile> getFiles() {
		if (getCachedPrintableFiles() == null) {
			File dir = new File(getDirectory());
		    FileFilter fileFilter = new FileFilter() {
		        public boolean accept(File file) {
		            return !file.isDirectory();
		        }
		    };
		    List<PrintableFile> result = new ArrayList<PrintableFile>();
		    for (File file : dir.listFiles(fileFilter)) {
				result.add(new PrintableFile(file));
			}
			cachePrintableFiles(result);
		}
		return getCachedPrintableFiles();
	}

	/**
	 * @return the number of things of the current department.
	 */
	public int getFilesNumber() {
		return getFiles().size();
	}

	/**
	 * @param filenameToDownload the filenameToDownload to set
	 */
	public void setFilenameToDownload(final String filenameToDownload) {
		this.filenameToDownload = filenameToDownload;
	}
	
//	/**
//	 * JSF callback.
//	 * @param event
//	 * @throws IOException 
//	 */
//	public String downloadFile(
//			@SuppressWarnings("unused") ActionEvent event) throws IOException {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext externalContext = facesContext.getExternalContext();
//		externalContext.redirect(getDownloadUrl() + "?file=auth.php");
//		facesContext.responseComplete();
//		return;
//	}

	/**
	 * JSF callback.
	 * @return a String.
	 */
	public String downloadFile() {
		DownloadUtils.setDownload(getDirectory() + "/" + filenameToDownload);
		return null;
	}

}
