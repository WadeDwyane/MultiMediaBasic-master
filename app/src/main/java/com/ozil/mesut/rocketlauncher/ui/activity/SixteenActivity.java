package com.ozil.mesut.rocketlauncher.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.bean.ProcessChooseBean;
import com.ozil.mesut.rocketlauncher.bean.ProcessChooseSection;
import com.ozil.mesut.rocketlauncher.ui.adapter.ProcessChooseAdapter;

import java.util.ArrayList;

/**
 * @author kui.liu
 * @time 2018/8/6 16:29
 */
public class SixteenActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        ArrayList<ProcessChooseSection> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new ProcessChooseSection(true, "标题 " + i));
            for (int j = 0; j < 5; j++) {
                list.add(new ProcessChooseSection(new ProcessChooseBean("key" + j, "value" + j)));
            }
        }

        ProcessChooseAdapter mChooseAdapter = new ProcessChooseAdapter(
                R.layout.item_process_choose_content,
                R.layout.item_process_choose_header,
                list);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mChooseAdapter);


    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rv_test);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sixteen;
    }
}
