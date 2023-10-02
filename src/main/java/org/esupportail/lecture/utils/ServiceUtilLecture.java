package org.esupportail.lecture.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.ItemWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;

public class ServiceUtilLecture {
	/**
	 * log instance.
	 */
	protected static final Log LOG = LogFactory.getLog(ServiceUtilLecture.class);
	
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

	public static void sortItemInHilightSources( List<CategoryWebBean> listCat) {
		
		if (listCat == null) return;
		for (CategoryWebBean cat : listCat) {
			
			for (SourceWebBean src : cat.getSources()) {
				if (src.getHighlight()) {
					sortItemsByPubDate(src.getItems());
				}
			}
		}
	}
	
	public static void sortItemsByPubDate(List<ItemWebBean> listItems ) {
		if (listItems == null) return ;
		Collections.sort(listItems, new Comparator<ItemWebBean>() {
			@Override
			public int compare(ItemWebBean item1, ItemWebBean item2) {
				Date d1 = item1.getPubDate();
				Date d2 = item2.getPubDate();
				if (d1 == null ) {
					if (d2 == null) return 0;
					return -1;
				} 
				if (d2 == null) return 1;
				if (d1.before(d2)) return 1;
				if (d1.after(d2)) return -1;
				return 0;
			}
		});
		
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
		int nbrArticleALaUneNonLu = 0;
		
			// les id des items deja dans listeItemAcceuil
		Set<String> idBean = new HashSet<String>();
		
		
		
		// on traite tous les Highlight "a la une" en premier
		for (CategoryWebBean cat : listCat) {
			
			for (SourceWebBean src : cat.getSources()) {
			
				if (src.getHighlight()) {
					LOG.debug("getListItemAccueil: SOURCE A LA UNE :"+ src.getName());
					for (ItemWebBean item : src.getItems()) {
						if (idBean.add(item.getId())) {
								// si pas deja la on ajoute
							listeItemAcceuil.add(item);
							if (!item.isRead()) {
								nbrArticleNonLu++;
								nbrArticleALaUneNonLu++;
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
		contexte.setNbrUnreadItemHighlight(nbrArticleALaUneNonLu);

		if (!completed(listeItemAcceuil, listeItemNonLue, nbMinArticle) ){
			completed(listeItemAcceuil, listeItemDejaLue, nbMinArticle);
		}
		return listeItemAcceuil;

	}

	/**
	 * Donne la liste des articles à afficher en page d'accueil si on veut masquer les articles déjà lus
	 * NbrUnreadItem et setNbrUnreadItemHighlight ont été calculés au préalable et sont dispo dans le contexte
	 * @param contexte
	 * @param listCat
	 * @return
	 */
	public static List<ItemWebBean> getListItemAccueilMasquerDejaLues(ContextWebBean contexte, List<CategoryWebBean> listCat) {

		// Nombre d'articles minimum a afficher si possible.
		int nbMinArticle = contexte.getNombreArticle();

		// Les Items a présenter en page d'accueil. C'est le résultat final.
		List<ItemWebBean> listeItemAcceuil = new ArrayList<ItemWebBean>();

		// Les items pas encore lus a présenter apres les à la une
		List<ItemWebBean> listeItemNonLue = new ArrayList<ItemWebBean>();

		// Les id des items deja dans listeItemAcceuil
		Set<String> idBean = new HashSet<String>();

		// Première étape : on va chercher les à la une
		for (CategoryWebBean cat : listCat) {
			for (SourceWebBean src : cat.getSources()) {
				if (src.getHighlight()) {
					if(src.getItems().size() >= nbMinArticle){
						if(contexte.getNbrUnreadItemHighlight() >= nbMinArticle){
							// 1. Cas ou on a plus de nbMinArticle articles à la une non lus
							// On affiche alors juste tous les à la une non lus
							for (ItemWebBean item : src.getItems()) {
								if (!item.isRead()){
									listeItemAcceuil.add(item);
								}
							}
						}
						// 2. Cas ou on a plus de nbMinArticle articles à la une, mais pas plus de nbMinArticle articles à la une non lus
						else{
							// On ajoute d'abord les non lus
							for (ItemWebBean item : src.getItems()) {
								if (!item.isRead()){
									if (idBean.add(item.getId())) {
										listeItemAcceuil.add(item);
									}
								}
							}
							// Puis on complète avec des lus
							for (ItemWebBean item : src.getItems()) {
								if (item.isRead() && listeItemAcceuil.size() < nbMinArticle){
									if (idBean.add(item.getId())) {
										listeItemAcceuil.add(item);
									}
								}
							}
						}
					}
					// 3. Cas ou on a moins de nbMinArticle articles à la une
					// On complète alors jusqu'à nbMinArticle avec des autres non lus
					else {
						// On ajoute tous les à la une non lues puis lues
						for (ItemWebBean item : src.getItems()) {
							if (!item.isRead()){
								if (idBean.add(item.getId())) {
									listeItemAcceuil.add(item);
								}
							}
						}
						for (ItemWebBean item : src.getItems()) {
							if (item.isRead()){
								if (idBean.add(item.getId())) {
									listeItemAcceuil.add(item);
								}
							}
						}
						// On ajoutera les autres dans la boucle suivante
					}
				}
			}
		}

		// Deuxième étape : on traite les articles qui ne sont pas "a la une" (on ne prend que les non lus)
		if(listeItemAcceuil.size() < nbMinArticle){
			for (CategoryWebBean cat : listCat) {
				for (SourceWebBean src : cat.getSources()) {
					if (!src.getHighlight()) {
						for (ItemWebBean item : src.getItems()) {
							if ( idBean.add(item.getId())) {
								if (!item.isRead()) {
									listeItemNonLue.add(item);
								}
							}
						}
					}
				}
			}

			// On complète avec les articles qui manquent (ordonnés par date) pour le cas 3. jusqu'à nbMinArticle
			// On trie par date de publication
			Collections.sort(listeItemNonLue, new Comparator<ItemWebBean>() {
				@Override
				public int compare(ItemWebBean obj1, ItemWebBean obj2) {
					Date date1 = obj1.getPubDate();
					Date date2 = obj2.getPubDate();
					return -date1.compareTo(date2);
				}
			});
			// Puis on ajoute les X premiers si on en a assez
			final int nbArticlesMaxaAjouter = nbMinArticle-listeItemAcceuil.size();
			for(int i=0; i<nbArticlesMaxaAjouter; i++){
				if(listeItemNonLue.size() > i){
					listeItemAcceuil.add(listeItemNonLue.get(i));
				}
			}
		}

		return listeItemAcceuil;

	}

	public static int compteNombreArticleNonLu(ContextWebBean contexte) {
		List<CategoryWebBean> listCat = new ArrayList<CategoryWebBean>();
		listCat = contexte.getCategories();
		int nbrArtic = 0;
		// TODO Auto-generated method stub
		
		HashSet<String> idsNonLues = new HashSet<String>();
		
		for (CategoryWebBean cat : listCat) {

			for (SourceWebBean src : cat.getSources()) {

				for (ItemWebBean item : src.getItems()) {
					if (!item.isRead()) {
						if (idsNonLues.add(item.getId())) {
							nbrArtic++;
						}
					}
				}
			}
		}
		return nbrArtic;

	}

}
