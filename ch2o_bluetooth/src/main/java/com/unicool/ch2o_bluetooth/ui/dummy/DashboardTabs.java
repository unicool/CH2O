package com.unicool.ch2o_bluetooth.ui.dummy;

import com.unicool.ch2o_bluetooth.ui.fragment.DashboardItemFragment;

/**
 * Dashboard 导航栏
 */
public enum DashboardTabs {
    A(0, "ItemA", DashboardItemFragment.class, "A"),
    B(1, "ItemB", DashboardItemFragment.class, "B"),
    C(2, "ItemC", DashboardItemFragment.class, "C");

    public int index;
    public String title;
    public Class<?> clazz;
    public String arg;

    DashboardTabs(int index, String title, Class<?> clazz, String arg) {
        this.index = index;
        this.title = title;
        this.clazz = clazz;
        this.arg = arg;
    }
}