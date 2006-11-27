package org.esupportail.lecture.domain.beans;

import org.esupportail.lecture.domain.model.CustomSource;
import org.esupportail.lecture.domain.model.SourceProfile;

/**
 * @author bourges
 * used to store source informations
 */
public class SourceBean {
	/**
	 * id of source
	 */
	private String id;
	/**
	 * name of source
	 */
	private String name;
	/**
	 * type of source
	 * "subscribed" --> The source is alloweb and subscribed by the user
	 * "notSubscribed" --> The source is alloweb and not yet subscribed by the user (used in edit mode)
	 * "obliged" --> The source is obliged: user can't subscribe or unsubscribe this source
	 */
	//TODO (GB) utiliser des constantes définies dans DomainTools pour ça.
	private String type;
	// TODO (GB) revoir comment concevoir cela : il faut aussi tenir compte des personnalSources qui n'ont pas de type
	
	public SourceBean(){}
	
	public SourceBean(CustomSource customSource){
		SourceProfile profile = customSource.getSourceProfile();
		
		setName(profile.getName());
		setId(profile.getId());
		
	}
	
	
	
	
	/**
	 * @return name of source
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return id of source
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return type of source
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		String string = "";
		string += "     Id = " + id.toString() + "\n";
		string += "     Name = " + name.toString() + "\n";
		string += "     Type = "; 
		if (type != null){
			string += type + "\n";
		}
		
		return string;
	}
	
	
}
