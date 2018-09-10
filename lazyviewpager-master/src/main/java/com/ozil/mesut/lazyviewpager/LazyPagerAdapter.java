package com.ozil.mesut.lazyviewpager;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


/**
 * @author kui.liu
 * @time 2018/9/3 17:13
 */
public abstract class LazyPagerAdapter<T> extends PagerAdapter {

    protected SparseArray<T> mLazyItems = new SparseArray<T>();
    private T mCurrentItem;

    //添加Item到容器中
    protected abstract T addLazyItem(ViewGroup container, int position);

    //获取到Item
    protected abstract T getItem(ViewGroup container, int position);

    //Item是否是懒加载的
    public boolean isLazyItem(int position) {
        return mLazyItems.get(position) != null;
    }

    //获取到当前的Item
    public T getCurrentItem() {
        return mCurrentItem;
    }

    //设置当前的Item
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentItem = addLazyItem(container, position);
    }
}
