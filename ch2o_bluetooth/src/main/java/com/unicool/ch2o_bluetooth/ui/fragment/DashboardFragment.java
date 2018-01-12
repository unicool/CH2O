package com.unicool.ch2o_bluetooth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.unicool.ch2o_bluetooth.R;
import com.unicool.ch2o_bluetooth.ui.dummy.DashboardTabs;
import com.unicool.ch2o_bluetooth.ui.fragment.base.BaseFragment;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.ui.fragment
 *  @文件名:   DashboardFragment
 *  @创建者:   cjf
 *  @创建时间:  2017/11/13 20:02
 *  @描述：    TODO 
 */
public class DashboardFragment extends BaseFragment {

    private FragmentTabHost mFTabHost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFTabHost = view.findViewById(R.id.f_tab_host);
        initData();
    }

    private void initData() {
        // 1.初始化-关联
        mFTabHost.setup(getContext(), getActivity().getSupportFragmentManager(), R.id.fl_container);
        mFTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); //分割线

        //TabWidget tabWidget = mFTabHost.getTabWidget();
        //tabWidget.setOrientation(LinearLayout.VERTICAL); //标签栏垂直布局

        //LinearLayout.LayoutParams lp0 = (LinearLayout.LayoutParams) mFTabHost.getTabWidget().getLayoutParams();
        //lp0.height = LinearLayout.LayoutParams.MATCH_PARENT;

        //int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());//左边和右边的值(sp2px)
        for (DashboardTabs tab : DashboardTabs.values()) {
            // 2.配置选项卡
            TabHost.TabSpec tabSpec = mFTabHost.newTabSpec(tab.arg).setIndicator(tab.title);
            Bundle bundle = new Bundle();
            bundle.putString("arg", tab.arg);
            // 3.添加选项卡
            mFTabHost.addTab(tabSpec, tab.clazz, bundle);

            //View cv = tabWidget.getChildAt(tab.index);
            //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cv.getLayoutParams();
            //lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            //TextView title = (TextView) cv.findViewById(android.R.id.title);
            //title.setTextSize(textSize);
            //ImageView icon = (ImageView) cv.findViewById(android.R.id.icon);
        }

        mFTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(getActivity(), "tabId:" + tabId, Toast.LENGTH_SHORT).show();
                Log.d("DashboardFragment", "\t\t\tonTabChanged.onTabChanged\t\t\t" + tabId + mFTabHost.getCurrentTab());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
