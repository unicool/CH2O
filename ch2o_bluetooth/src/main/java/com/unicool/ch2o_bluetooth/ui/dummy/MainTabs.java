package com.unicool.ch2o_bluetooth.ui.dummy;

import com.unicool.ch2o_bluetooth.ui.fragment.HomeFragment;

/**
 * 导航栏
 */
public enum MainTabs {
    H(0, "Home", HomeFragment.class, "H");

    public int index;
    public String title;
    public Class<?> clazz;
    public String arg;

    MainTabs(int index, String title, Class<?> clazz, String arg) {
        this.index = index;
        this.title = title;
        this.clazz = clazz;
        this.arg = arg;
    }
}