package org.esupportail.lecture.dao;

import org.esupportail.lecture.domain.model.ItemDisplayMode;

/**
 * @author bourges
 *
 * This Class extends EnumUserType which is a Generic Hibernate UserType for java 5 Enun
 * @see EnumUserType
 */
public class ItemDisplayModeUserType extends EnumUserType<ItemDisplayMode> { 

    /**
     * Constructor
     */
    public ItemDisplayModeUserType() { 
        super(ItemDisplayMode.class); 
    } 
}