package org.esupportail.lecture.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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

	public static List<ItemWebBean> getListItemAccueil(ContextWebBean contexte, List<CategoryWebBean> listCat) {
		List<ItemWebBean> listeItemNonDup = new ArrayList<ItemWebBean>();
		List<ItemWebBean> listeItemAcceuil = new ArrayList<ItemWebBean>();
		int nbrArticleNonLu = 0;
		Set<String> idBean = new HashSet<String>();
		int i = 0;
		for (CategoryWebBean cat : listCat) {

			for (SourceWebBean src : cat.getSources()) {

				for (ItemWebBean item : src.getItems()) {

					if (src.getHighlight()) {
						if (i < contexte.getNombreArticle()) {
							listeItemAcceuil.add(item);
							idBean.add(item.getId());
							if (!item.isRead()) {
								nbrArticleNonLu++;
							}
							i++;

						}
					} else {
						// liste des articles qui ne sont pas à la une
						if (!(idBean.contains(item.getId()))) {
							listeItemNonDup.add(item);
							idBean.add(item.getId());
							if (!item.isRead()) {
								nbrArticleNonLu++;
							}
						}
					}
				}
			}
		}
		contexte.setNbrUnreadItem(nbrArticleNonLu);
		if (i < contexte.getNombreArticle()) {
			for (ItemWebBean item : listeItemNonDup) {
				// non lu en premier
				if (!item.isRead() && i < contexte.getNombreArticle()) {
					listeItemAcceuil.add(item);
					i++;
				}
			}
			if (i < contexte.getNombreArticle()) {
				for (ItemWebBean item : listeItemNonDup) {
					// compléter par les lus
					if (item.isRead() && i < contexte.getNombreArticle()) {
						listeItemAcceuil.add(item);
						i++;
					}

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
