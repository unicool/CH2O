package com.unicool.ch2o_bluetooth.presenter.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class BasicPagerAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private List<PageTab> tabs = new ArrayList<>();

    public BasicPagerAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);

        this.mContext = viewPager.getContext();

        // 设置数据适配器
        viewPager.setAdapter(this);
    }

    public void addTab(Class<?> clazz, String title, Bundle bundle) {
        tabs.add(new PageTab(clazz, title, bundle));
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (tabs == null) {
            return null;
        }
        PageTab pageTab = tabs.get(position);
        return Fragment.instantiate(mContext, pageTab.clazz.getName(), pageTab.bundle);
    }

    @Override
    public int getCount() {
        if (tabs == null) {
            return 0;
        }
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabs == null) {
            return super.getPageTitle(position);
        }
        return tabs.get(position).title;
    }

    private class PageTab {
        Class<?> clazz;
        String title;
        Bundle bundle;

        private PageTab(Class<?> clazz, String title, Bundle bundle) {
            this.clazz = clazz;
            this.title = title;
            this.bundle = bundle;
        }
    }
}