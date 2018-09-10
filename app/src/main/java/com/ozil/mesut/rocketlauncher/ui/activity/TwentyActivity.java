package com.ozil.mesut.rocketlauncher.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.ui.adapter.ContentAdapter;

import java.util.ArrayList;

/**
 * @author kui.liu
 * @time 2018/9/4 10:03
 */
public class TwentyActivity extends BaseActivity{
    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(manager);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("我的审批，哈哈哈哈啊哈哈 " + i);
        }

        ContentAdapter contentAdapter = new ContentAdapter(this, list);
        mRecyclerView.setAdapter(contentAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_twenty;
    }
}
