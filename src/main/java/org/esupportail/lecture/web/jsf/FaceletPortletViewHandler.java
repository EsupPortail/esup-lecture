package org.esupportail.lecture.web.jsf;

import java.io.IOException;
import java.io.Writer;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.facelets.FaceletViewHandler;

/**
 * ViewHandler implementation for Facelets used in Portlets
 * 
 */
public class FaceletPortletViewHandler extends FaceletViewHandler {

	public FaceletPortletViewHandler(ViewHandler parent) {
		super(parent);
	}

	protected ResponseWriter createResponseWriter(FacesContext context)
			throws IOException, FacesException {
		ExternalContext extContext = context.getExternalContext();
		RenderKit renderKit = context.getRenderKit();

		RenderRequest request = (RenderRequest) extContext.getRequest();
		RenderResponse response = (RenderResponse) extContext.getResponse();

		String contenttype = request.getResponseContentType();
		if (contenttype == null) {
			contenttype = "text/html";
		}

		String encoding = response.getCharacterEncoding();
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}

		ResponseWriter writer = renderKit.createResponseWriter(NullWriter.Instance, contenttype, encoding);

		contenttype = writer.getContentType();

		// apply them to the response
		response.setContentType(contenttype);

		// Now, clone with the real writer
		writer = writer.cloneWithWriter(response.getWriter());

		return writer;
	}

	protected static class NullWriter extends Writer {
        static final NullWriter Instance = new NullWriter();
        public void write(char[] buffer) {
        }
        public void write(char[] buffer, int off, int len) {
        }
        public void write(String str) {
        }
        public void write(int c) {
        }
        public void write(String str, int off, int len) {
        }
        public void close() {
        }
        public void flush() {
        }
    }
}
