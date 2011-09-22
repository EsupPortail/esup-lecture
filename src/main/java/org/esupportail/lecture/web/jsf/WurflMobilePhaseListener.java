package org.esupportail.lecture.web.jsf;


import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLManager;

import org.esupportail.commons.context.ApplicationContextHolder;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.springframework.context.ApplicationContext;



public class WurflMobilePhaseListener extends MobilePhaseListerner {

	private static final long serialVersionUID = -5393736504160028305L;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ApplicationContext springContext = ApplicationContextHolder.getContext();
		WURFLManager wurflManager = (WURFLManager) springContext.getBean("wurflManager");
		String agent = facesContext.getExternalContext().getRequestHeaderMap().get("User-Agent");
		Device device = wurflManager.getDeviceForRequest(agent);
		boolean isWirelessDevice;
		if (device.getCapability("is_wireless_device").equals("true")) {
			isWirelessDevice = true;
		}
		else {
			isWirelessDevice = false;
		}
		if (isWirelessDevice) {
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
