package com.ozil.mesut.rocketlauncher.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ozil.mesut.rocketlauncher.ui.fragment.ContentFragment;

import java.util.List;

public class TestFragmentAdapter extends FragmentStatePagerAdapter {

    public static final String TAB_TAG = "@dream@";

    private List<String> mTitles;

    public TestFragmentAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        //初始化Fragment数据

        //设置不同的Fragment

        ContentFragment fragment = new ContentFragment();
        String title = mTitles.get(position);
        fragment.setType(0);
        fragment.setTitle(title);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

