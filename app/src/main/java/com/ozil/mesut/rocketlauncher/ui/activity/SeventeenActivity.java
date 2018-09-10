package com.ozil.mesut.rocketlauncher.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.ui.adapter.MultChooseAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author kui.liu
 * @time 2018/8/23 16:53
 */
public class SeventeenActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> mList;
    private MultChooseAdapter mAdapter;

    @Override
    protected void initListener() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, Boolean> map = mAdapter.getMap();
                Set<Integer> keySet = map.keySet();
                Iterator<Integer> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    Integer next = iterator.next();
                    Log.i("SeventeenActivity", "position = " + next);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add("选项" + i);
        }
        mAdapter = new MultChooseAdapter(this, mList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rv_seventeen);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seventeen;
    }

    //规划进度管理 ---> 进度管理计划
    //定义活动 ---> WBS(可交付成果)  定义活动(输出的是活动) 活动清单/活动属性/里程碑清单
    //排列活动顺序 --->紧前关系绘图法(PDM)  提前量(-)和滞后量(+)  活动依赖关系(强制依赖关系 选择依赖关系 内部依赖关系 外部依赖关系)
                    //项目进度网络图
    //估算活动持续时间 ---> 持续时间估算 估算依据 工具和方法:专家判断
    //制定进度计划 ---> 关键路径法/资源优化/提前量和滞后量/进度压缩/

}
