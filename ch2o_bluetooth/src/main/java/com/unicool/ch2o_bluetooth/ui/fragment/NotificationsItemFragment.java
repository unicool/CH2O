package com.unicool.ch2o_bluetooth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicool.ch2o_bluetooth.R;
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
public class NotificationsItemFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_item, null, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int layout_height = getArguments().getInt("Layout_Height", ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (layout_height > 0) {
            layout_height = (int) CommUtils.sp2px(getContext(), layout_height);
        }
        lp.height = layout_height;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
