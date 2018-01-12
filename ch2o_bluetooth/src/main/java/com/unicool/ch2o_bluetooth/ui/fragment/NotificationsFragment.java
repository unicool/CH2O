package com.unicool.ch2o_bluetooth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unicool.ch2o_bluetooth.R;
import com.unicool.ch2o_bluetooth.presenter.adapter.BasicPagerAdapter;
import com.unicool.ch2o_bluetooth.ui.fragment.base.BaseFragment;
import com.unicool.ch2o_bluetooth.util.CommUtils;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.ui.fragment
 *  @文件名:   NotificationsFragment
 *  @创建者:   cjf
 *  @创建时间:  2017/11/13 20:02
 *  @描述：    TODO
 */
public class NotificationsFragment extends BaseFragment {

    private ViewPager mNotiViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.view_amap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), v.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        TabLayout notiTabLayout = view.findViewById(R.id.noti_tabLayout);
        mNotiViewPager = view.findViewById(R.id.noti_viewPager);
//        notiViewPager.setAdapter(new BasicPagerAdapter(getChildFragmentManager(), notiViewPager));
        addTabs(new BasicPagerAdapter(getChildFragmentManager(), mNotiViewPager));
        notiTabLayout.setupWithViewPager(mNotiViewPager);

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mNotiViewPager.getLayoutParams().height = (int) CommUtils.sp2px(getContext(), 300);
                        break;
                    case 1:
                        mNotiViewPager.getLayoutParams().height = (int) CommUtils.sp2px(getContext(), 600);
                        break;
                    case 2:
                        mNotiViewPager.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        break;
                    case 3:
                        mNotiViewPager.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        break;
                    case 4:
                        mNotiViewPager.getLayoutParams().height = (int) CommUtils.sp2px(getContext(), 100);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mNotiViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    private void addTabs(BasicPagerAdapter adapter) {
        adapter.addTab(NotificationsItemFragment.class, "Item1", getBundle(ViewGroup.LayoutParams.MATCH_PARENT));
        adapter.addTab(NotificationsItemFragment.class, "Item2", getBundle(ViewGroup.LayoutParams.WRAP_CONTENT));
        adapter.addTab(NotificationsItemFragment.class, "Item3", getBundle(200));
        adapter.addTab(NotificationsItemFragment.class, "Item4", getBundle(800));
        adapter.addTab(NotificationsItemFragment.class, "Item5", getBundle(500));
    }

    private Bundle getBundle(Object arg) {
        Bundle bundle = new Bundle();
        bundle.putInt("Layout_Height", (int) arg);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNotiViewPager.removeOnPageChangeListener(mOnPageChangeListener);
    }
}
