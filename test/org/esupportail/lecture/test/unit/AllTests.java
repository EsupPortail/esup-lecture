package org.esupportail.lecture.test.unit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.esupportail.lecture.test");
		//$JUnit-BEGIN$
	//	suite.addTestSuite(Bidon.class);
		//$JUnit-END$
		return suite;
	}

}
