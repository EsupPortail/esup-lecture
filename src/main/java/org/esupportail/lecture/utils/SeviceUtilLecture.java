package org.esupportail.lecture.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class SeviceUtilLecture {

	public static List<CategoryWebBean> trierListCategorie(List<CategoryWebBean> listCat, String idCat, String idSrc,
			String nomSrc, String filtreNonLu) {
		List<CategoryWebBean> listCatFiltre = new ArrayList<CategoryWebBean>();// contient
																				// un
																				// seul
																				// élément
		if (!"".equals(idCat) && idCat != null) {
			for (CategoryWebBean cat : listCat) {
				if (cat.getId().equals(idCat)) {
					if (!"".equals(idSrc) && idSrc != null) {
						for (SourceWebBean src : cat.getSources()) {
							if (idSrc.equals(src.getId())) {
								List<SourceWebBean> sources = new ArrayList<SourceWebBean>();
								sources.add(src);
								CategoryWebBean categoryWebBean = new CategoryWebBean();
								categoryWebBean.setAvailabilityMode(cat.getAvailabilityMode());
								categoryWebBean.setDescription(cat.getDescription());
								categoryWebBean.setId(cat.getId());
								categoryWebBean.setName(cat.getName());
								categoryWebBean.setSources(sources);
								categoryWebBean.setUserCanMarkRead(cat.isUserCanMarkRead());
								listCatFiltre.add(categoryWebBean);
							}
						}
					} else {
						if (!"".equals(nomSrc) && nomSrc != null) {
							for (SourceWebBean src : cat.getSources()) {
								if (nomSrc.equals(src.getName())) {
									List<SourceWebBean> sources = new ArrayList<SourceWebBean>();
									sources.add(src);
									CategoryWebBean categoryWebBean = new CategoryWebBean();
									categoryWebBean.setAvailabilityMode(cat.getAvailabilityMode());
									categoryWebBean.setDescription(cat.getDescription());
									categoryWebBean.setId(cat.getId());
									categoryWebBean.setName(cat.getName());
									categoryWebBean.setSources(sources);
									categoryWebBean.setUserCanMarkRead(cat.isUserCanMarkRead());
									listCatFiltre.add(categoryWebBean);
								}
							}
						} else {
							listCatFiltre.add(cat);
						}
					}
				}
			}
		} else {
			listCatFiltre.addAll(listCat);
		}

		if ("val2".equals(filtreNonLu)) {
			for (CategoryWebBean cat : listCatFiltre) {
				for (SourceWebBean src : cat.getSources()) {
					List<ItemWebBean> listeItemCopy = new ArrayList<ItemWebBean>(src.getItems());
					for (ItemWebBean item : src.getItems()) {
						if (item.isRead()) {
							listeItemCopy.remove(item);
						}
					}
					src.setItems(listeItemCopy);
				}
			}
		}
		if ("val3".equals(filtreNonLu)) {
			for (CategoryWebBean cat : listCatFiltre) {
				for (SourceWebBean src : cat.getSources()) {
					Collections.sort(src.getItems(), new Comparator<ItemWebBean>() {
						@Override
						public int compare(ItemWebBean item1, ItemWebBean item2) {
							return Boolean.compare(item1.isRead(), item2.isRead());
						}
					});

				}
			}
		}

		return listCatFiltre;

	}
	
		/**
		 * Complete le resultat avec les elements de la source pour atteindre minElem .
		 * idInRsultat doit contenir les identifiants des item deja dans resultat
		 * quand le restultat est complet return true.
		 * sinon return false (cas ou la source est trop petite);
		 * @param source
		 * @param resultat
		 * @param minElem
		 * @param idInResultat
		 * @return
		 */
	private static boolean completed( 
								List<ItemWebBean> resultat,
								List<ItemWebBean> source , 
								int minElem) {
		if (resultat.size() < minElem) {
			for (ItemWebBean item : source) {
				resultat.add(item);
				if (resultat.size() >= minElem ) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	
	/**
	 * Donne la liste des articles à afficher en page d'accueil 
	 * et calcule dans contexte.nbrUnreadItem le nombre d'articles pas encore lue.  
	 * @param contexte
	 * @param listCat
	 * @return
	 */
	public static List<ItemWebBean> getListItemAccueil(ContextWebBean contexte, List<CategoryWebBean> listCat) {
		
			// nombre d'article minimum a afficher si possible.
		int nbMinArticle = contexte.getNombreArticle();
		
			// Les Items a présenter en page d'accueil. C'est le résultat final.
		List<ItemWebBean> listeItemAcceuil = new ArrayList<ItemWebBean>();
		
			// les items pas encore lue a présenter apres les à la une
		List<ItemWebBean> listeItemNonLue = new ArrayList<ItemWebBean>();
		
			// les items  deja lue pour completer la liste juste au nombre d'article minimum.
		List<ItemWebBean> listeItemDejaLue = new ArrayList<ItemWebBean>();

		int nbrArticleNonLu = 0;
		
			// les id des items deja dans listeItemAcceuil
		Set<String> idBean = new HashSet<String>();
		
		
		
		// on traite tous les Highlight "a la une" en premier
		for (CategoryWebBean cat : listCat) {
			
			for (SourceWebBean src : cat.getSources()) {
			
				if (src.getHighlight()) {
					for (ItemWebBean item : src.getItems()) {
						if (idBean.add(item.getId())) {
								// si pas deja la on ajoute
							listeItemAcceuil.add(item);
							if (!item.isRead()) {
								nbrArticleNonLu++;
							}
						}
					}
				}
			}
		}
		
			// on traite les suivants ceux qui ne sont pas "a la une" :
		for (CategoryWebBean cat : listCat) {
			for (SourceWebBean src : cat.getSources()) {
				if (! src.getHighlight()) {
					for (ItemWebBean item : src.getItems()) {
						if ( idBean.add(item.getId())) {
							if (item.isRead()) {
								listeItemDejaLue.add(item);
							} else {
								listeItemNonLue.add(item);
								nbrArticleNonLu++;
							}
						}
					}
				}
			}
		}
		
		contexte.setNbrUnreadItem(nbrArticleNonLu);
		
		if (!completed(listeItemAcceuil, listeItemNonLue, nbMinArticle) ){
			completed(listeItemAcceuil, listeItemDejaLue, nbMinArticle);
		}
		return listeItemAcceuil;

	}

	public static int compteNombreArticleNonLu(ContextWebBean contexte) {
		List<CategoryWebBean> listCat = new ArrayList<CategoryWebBean>();
		listCat = contexte.getCategories();
		int nbrArtic = 0;
		// TODO Auto-generated method stub
		for (CategoryWebBean cat : listCat) {

			for (SourceWebBean src : cat.getSources()) {

				for (ItemWebBean item : src.getItems()) {
					if (!item.isRead()) {
						nbrArtic++;
					}
				}
			}
		}
		return nbrArtic;

	}

}
