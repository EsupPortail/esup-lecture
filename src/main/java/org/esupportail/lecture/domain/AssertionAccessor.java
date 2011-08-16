package org.esupportail.lecture.domain;

import org.jasig.cas.client.validation.Assertion;

public interface AssertionAccessor {

	public void setAssertion(Assertion assertion);

	public Assertion getAssertion();

}
