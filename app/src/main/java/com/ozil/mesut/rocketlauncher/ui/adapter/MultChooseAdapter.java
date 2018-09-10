package com.ozil.mesut.rocketlauncher.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ozil.mesut.rocketlauncher.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author kui.liu
 * @time 2018/8/23 16:57
 */
public class MultChooseAdapter extends RecyclerView.Adapter<MultChooseAdapter.ViewHolder> {

    private List<String> mList;
    private Context mContext;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;


    public MultChooseAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rcy_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mList.get(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    map.put(position, true);
                }else {
                    map.remove(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mTextView = (TextView) itemView.findViewById(R.id.textview);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    interface OnCheckBoxListener {
        void onChecked(int position);
        void onCanceled(int position);
    }

    public void setOnCheckBoxListener(OnCheckBoxListener listener) {

    }

}
