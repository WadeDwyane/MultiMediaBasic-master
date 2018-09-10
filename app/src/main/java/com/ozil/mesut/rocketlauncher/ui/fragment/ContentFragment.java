package com.ozil.mesut.rocketlauncher.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.ui.adapter.ContentAdapter;

import java.util.ArrayList;

public class ContentFragment extends LazyLoadFragment {

    private View viewContent;
    private int mType = 0;
    private String mTitle;
    private RecyclerView mRecyclerView;

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.fragment_content, container, false);
        mRecyclerView = viewContent.findViewById(R.id.recyclerview);
        return viewContent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ContentFragment", "---onResume---");
    }

    @Override
    public void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add("我的审批，哈哈哈哈啊哈哈 " + i);
        }

        //赫兹伯格(双因素):
        //麦克格雷(X Y):
        //俘虏木(期望理论)
        //麦克李兰(成就动机理论)

        ContentAdapter contentAdapter = new ContentAdapter(getContext(), list);
        mRecyclerView.setAdapter(contentAdapter);
    }
}
