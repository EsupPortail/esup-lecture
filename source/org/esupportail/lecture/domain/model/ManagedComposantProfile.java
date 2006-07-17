package org.esupportail.lecture.domain.model;


interface ManagedComposantProfile extends ComposantProfile {
	
	public Accessibility getAccess(); 
	
	public void setAccess(Accessibility access);

	public VisibilitySets getVisibility();

	public void setVisibility(VisibilitySets visibility);

	public int getTtl();
	
	public void setTtl(int ttl) ;
	
	public void setVisibilityAllowed(DefAndContentSets d);
	
	public DefAndContentSets getVisibilityAllowed();
	
	public void setVisibilityAutoSubcribed(DefAndContentSets d);
	
	public DefAndContentSets getVisibilityAutoSubscribed();
	
	public void setVisibilityObliged(DefAndContentSets d);

	public DefAndContentSets getVisibilityObliged();
	

}
