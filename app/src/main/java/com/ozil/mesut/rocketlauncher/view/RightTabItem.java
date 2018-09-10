package com.ozil.mesut.rocketlauncher.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;

/**
 * @author kui.liu
 * @time 2018/8/30 15:49
 */
public class RightTabItem extends TabItem {

    public RightTabItem(Context context, int imageNormal, int imagePress,
                       int title, Class<? extends Fragment> fragmentClass) {
        super(context, imageNormal, imagePress, title, fragmentClass);
    }

    @Override
    public View getView() {
        if (this.view == null) {
            this.view = LayoutInflater.from(context).inflate(R.layout.left_tab_indicator, null);
            this.imageView = this.view.findViewById(R.id.tab_iv_image);
            this.textView = this.view.findViewById(R.id.tab_tv_text);
            if (title == 0) {
                this.textView.setVisibility(View.GONE);
            } else {
                this.textView.setVisibility(View.VISIBLE);
                this.textView.setText(getTitleString());
            }
            this.imageView.setImageResource(imageNormal);
        }
        return this.view;
    }

    @Override
    public void setChecked(boolean isChecked) {
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
                        getColor(R.color.main_botton_text_selected));
            } else {
                textView.setTextColor(context.getResources().
                        getColor(R.color.main_bottom_text_normal));
            }
        }
        //切换background
        if (null != view) {
            if (isChecked) {
                view.setBackground(context.getResources().getDrawable(R.drawable.right_tab_selected_shape));
            } else {
                view.setBackground(context.getResources().getDrawable(R.drawable.right_tab_normal_shape));
            }
        }
    }
}
