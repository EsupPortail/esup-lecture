package org.esupportail.lecture.web.controllers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.AttributesMapper;
import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.NameClassPairCallbackHandler;
import org.springframework.ldap.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapContextSource;

import sun.print.resources.serviceui;

import com.sun.jndi.ldap.ctl.ResponseControlFactory;

public class ManyHomeController {

	/**
	 * Log instance.
	 */
	private static final Log LOG = LogFactory.getLog(ManyHomeController.class);

	static List<String> users = new ArrayList<String>();

	public static Test suite() {
		//find users in LDAP
		Hashtable env = new Hashtable(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://vldap.univ-rennes1.fr:389/dc=univ-rennes1,dc=fr");
		DirContext ctx;
		try {
			ctx = new InitialDirContext(env);
			SearchControls searchControls = new SearchControls(); 
			String[] attrs = {"uid"};
			searchControls.setReturningAttributes(attrs);
			// Search for objects that have those matching attributes
			NamingEnumeration entries = ctx.search("ou=People", "(departmentnumber=957)", searchControls);
//			NamingEnumeration entries = ctx.search("ou=People", "(|(uid=bourges)(uid=germes))", searchControls);
			while  (entries.hasMore()) {
				SearchResult res = (SearchResult) entries.next();
				String user = (String) res.getAttributes().get("uid").get();
				LOG.info("User = " + user);
				users.add(user);
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TestSuite suite = new TestSuite("Test for esup-lecture");
		//		for (int i = 0; i < 2; i++) {
		//			suite.addTestSuite(TestMainBeanWI.class);
		//			suite.addTestSuite(TestWIPerformAction.class);
		//		}
		for (int i = 0; i < users.size(); i++) {
			suite.addTestSuite(TestUserAuth.class);
			suite.addTestSuite(TestHomeController.class);	
			suite.addTestSuite(TestResetSession.class);
		}
		return suite;
	}

}
