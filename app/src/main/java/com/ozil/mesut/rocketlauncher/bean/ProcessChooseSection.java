package com.ozil.mesut.rocketlauncher.bean;

import com.chad.library.adapter.base.entity.SectionEntity;


/**
 * @author kui.liu
 * @time 2018/8/7 14:05
 */
public class ProcessChooseSection extends SectionEntity<ProcessChooseBean> {
    public ProcessChooseSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ProcessChooseSection(ProcessChooseBean processChooseBean) {
        super(processChooseBean);
    }
}
