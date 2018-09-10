package com.ozil.mesut.rocketlauncher.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.ui.adapter.TestFragmentAdapter;

import java.util.Arrays;

public class TestFragment1 extends android.support.v4.app.Fragment {

    private View viewContent;
    private TabLayout tab_essence;
    public ViewPager vp_essence;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.fragment_test_1, container, false);
        initConentView(viewContent);
        initData();
        return viewContent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TestFragment", "TestFragment1  --onResume()---方法走了");
    }

    public void initConentView(View viewContent) {
        this.tab_essence = (TabLayout) viewContent.findViewById(R.id.tab_essence);
        this.vp_essence = viewContent.findViewById(R.id.vp_essence);
    }

    public void initData() {
        //获取标签数据
        String[] titles = getResources().getStringArray(R.array.me_approval);

        //创建一个viewpager的adapter
        TestFragmentAdapter adapter = new TestFragmentAdapter(getFragmentManager(),
                Arrays.asList(titles));
        /*CustomFragmentPagerAdapter adapter =
                new CustomFragmentPagerAdapter(getFragmentManager(), Arrays.asList(titles));*/

        this.vp_essence.setAdapter(adapter);

        //将TabLayout和ViewPager关联起来
        this.tab_essence.setupWithViewPager(this.vp_essence);
    }

    public static TestFragment1 getInstance() {
        TestFragment1 fragment1 = new TestFragment1();
        return fragment1;
    }
}
