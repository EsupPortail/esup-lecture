/**
 * 
 */
package org.esupportail.lecture.web.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author bourges
 *
 */
public class DeviceRegexesHolder implements InitializingBean {
	/**
	 * Regexes of mobile device user agents
	 */
	private List<Pattern> mobileDeviceRegexes = null;

	/**
	 * @return the mobileDeviceRegexes
	 */
	public List<Pattern> getMobileDeviceRegexes() {
		return mobileDeviceRegexes;
	}

	public void setMobileDeviceRegexes(List<String> patterns) {
		this.mobileDeviceRegexes = new ArrayList<Pattern>();
		for (String pattern : patterns) {
			this.mobileDeviceRegexes.add(Pattern.compile(pattern));
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.mobileDeviceRegexes, 
				"property mobileDeviceRegexes of class " + this.getClass().getName() 
				+ " can not be null");
	}


}
