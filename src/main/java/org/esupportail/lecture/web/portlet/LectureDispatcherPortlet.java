package org.esupportail.lecture.web.portlet;

import java.util.List;

import javax.portlet.MimeResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.esupportail.commons.context.ApplicationContextHolder;
import org.springframework.web.portlet.DispatcherPortlet;
import org.w3c.dom.Element;

public class LectureDispatcherPortlet extends DispatcherPortlet {

	@Override
	protected void doHeaders(RenderRequest request, RenderResponse response) {
		super.doHeaders(request, response);
		@SuppressWarnings("unchecked")
		//add css en js to portal Header
		List<HeadElement> headElements = (List<HeadElement>) ApplicationContextHolder.getContext().getBean("headElements");
		for (HeadElement headElement : headElements) {
			Element element = response.createElement(headElement.getName());
			element.setAttribute("href",
					response.encodeURL(request.getContextPath() + headElement.getHref()));
			element.setAttribute("rel", headElement.getRel());
			element.setAttribute("type", headElement.getType());
			response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
		}
	}

}
