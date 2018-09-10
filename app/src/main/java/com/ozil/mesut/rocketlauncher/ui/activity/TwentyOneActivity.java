package com.ozil.mesut.rocketlauncher.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.ui.fragment.TestFragment1;
import com.ozil.mesut.rocketlauncher.ui.fragment.TestFragment2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author kui.liu
 * @time 2018/9/4 15:29
 */
public class TwentyOneActivity extends BaseActivity {

    @BindView(R.id.tv_not_selected)
    TextView mTvNotSelected;
    @BindView(R.id.launch_normal_container)
    RelativeLayout mLaunchNormalContainer;
    @BindView(R.id.tv_my_approval_selected)
    TextView mTvMyApprovalSelected;
    @BindView(R.id.imv_my_approval_selected)
    ImageView mImvMyApprovalSelected;
    @BindView(R.id.approval_selected_container)
    RelativeLayout mApprovalSelectedContainer;
    @BindView(R.id.container1)
    FrameLayout mContainer1;
    @BindView(R.id.imv_my_approval_not_selected)
    ImageView mImvMyApprovalNotSelected;
    @BindView(R.id.tv_my_approval_not_selected)
    TextView mTvMyApprovalNotSelected;
    @BindView(R.id.approval_normal_container)
    RelativeLayout mApprovalNormalContainer;
    @BindView(R.id.tv_my_launch_selected)
    TextView mTvMyLaunchSelected;
    @BindView(R.id.imv_my_launch_selected)
    ImageView mImvMyLaunchSelected;
    @BindView(R.id.launch_selected_container)
    RelativeLayout mLaunchSelectedContainer;
    @BindView(R.id.container2)
    FrameLayout mContainer2;
    private TestFragment1 mFragment1;
    private TestFragment2 mFragment2;
    int currentTabIndex = 0;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        //先将Fragment1添加进入容器中.
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mFragment1 = TestFragment1.getInstance();
        transaction.add(R.id.ll_content_container, mFragment1);
        transaction.show(mFragment1);
        transaction.commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_twentyone;
    }

    @OnClick({R.id.launch_normal_container, R.id.approval_selected_container,
            R.id.approval_normal_container, R.id.launch_selected_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.approval_selected_container:
                if (currentTabIndex == 1) {
                    showApproval();
                    currentTabIndex = 0;
                }
                break;
            case R.id.approval_normal_container:
                if (currentTabIndex == 1) {
                    showApproval();
                    currentTabIndex = 0;
                }
                break;
            case R.id.launch_normal_container:
                if (currentTabIndex == 0) {
                    showLaunch();
                    currentTabIndex = 1;
                }
                break;
            case R.id.launch_selected_container:
                if (currentTabIndex == 0) {
                    showLaunch();
                    currentTabIndex = 1;
                }
                break;
        }
    }

    public void showApproval() {
        mContainer1.setVisibility(View.VISIBLE);
        mContainer2.setVisibility(View.GONE);
        if (null == mFragment1) {
            mFragment1 = TestFragment1.getInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mFragment1);
        transaction.remove(mFragment2);
        mFragment1 = TestFragment1.getInstance();
        transaction.add(R.id.ll_content_container, mFragment1);
        transaction.commit();
    }

    public void showLaunch() {
        mContainer1.setVisibility(View.GONE);
        mContainer2.setVisibility(View.VISIBLE);
        //如何确保在切换tab的时候刷新列表
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(mFragment1);
        if (null != mFragment2) {
            transaction.remove(mFragment2);
        }
        mFragment2 = TestFragment2.getInstance();
        transaction.add(R.id.ll_content_container, mFragment2);
        transaction.commit();
    }
}
