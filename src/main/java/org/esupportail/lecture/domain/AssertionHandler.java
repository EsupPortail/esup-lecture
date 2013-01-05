/**
 *
 */
package org.esupportail.lecture.domain;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletRequestAttributes;

/**
 * @author GIP RECIA - jgribonvald
 * 21 juil. 2011
 */
public class AssertionHandler implements AssertionAccessor, InitializingBean {

	/**	 */
	protected static Log log = LogFactory.getLog(AssertionHandler.class);

	/**  */
	private TicketValidator ticketValidator;

	/**  */
	private String service;

	/**  */
	private Assertion assertion;

	/**
	 * @param ticketValidator the ticketValidator to set
	 */
	public void setTicketValidator(TicketValidator ticketValidator) {
		this.ticketValidator = ticketValidator;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @param assertion
	 */
	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
		log.debug("AssertionHandler::setAssertion() : assertion [" + assertion + "] set");
	}

	public Assertion getAssertion() {
		//get ProxyTicket for current portlet from uPortal
		if (assertion == null) {
			final PortletRequest request = ((PortletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
			@SuppressWarnings("unchecked")
			Map<String,String> userinfo = (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
			String ticket = userinfo.get("casProxyTicket");
			if (log.isDebugEnabled()) {
				log.debug("casProxyTicket obtained :" + ticket );
			}
			if(ticket == null) {
				log.error("AssertionHandler::getAssertion() : no portal PT available for user [" + request.getRemoteUser() + "]");
				throw new RuntimeException("no portal PT available for user [" + request.getRemoteUser() + "]") ;
			}
			//get ProxyTicket for casTargetService
			try {
				assertion = ticketValidator.validate(ticket, service);
			} catch (TicketValidationException e) {
				log.error("fail to validate a ticket", e);
				throw new RuntimeException(e);
			}
		}
		return assertion;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ticketValidator, "property ticketValidator of class "
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(service, "property service of class "
				+ this.getClass().getName() + " can not be null");
	}
}
