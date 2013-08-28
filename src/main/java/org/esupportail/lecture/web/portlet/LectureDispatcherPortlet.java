package org.esupportail.lecture.web.portlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			for (HeadElement headElement : headElements) {
				Element element = response.createElement(headElement.getName());
				String hrefKey = "href";
				if (headElement.getName().equals("script")) {
					hrefKey = "src";
					element.setTextContent(" ");
				}
				String href = headElement.getHref();
				if (!href.startsWith("http")) {
					href = request.getContextPath() + href;
				}
				element.setAttribute(hrefKey, response.encodeURL(href));
				if  (headElement.getName().equals("link")) {
					element.setAttribute("rel", headElement.getRel());
				}
				element.setAttribute("type", headElement.getType());
				//uPortal workaround
				StringWriter buffer = new StringWriter();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.transform(new DOMSource(element), new StreamResult(buffer));
				response.getWriter().write(buffer.toString());
				//standard way
				//			response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
			}		
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
