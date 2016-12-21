package org.esupportail.lecture.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class SeviceUtilLecture {
	
	public static List<CategoryWebBean> trierListCategorie(List<CategoryWebBean> listCat,String idCat,String idSrc,String filtreNonLu){
    	List<CategoryWebBean> listCatFiltre = new ArrayList<CategoryWebBean>();//contient un seul élément
		if(!"".equals(idCat)&&idCat!=null){
    		for (CategoryWebBean cat : listCat) {
    			if(cat.getId().equals(idCat)){
    				if(!"".equals(idSrc)&&idSrc!=null){
    					for(SourceWebBean src:cat.getSources()){
    						if(src.getId().equals(idSrc)){
    						    List<SourceWebBean> sources = new ArrayList<SourceWebBean>();
    						    sources.add(src);
    							CategoryWebBean categoryWebBean =new CategoryWebBean();
    							categoryWebBean.setAvailabilityMode(cat.getAvailabilityMode());
    							categoryWebBean.setDescription(cat.getDescription());
    							categoryWebBean.setId(cat.getId());
    							categoryWebBean.setName(cat.getName());
    							categoryWebBean.setSources(sources);
    							categoryWebBean.setUserCanMarkRead(cat.isUserCanMarkRead());
    							listCatFiltre.add(categoryWebBean);
    						}
    					}
    				}else{
    					listCatFiltre.add(cat);
    				}
    			}
    		}
    	}else{
    		listCatFiltre.addAll(listCat);
    	}
    	
    	if("val2".equals(filtreNonLu)){
    		for (CategoryWebBean cat : listCatFiltre) {
    			for(SourceWebBean src:cat.getSources()){
    				List<ItemWebBean> listeItemCopy = new ArrayList<ItemWebBean>(src.getItems());
    				for(ItemWebBean item:src.getItems()){
    					if(item.isRead()){
    						listeItemCopy.remove(item);
    					}
    				}
    				src.setItems(listeItemCopy);
    			}	
    		}
    	}
    	if("val3".equals(filtreNonLu)){
    		for (CategoryWebBean cat : listCatFiltre) {
    			for(SourceWebBean src:cat.getSources()){
    						Collections.sort(src.getItems(), new Comparator<ItemWebBean>() {
    					        @Override
    					        public int compare(ItemWebBean item1, ItemWebBean item2)
    					        {
    					            return  Boolean.compare(item1.isRead(),item2.isRead());
    					        }
    					    });
    				
    				}
    			}	
    		}
		
		return listCatFiltre;
		
	}

}
