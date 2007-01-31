package org.esupportail.lecture.dao;

import org.esupportail.lecture.domain.model.ItemDisplayMode;

public class ItemDisplayModeUserType extends EnumUserType<ItemDisplayMode> { 
    public ItemDisplayModeUserType() { 
        super(ItemDisplayMode.class); 
    } 
}