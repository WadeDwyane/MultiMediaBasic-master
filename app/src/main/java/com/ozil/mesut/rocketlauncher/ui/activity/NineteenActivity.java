package com.ozil.mesut.rocketlauncher.ui.activity;

import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.ui.fragment.TestFragment1;
import com.ozil.mesut.rocketlauncher.ui.fragment.TestFragment2;
import com.ozil.mesut.rocketlauncher.view.LeftTabItem;
import com.ozil.mesut.rocketlauncher.view.RightTabItem;
import com.ozil.mesut.rocketlauncher.view.TabItem;

import java.util.ArrayList;

/**
 * @author kui.liu
 * @time 2018/8/29 16:07
 */
public class NineteenActivity extends BaseActivity {
    private ArrayList<Object> mTableItemList;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        initTabData();
        initTabHost();
    }

    private void initTabHost() {
        //实例化FragmentTabHost对象
        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //去掉分割线
        fragmentTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < mTableItemList.size(); i++) {
            TabItem tabItem = (TabItem) mTableItemList.get(i);
            //实例化一个TabSpec,设置tab的名称和视图
            TabHost.TabSpec tabSpec = fragmentTabHost.
                    newTabSpec(tabItem.getTitleString()).setIndicator(tabItem.getView());
            fragmentTabHost.addTab(tabSpec, tabItem.getFragmentClass(), null);

            //给Tab按钮设置背景
            fragmentTabHost.getTabWidget().getChildAt(i).
                    setBackgroundColor(getResources().getColor(R.color.main_bottom_bg));

            //默认选中第一个tab
            if (i == 0) {
                tabItem.setChecked(true);
            }
        }

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //重置Tab样式
                for (int i = 0; i < mTableItemList.size(); i++) {
                    TabItem tabitem = (TabItem) mTableItemList.get(i);
                    if (tabId.equals(tabitem.getTitleString())) {
                        tabitem.setChecked(true);
                    } else {
                        tabitem.setChecked(false);
                    }
                }
            }
        });

    }

    @Override
    protected void initView() {
    }

    private void initTabData() {
        //初始化Tab数据
        mTableItemList = new ArrayList<>();
        //添加tab
        mTableItemList.add(new LeftTabItem(this, R.mipmap.approval, R.mipmap.approval,
                R.string.me_approval, TestFragment1.class));
        mTableItemList.add(new RightTabItem(this, R.mipmap.cc, R.mipmap.cc,
                R.string.me_launch, TestFragment2.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_nineteen;
    }

}
