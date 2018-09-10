package com.ozil.mesut.rocketlauncher.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    /**
     * 需要申请的运行时权限
     */
    private String[] permissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    //用户拒绝的权限列表
    private List<String> mPermissionList = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST = 1001;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        //检查运行时权限
        checkPermissions();
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                        PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @OnClick({R.id.btn_one, R.id.btn_two, R.id.btn_three,
            R.id.btn_four, R.id.btn_fifth, R.id.btn_six,
            R.id.btn_seven, R.id.btn_eight, R.id.btn_nine,
            R.id.btn_ten, R.id.btn_eleven, R.id.btn_twelve,
            R.id.btn_thirteen, R.id.btn_fourteen, R.id.btn_fifteen,
            R.id.btn_sixteen, R.id.btn_seventeen, R.id.btn_eighteen,
            R.id.btn_nineteen, R.id.btn_twenty, R.id.btn_twentyoneactivity})
    protected void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_one:
                intent = new Intent(this, FirstActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_two:
                intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_three:
                intent = new Intent(this, ThirdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_four:
                intent = new Intent(this, ForthActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fifth:
                intent = new Intent(this, FifthActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_six:
                intent = new Intent(this, SixActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_seven:
                intent = new Intent(this, SevenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_eight:
                intent = new Intent(this, EightActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_nine:
                intent = new Intent(this, NineActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_ten:
                intent = new Intent(this, TenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_eleven:
                intent = new Intent(this, ElevenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_twelve:
                intent = new Intent(this, TwelveActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_thirteen:
                intent = new Intent(this, ThirteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fourteen:
                intent = new Intent(this, FourteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_fifteen:
                intent = new Intent(this, FifteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sixteen:
                intent = new Intent(this, SixteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_seventeen:
                intent = new Intent(this, SeventeenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_eighteen:
                //TODO:调用rhino的例子
                intent = new Intent(this, EighteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_nineteen:
                //TODO:切换顶部的tab
                intent = new Intent(this, NineteenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_twenty:
                intent = new Intent(this, TwentyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_twentyoneactivity:
                intent = new Intent(this, TwentyOneActivity.class);
                startActivity(intent);
                break;
        }
    }
}
