package org.esupportail.lecture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.lecture.domain.DomainService;
import org.esupportail.lecture.web.beans.CategoryWebBean;
import org.esupportail.lecture.web.beans.ContextWebBean;
import org.esupportail.lecture.web.beans.SourceWebBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by bourges on 26/06/14.
 */
@ContextConfiguration(locations="classpath:/properties/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SaisieSimplifeeTest {

    private static final Log log = LogFactory.getLog(SaisieSimplifeeTest.class);

    @Inject
    DomainService domainService;

    @Test
    /**
     * Context saisieNormale contains 2 categories
     * so ContextWebBean should contains 2 CategoryWebBean
     */
    public void nbCategories1() {
        String ctxName = "saisieNormale";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        int size = contextWebBean.getCategories().size();
        Assert.isTrue(size == 2,
                "ContextWebBean for context \"" + ctxName + "\" should have 2 CategoryWebBean but it have " + size);
    }

    @Test
    /**
     * Context saisieSimplifiee contains 4 categories but 2 have groupCategory attribute at true
     * so ContextWebBean should contains 3 CategoryWebBean
     */
    public void nbCategories2() {
        String ctxName = "saisieSimplifiee";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        int size = contextWebBean.getCategories().size();
        Assert.isTrue(size == 3,
                "ContextWebBean for context \"" + ctxName + "\" should have 3 CategoryWebBean but it have " + size);
    }

    @Test
    public void groupedCategoryContent1() {
        String ctxName = "saisieSimplifiee";
        String entityName = "Catégorie News 2";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        List<CategoryWebBean> list = contextWebBean.getCategories();
        boolean found = false;
        for (CategoryWebBean cat : list) {
            if (cat.getName().equals(entityName)) {
                found = true;
            }
        }
        Assert.isTrue(found,
                "ContextWebBean for context \"" + ctxName + "\" should have a CategoryWebBean with name as an entity name \""
                + entityName + "\"");
    }

    @Test
    public void groupedCategoryContent2() {
        String ctxName = "saisieSimplifiee";
        String entityName = "Catégorie News 2";
        String catName = "Exemples";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        List<CategoryWebBean> list = contextWebBean.getCategories();
        boolean found = false;
        for (CategoryWebBean cat : list) {
            if (cat.getName().equals(entityName)) {
                List<SourceWebBean> list2 = cat.getSources();
                if (list2 != null) {
                    for (SourceWebBean src : list2) {
                        if (src.getName().equals(catName)) {
                            found = true;
                        }
                    }
                }
            }
        }
        Assert.isTrue(found,
                "ContextWebBean for context \""
                + ctxName + "\" should have a CategoryWebBean \""
                + entityName + "\" with a source with a name as an category name \""
                + catName + "\"");
    }

    @Test
    public void groupedCategoryContent3() {
        String ctxName = "saisieSimplifiee";
        String entityName = "Catégorie News 2";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        List<CategoryWebBean> list = contextWebBean.getCategories();
        int nb = 0;
        for (CategoryWebBean cat : list) {
            if (cat.getName().equals(entityName)) {
                    nb = cat.getSources().size();
                }
        }
        Assert.isTrue(nb == 2,
                "ContextWebBean for context \""
                        + ctxName + "\" and CategoryWebBean \""
                        + entityName + "\" should have 2 SourceWebBean but it have " + nb);
    }

    @Test
    public void groupedCategoryContent4() {
        String ctxName = "saisieSimplifiee";
        String entityName = "Catégorie News 2";
        String catName = "Exemples";
        ContextWebBean contextWebBean = domainService.getContext("bourges", ctxName);
        List<CategoryWebBean> list = contextWebBean.getCategories();
        int nb = 0;
        for (CategoryWebBean cat : list) {
            if (cat.getName().equals(entityName)) {
                List<SourceWebBean> list2 = cat.getSources();
                for (SourceWebBean src : list2) {
                    if (src.getName().equals(catName)) {
                        nb += src.getItems().size();
                    }
                }
            }
        }
        Assert.isTrue(nb == 7,
                "ContextWebBean for context \""
                        + ctxName + "\" and  CategoryWebBean \""
                        + entityName + "\" and SourceWebBean \""
                        + catName + "\" should have 7 items but it have " + nb);
    }
}
