package org.esupportail.lecture.web.jsf;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

abstract public class MobilePhaseListerner implements PhaseListener {

	private static final long serialVersionUID = 5156934660063471596L;


	public MobilePhaseListerner() {
		super();
	}

	public void afterPhase(PhaseEvent event) {
		// nothing
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
	
	abstract public void beforePhase(PhaseEvent event);
}