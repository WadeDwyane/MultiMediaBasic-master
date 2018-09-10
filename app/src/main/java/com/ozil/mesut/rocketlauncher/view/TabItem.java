package com.ozil.mesut.rocketlauncher.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author kui.liu
 * @time 2018/8/30 15:18
 */
public abstract class TabItem {

    //正常情况下显示的图片
    public int imageNormal;
    //选中情况下显示的图片
    public int imagePress;
    //tab的名字
    public int title;
    public String titleString;

    //tab对应的fragment
    public Class<? extends Fragment> fragmentClass;

    public View view;
    public ImageView imageView;
    public TextView textView;
    public Context context;

    public TabItem(Context context, int imageNormal, int imagePress, int title,
                   Class<? extends Fragment> fragmentClass) {
        this.context = context;
        this.imageNormal = imageNormal;
        this.imagePress = imagePress;
        this.title = title;
        this.fragmentClass = fragmentClass;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public int getImageNormal() {
        return imageNormal;
    }

    public int getImagePress() {
        return imagePress;
    }

    public int getTitle() {
        return title;
    }

    public String getTitleString() {
        if (title == 0) {
            return "";
        }
        if (TextUtils.isEmpty(titleString)) {
            titleString = context.getString(title);
        }
        return titleString;
    }

    public abstract View getView();

    //切换tab的方法
    public abstract void setChecked(boolean isChecked); /*{
        //切换选中的图片
        if (imageView != null) {
            if (isChecked) {
                imageView.setImageResource(imagePress);
            } else {
                imageView.setImageResource(imageNormal);
            }
        }
        //切换字体颜色
        if (textView != null && title != 0) {
            if (isChecked) {
                textView.setTextColor(context.getResources().
                        getColor(R.color.main_botton_text_select));
            } else {
                textView.setTextColor(context.getResources().
                        getColor(R.color.main_bottom_text_normal));
            }
        }
        //切换background
        if (null != view) {
            if (isChecked) {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                view.setBackgroundColor(Color.parseColor("#99c8fc"));
            }
        }
    }*/
}
