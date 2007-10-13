/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.lecture.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.el.VariableResolver;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.application.ApplicationService;
import org.esupportail.commons.services.application.ApplicationUtils;
import org.esupportail.commons.services.application.VersionException;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.database.DatabaseUtils;
import org.esupportail.commons.services.exceptionHandling.ExceptionService;
import org.esupportail.commons.services.exceptionHandling.ExceptionUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.services.urlGeneration.AbstractUrlGenerator;
import org.esupportail.commons.services.urlGeneration.ServletUrlGeneratorImpl;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.web.deepLinking.AbstractDeepLinkingRedirector;
import org.esupportail.commons.web.deepLinking.DeepLinkingRedirector;
import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A JSF-based servlet that catches exception and gives them to an exception service.
 * see Javadoc of <a href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/api/index.html">JSF Specification</a>
 */
public class FacesServlet extends HttpServlet {
	
	/**
	 * The id for serialization.
	 */
	private static final long serialVersionUID = 6668301918264753450L;

	/**
	 * The default name of the redirector.
	 */
	private static final String DEFAULT_REDIRECTOR_NAME = "deepLinkingRedirector";

	/**
	 * The name of the servlet parameter that gives the name of the redirector.
	 */
	private static final String REDIRECTOR_NAME_PARAM = "deep-linking-redirector";

	/**
	 * The default default view.
	 */
	private static final String DEFAULT_DEFAULT_VIEW = "/stylesheets/welcome.faces";

	/**
	 * The name of the servlet parameter that gives the name of the redirector.
	 */
	private static final String DEFAULT_VIEW_PARAM = "default-view";

	/**
	 * The servlet info.
	 */
    private static final String SERVLET_INFO = "FacesServlet";
    
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
    /**
     * The context factory.
     */
    private FacesContextFactory facesContextFactory;
    
    /**
     * The lifecycle.
     */
    private Lifecycle lifecycle;

	/**
	 * The name of the redirector.
	 */
	private String redirectorName;
	
	/**
	 * The default view.
	 */
	private String defaultView;

