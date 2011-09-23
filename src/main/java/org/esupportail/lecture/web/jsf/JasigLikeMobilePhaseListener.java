package org.esupportail.lecture.web.jsf;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLManager;

import org.esupportail.commons.context.ApplicationContextHolder;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.context.ApplicationContext;



public class JasigLikeMobilePhaseListener extends MobilePhaseListerner {

	private static final long serialVersionUID = -5393736504160028305L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * @param userAgent
	 * @return true if userAgent in mobileDeviceRegexes
	 */
	private boolean isWirelessDevice(String userAgent) {
		DeviceRegexesHolder deviceRegexesHolder = (DeviceRegexesHolder) ApplicationContextHolder.getContext().getBean("deviceRegexesHolder");
		List<Pattern> mobileDeviceRegexes = deviceRegexesHolder.getMobileDeviceRegexes();
		if (mobileDeviceRegexes != null && userAgent != null) {
			for (Pattern regex : mobileDeviceRegexes) {
				if (regex.matcher(userAgent).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		String userAgent = facesContext.getExternalContext().getRequestHeaderMap().get("User-Agent");
		if (isWirelessDevice(userAgent)) {
			String view = facesContext.getViewRoot().getViewId();
			if (!view.startsWith("/stylesheets/mobile")) {
				view = "/stylesheets/mobile/" + view.substring(13);
				String viewURL = facesContext.getApplication().getViewHandler().getActionURL(facesContext, view);
				try {
					event.getFacesContext().getExternalContext().redirect(viewURL);
				} catch (IOException e) {
					logger.error("EXCEPTION: " + e.getMessage());				}
			}
		}
	}

}
