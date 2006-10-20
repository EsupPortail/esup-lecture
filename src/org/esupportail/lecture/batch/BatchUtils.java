/**
 * ESUP-Portail Lecture - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-lecture
 */
package org.esupportail.lecture.batch; 

import org.esupportail.commons.batch.AbstractBatchUtils;
import org.esupportail.commons.utils.BeanUtils;

/**
 * A class that provides utilities for batch commands.
 */
public class BatchUtils extends AbstractBatchUtils {

	/**
	 * Private constructor.
	 */
	private BatchUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return an application service.
	 */
	public static VersionningService createVersionningService() {
		return (VersionningService) BeanUtils.getBean(AbstractBatchUtils.VERSIONNING_SERVICE_BEAN);
	}
	
}

