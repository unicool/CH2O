package com.unicool.ch2o_bluetooth.ui.dummy;

import com.unicool.ch2o_bluetooth.ui.fragment.DashboardItemFragment;

/**
 * Dashboard 导航栏
 */
public enum DashboardTabs {
    A(0, "ItemA", DashboardItemFragment.class, "-1"),
    B(1, "ItemB", DashboardItemFragment.class, "-2"),
    C(2, "ItemC", DashboardItemFragment.class, "200"),
    D(3, "ItemD", DashboardItemFragment.class, "500"),
    E(4, "ItemE", DashboardItemFragment.class, "800");

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