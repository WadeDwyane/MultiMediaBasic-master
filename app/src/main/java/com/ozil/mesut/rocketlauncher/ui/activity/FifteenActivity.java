package com.ozil.mesut.rocketlauncher.ui.activity;

import com.ozil.mesut.flowtaglayout.FlowTagLayout;
import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.ui.adapter.TagAdapter;

/**
 * @author kui.liu
 * @time 2018/8/6 11:06
 */
public class FifteenActivity extends BaseActivity {

    private FlowTagLayout mColorFlowTagLayout;
    private FlowTagLayout mSizeFlowTagLayout;
    private FlowTagLayout mMobileFlowTagLayout;
    private TagAdapter<String> mSizeTagAdapter;
    private TagAdapter<String> mColorTagAdapter;
    private TagAdapter<String> mMobileTagAdapter;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fifteen;
    }
}
