package com.unicool.ch2o_bluetooth.ui.dummy;

import com.unicool.ch2o_bluetooth.ui.fragment.DashboardFragment;
import com.unicool.ch2o_bluetooth.ui.fragment.HomeFragment;
import com.unicool.ch2o_bluetooth.ui.fragment.NotificationsFragment;

/**
 * 导航栏
 */
public enum DispTabs {
    H(0, "报文", HomeFragment.class, "H"),
    D(1, "事件", DashboardFragment.class, "D"),
    N(2, "电话本", NotificationsFragment.class, "N");

    public int index;
    public String title;
    public Class<?> clazz;
    public String arg;

    DispTabs(int index, String title, Class<?> clazz, String arg) {
        this.index = index;
        this.title = title;
        this.clazz = clazz;
        this.arg = arg;
    }
}