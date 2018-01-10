package com.unicool.ch2o_bluetooth.ui.dummy;

import com.unicool.ch2o_bluetooth.ui.fragment.DashboardFragment;
import com.unicool.ch2o_bluetooth.ui.fragment.HomeFragment;
import com.unicool.ch2o_bluetooth.ui.fragment.NotificationsFragment;

/**
 * 导航栏
 */
public enum MainTabs {
    H(0, "Home", HomeFragment.class, "H"),
    D(1, "Dashboard", DashboardFragment.class, "D"),
    N(2, "Notifications", NotificationsFragment.class, "N");

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