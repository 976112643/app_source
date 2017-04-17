package com.wq.businessdirectory.common.mode;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by WQ on 2017/4/17.
 */

public class BottomTab implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public BottomTab(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    public static CustomTabEntity createTab(String title, int selectedIcon, int unSelectedIcon){
        return new BottomTab(title, selectedIcon, unSelectedIcon);
    }
    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
