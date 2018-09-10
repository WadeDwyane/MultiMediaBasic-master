package com.ozil.mesut.rocketlauncher.ui.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.bean.ProcessChooseSection;

import java.util.List;

/**
 * @author kui.liu
 * @time 2018/8/7 14:03
 */
public class ProcessChooseAdapter extends BaseSectionQuickAdapter<ProcessChooseSection, BaseViewHolder> {

    public ProcessChooseAdapter(int layoutResId, int sectionHeadResId, List<ProcessChooseSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ProcessChooseSection item) {
        //过程、政策、程序、组织知识库(组织数据库)
        //以往项目的档案：范围基准、进度基准、成本基准

        helper.setText(R.id.tv_process_choose_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcessChooseSection item) {
        helper.setText(R.id.tv_question_item, item.t.value);
    }
}


