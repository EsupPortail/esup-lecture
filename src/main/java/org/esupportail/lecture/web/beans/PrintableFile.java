/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.web.beans;

import java.io.File;

/**
 * A class to print files.
 */
public class PrintableFile {

	/**
	 * The file.
	 */
	private File file;
	
	/**
	 * Constructor.
	 * @param file 
	 */
	public PrintableFile(
			final File file) {
		super();
		this.file = file;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return file.length();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return null;
	}

}
