package com.ozil.mesut.rocketlauncher.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.ozil.mesut.lazyviewpager.LazyFragmentPagerAdapter;
import com.ozil.mesut.rocketlauncher.ui.fragment.CustomFragment;

import java.util.List;


/**
 * @author kui.liu
 * @time 2018/9/4 10:07
 */
public class CustomFragmentPagerAdapter extends LazyFragmentPagerAdapter {

    private List<String> mTitles;

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CustomFragmentPagerAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return CustomFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
