package org.esupportail.lecture.web.controllers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.batch.WebApplicationFilter;
import org.esupportail.commons.test.AbstractTest;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class TestHomeController extends AbstractTest {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(TestHomeController.class);

	public void testHome() throws Exception {
		LOG.info("=========================== TestHomeController =================================");

		new WebApplicationFilter(webApplicationUtils, new FilterChain() {

			public void doFilter(
					final ServletRequest arg0, 
					final ServletResponse arg1) 
			throws IOException, ServletException {
				HomeController homeController = 
					(HomeController) applicationContext.getBean("homeController");
				//left tree
				homeController.getContext().getCategories();
				//right tree
				//homeController.selectedCategory.selectedOrAllSources
				LOG.info("Cat. name = " + homeController.getSelectedCategory().getName());
				for (SourceWebBean src 
						: homeController.getSelectedCategory().getSelectedOrAllSources()) {
						LOG.info(" src = " + src.getId());
					for (ItemWebBean item : src.getSortedItems()) {
						assertTrue(item.getHtmlContent().length() > 1);
						LOG.info("  item = " + item.getId());
					}
				}
				//assertTrue(true);
			}

		}).run();

	}
	
	/**
	 * call to setComplete in order to store data in database.
	 * @see org.esupportail.commons.test.AbstractTest#onSetUpInTransaction()
	 */
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		setComplete();
	}
	
}
