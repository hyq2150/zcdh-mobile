package com.zcdh.mobile.framework.adapters;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * 子控件自动换行适配器
 * 
 * @author chenhanbu
 * 
 */
public abstract class PredicateAdapter {
	private final DataSetObservable mDataSetObservable = new DataSetObservable();

	public boolean hasStableIds() {
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}

	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}

	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}

	public abstract View getView(int position, ViewGroup parentView);

	public abstract int getCount();

	public abstract void setLayout(ViewGroup parentView);

	/**
	 * 重写此方法返回容器的margin值
	 * 
	 * @return
	 * @author jeason, 2014-7-25 下午3:36:08
	 */
	public abstract int getMarginOffset();
}
