package com.ozil.mesut.rocketlauncher.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ozil.mesut.rocketlauncher.R;

import java.util.ArrayList;

/**
 * @author kui.liu
 * @time 2018/8/30 16:28
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder>{

    public Context mContext;
    public ArrayList<String> mList;

    public ContentAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ContentAdapter.ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content,
                parent,false);//解决宽度不能铺满
        ContentViewHolder holder = new ContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ContentViewHolder holder, int position) {
        String content = mList.get(position);
        holder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_process_title);
        }
    }
}
