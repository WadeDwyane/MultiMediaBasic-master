package com.ozil.mesut.rocketlauncher.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ozil.mesut.lazyviewpager.LazyFragmentPagerAdapter;
import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.ui.adapter.ContentAdapter;

import java.util.ArrayList;


/**
 * @author kui.liu
 * @time 2018/9/4 10:09
 */
public class CustomFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable{

    private static final String KEY_POSITION = "position";

    private View viewContent;
    private int mType = 0;
    private String mTitle;
    private RecyclerView mRecyclerView;

    public static CustomFragment getInstance(int position) {
        CustomFragment customFragment = new CustomFragment();
        Bundle args = new Bundle(1);
        args.putInt(KEY_POSITION, position);
        customFragment.setArguments(args);
        return customFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //布局文件中只有一个居中的TextView
        viewContent = inflater.inflate(R.layout.fragment_content, container, false);
        mRecyclerView = viewContent.findViewById(R.id.recyclerview);
        initData();
        return viewContent;
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("我的审批，哈哈哈哈啊哈哈" + i);
        }

        ContentAdapter contentAdapter = new ContentAdapter(getContext(), list);
        mRecyclerView.setAdapter(contentAdapter);

    }
}