    /**
     * Constructor.
     */
    public FacesServlet() {
        super();
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    @Override
	public void destroy() {
        facesContextFactory = null;
        lifecycle = null;
    }

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
	public String getServletInfo() {
        return SERVLET_INFO;
    }

    private String getLifecycleId() {
        String lifecycleId = getServletConfig().getInitParameter(javax.faces.webapp.FacesServlet.LIFECYCLE_ID_ATTR);
        if (lifecycleId == null) {
        	lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        return lifecycleId;
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
	public void init(final ServletConfig servletConfig) throws ServletException {
    	try {
    		super.init(servletConfig);
    		facesContextFactory = 
    			(FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    		LifecycleFactory lifecycleFactory = 
    			(LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    		lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
    		ApplicationService applicationService = ApplicationUtils.createApplicationService();
    		logger.info("starting " + applicationService.getName() + " v" 
    				+ applicationService.getVersion() + "...");
    		defaultView = servletConfig.getInitParameter(DEFAULT_VIEW_PARAM);
    		if (!StringUtils.hasText(defaultView)) {
    			defaultView = DEFAULT_DEFAULT_VIEW;
    			logger.warn("property " + DEFAULT_VIEW_PARAM
    					+ " is not set, using default value [" + defaultView + "]");
    		}
    		redirectorName = servletConfig.getInitParameter(REDIRECTOR_NAME_PARAM);
    		if (!StringUtils.hasText(redirectorName)) {
    			redirectorName = DEFAULT_REDIRECTOR_NAME;
    			logger.warn("property " + REDIRECTOR_NAME_PARAM 
    					+ " is not set, using default value [" + redirectorName + "]");
    		}
    	} catch (Exception e) {
    		ExceptionUtils.catchException(e);
    		throw new ServletException(e);
    	}
    }

	/**
	 * Check the version of the application and throw a ConfigException
	 * if the servlet can not be executed (for instance if the version
	 * of the application and the one of the database do not match).
	 * @throws ConfigException
	 */
	protected void checkVersion() throws ConfigException {
		// This method can be overriden by particular implementations
		VersionningUtils.checkVersion(true, false);
	}

	/**
	 * Wrap exceptions thrown when already catching an exception.
	 */
	private void handleExceptionHandlingException(
			final Exception e) 
	throws ServletException, IOException {
		logger.error(
				"An exception was thrown while already catching a previous exception:", 
				e); 
        if (e instanceof IOException) {
            throw (IOException) e;
        } else if (e instanceof ServletException) {
            throw (ServletException) e;
        } else if (e.getMessage() != null) {
            throw new ServletException(e.getMessage(), e);
        } else {
            throw new ServletException(e);
        }
	}

	/**
	 * Catch an exception.
	 * @param exception
	 * @param request 
	 * @param response 
	 * @throws ServletException 
	 */
	protected void catchException(
			final Exception exception, 
			final HttpServletRequest request, 
			final HttpServletResponse response) throws ServletException, IOException {
		ExceptionUtils.markExceptionCaught(); 
		ExceptionService exceptionService = null;
		try {
			exceptionService = ExceptionUtils.catchException(exception);
		} catch (Exception e) {
			handleExceptionHandlingException(e);
			// never reached, prevent from warnings
			return;
		}
		ExceptionUtils.markExceptionCaught(exceptionService); 
		try {
			String exceptionUrl = request.getContextPath() 
			+ exceptionService.getExceptionView().replace(".jsp", ".faces");
			response.sendRedirect(exceptionUrl);
			logger.info("redirected the browser to [" + exceptionUrl + "]");
			return;
		} catch (Exception e) {
			handleExceptionHandlingException(e);
		}
	}

	/**
	 * @return the redirector
	 */
	private DeepLinkingRedirector getRedirector(
			final FacesContext facesContext) {
        VariableResolver vr = facesContext.getApplication().getVariableResolver();
        DeepLinkingRedirector deepLinkingRedirector = 
        	(DeepLinkingRedirector) vr.resolveVariable(facesContext, redirectorName);
        if (deepLinkingRedirector == null) {
        	throw new ConfigException("bean [" + redirectorName + "] not found!");
        }
        return deepLinkingRedirector;
	}

	/**
	 * @return the parameters of the current request.
	 */
	private Map<String, String> getParams(
			final HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		Map<String, String>  params = AbstractUrlGenerator.decodeArgToParams(
				request.getParameter(ServletUrlGeneratorImpl.ARGS_PARAM));
		if (request.getParameter("enter") != null) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			params.put(AbstractDeepLinkingRedirector.ENTER_PARAM, "");
		}
		return params;
	}

    /**
     * @see javax.servlet.http.HttpServlet#service(
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
	public void service(final HttpServletRequest request,
                        final HttpServletResponse response) 
    throws IOException, ServletException {

        FacesContext facesContext = null;
    	ServletRequestAttributes previousRequestAttributes = null;
        try {
        	previousRequestAttributes = ContextUtils.bindRequestAndContext(request, getServletContext());
            String pathInfo = request.getPathInfo();

            // if it is a prefix mapping ...
            if (pathInfo != null && (pathInfo.startsWith("/WEB-INF") || pathInfo .startsWith("/META-INF"))) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(" Someone is trying to access a secure resource : " + pathInfo);
                buffer.append("\n remote address is " + request.getRemoteAddr());
                buffer.append("\n remote host is " + request.getRemoteHost());
                buffer.append("\n remote user is " + request.getRemoteUser());
                buffer.append("\n request URI is " + request.getRequestURI());
                logger.warn(buffer.toString());
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            boolean openDatabases = true;
            boolean checkVersion = true;
            boolean exceptionAlreadyCaught = ExceptionUtils.exceptionAlreadyCaught();
            if (exceptionAlreadyCaught) {
            	ExceptionService exceptionService = ExceptionUtils.getMarkedExceptionService();
            	if (exceptionService != null) {
	            	Exception exception = exceptionService.getException();
	            	if (exception != null) {
	            		if (exception instanceof DataAccessException) {
	            			openDatabases = false;
	            		}
	            		if (exception instanceof VersionException) {
	            			checkVersion = false;
	            		}
	            	}
            	}
            }
            if (openDatabases) {
	            DatabaseUtils.open();
	            DatabaseUtils.begin();
	            if (checkVersion) {
	            	checkVersion();
	            }
			}
            facesContext = facesContextFactory.getFacesContext(
            		getServletConfig().getServletContext(), request, response, lifecycle);
    		Map<String, String> params = getParams(request); 
    		if (!exceptionAlreadyCaught && params != null) {
    			String view = getRedirector(facesContext).redirect(params);
    			if (view == null) {
    				view = defaultView;
    			}
    	        Application application = facesContext.getApplication();
    	        ViewHandler viewHandler = application.getViewHandler();
    	        UIViewRoot viewRoot = viewHandler.createView(facesContext, view);
    	        viewRoot.setViewId(view);
    	        facesContext.setViewRoot(viewRoot);
    		} else {
    			lifecycle.execute(facesContext);
    		}
			if (openDatabases) {
//				DatabaseUtils.commit();
			}
			lifecycle.render(facesContext);
            if (openDatabases && ExceptionUtils.exceptionAlreadyCaught()) {
            	ExceptionService exceptionService = ExceptionUtils.getMarkedExceptionService();
            	Exception exception = exceptionService.getException();
            	if (exception != null && exception instanceof DataAccessException) {
            		openDatabases = false;
            	}
            }
			if (openDatabases) {
				DatabaseUtils.commit();//RB ????
				DatabaseUtils.close();
			}
		} catch (Exception e) {
            DatabaseUtils.close();
			catchException(e, request, response);
        } finally {
        	ContextUtils.unbindRequest(previousRequestAttributes);
        	if (facesContext != null) {
        		facesContext.release();
        	}
        }
    }
}
