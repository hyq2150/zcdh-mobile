/**
 * 
 * @author jeason, 2014-4-11 下午3:24:03
 */
package com.zcdh.mobile.app.views;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.BasicSysCommentTagDTO;
import com.zcdh.mobile.api.model.CommentTagDTO;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-4-11 下午3:24:03 标签容器
 */
public class TagsContainer extends LinearLayout {

	private OnTagClickListener mListener;
	private PredicateAdapter mAdapter;
	private DataSetObserver mDataSetObserver;
	private Activity mActivity;

	/**
	 * @author jeason, 2014-4-11 下午3:24:08
	 * @param context
	 */
	public TagsContainer(Context context) {
		this(context, null);
	}

	public TagsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initData(Activity activity, int margin_offset,
			PredicateAdapter adapter, boolean extenion) {
		this.removeAllViewsInLayout();

		// LayoutInflater inflater = LayoutInflater.from(activity);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 10;
		lp.topMargin = 10;
		Display display = activity.getWindowManager().getDefaultDisplay();
		int maxWidth = display.getWidth() - 10 - margin_offset;

		if (adapter != null && adapter.getCount() > 0) {
			LinearLayout llAlso = new LinearLayout(getContext());
			llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			llAlso.setOrientation(LinearLayout.HORIZONTAL);

			int widthSoFar = 0;
			int j = 0;
			for (int i = 0; i < adapter.getCount(); i++) {
				View v = adapter.getView(i, this);

				v.measure(0, 0);
				widthSoFar += v.getMeasuredWidth() + lp.leftMargin;

				if (widthSoFar >= maxWidth) {

					addView(llAlso);

					llAlso = new LinearLayout(getContext());
					llAlso.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					llAlso.setOrientation(LinearLayout.HORIZONTAL);
					llAlso.addView(v, lp);
					widthSoFar = v.getMeasuredWidth() + lp.leftMargin;

					j++;
					if (!extenion && j >= 2) {
						break;
					}
				} else {
					llAlso.addView(v, lp);
				}
			}

			addView(llAlso);
		}

	}

	public void initData(List<String> collection, OnTagClickListener listener,
			int gravity) {
		this.removeAllViewsInLayout();

		mListener = listener;
		LayoutInflater inflater = LayoutInflater.from(getContext());
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 10;
		lp.topMargin = 10;
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		int maxWidth = display.getWidth() - 10;

		if (collection != null && collection.size() > 0) {
			LinearLayout llAlso = new LinearLayout(getContext());
			llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			llAlso.setOrientation(LinearLayout.HORIZONTAL);
			llAlso.setGravity(gravity);
			int widthSoFar = 0;

			for (String tag : collection) {
				TextView tv_tag = (TextView) inflater.inflate(
						R.layout.tag_item, null);
				tv_tag.setText(tag);
				tv_tag.setTag(tag);
				tv_tag.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mListener != null) {
							mListener.onTagClick(0);
						}
					}
				});

				tv_tag.measure(0, 0);
				widthSoFar += tv_tag.getMeasuredWidth() + lp.leftMargin;

				if (widthSoFar >= maxWidth) {
					addView(llAlso);

					llAlso = new LinearLayout(getContext());
					llAlso.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					llAlso.setOrientation(LinearLayout.HORIZONTAL);
					llAlso.setGravity(gravity);

					llAlso.addView(tv_tag, lp);
					widthSoFar = tv_tag.getMeasuredWidth() + lp.leftMargin;
				} else {
					llAlso.addView(tv_tag, lp);
				}

			}

			addView(llAlso);
		}

	}

	public void initEvaluationData(Activity activity, int margin_offset,
			Object[] collection, boolean[] show_selected, int gravity) {
		this.removeAllViewsInLayout();
		// LayoutInflater inflater = LayoutInflater.from(activity);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 10;
		lp.topMargin = 10;
		Display display = activity.getWindowManager().getDefaultDisplay();
		int maxWidth = display.getWidth() - 10 - margin_offset;

		if (collection != null && collection.length > 0) {
			LinearLayout llAlso = new LinearLayout(getContext());
			llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			llAlso.setOrientation(LinearLayout.HORIZONTAL);
			llAlso.setGravity(gravity);
			int widthSoFar = 0;
			int i = 0;
			for (Object tag : collection) {
				View view_item;
				if (tag instanceof BasicSysCommentTagDTO) {
					BasicSysCommentTagDTO basictag = (BasicSysCommentTagDTO) tag;
					EvaluationTag view_tag = EvaluationTag_.build(activity);
					view_tag.initEvaluationTag(activity, basictag.getTagName(),
							basictag.getTagCode(), true, show_selected[i]);
					view_item = view_tag;
				} else {
					CommentTagDTO mytag = (CommentTagDTO) tag;

					EvaluationTag view_tag = EvaluationTag_.build(activity);
					view_tag.initEvaluationTag(activity, mytag.getTagName(),
							mytag.getTagCode(), false, false);

					view_item = view_tag;

				}

				view_item.measure(0, 0);
				widthSoFar += view_item.getMeasuredWidth() + lp.leftMargin;

				if (widthSoFar >= maxWidth) {
					addView(llAlso);

					llAlso = new LinearLayout(getContext());
					llAlso.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					llAlso.setOrientation(LinearLayout.HORIZONTAL);
					llAlso.setGravity(gravity);

					llAlso.addView(view_item, lp);
					widthSoFar = view_item.getMeasuredWidth() + lp.leftMargin;
				} else {
					llAlso.addView(view_item, lp);
				}
				i++;

			}

			addView(llAlso);
		}

	}

	public void setAdapter(Activity activity, PredicateAdapter adapter) {
		mActivity = activity;

		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		mDataSetObserver = new DataSetObserver() {

			@Override
			public void onChanged() {
				super.onChanged();
				reInitialView();

			}

			@Override
			public void onInvalidated() {
				super.onInvalidated();
			}

		};

		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mDataSetObserver);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @author jeason, 2014-5-21 下午4:17:14
	 */
	protected void reInitialView() {
		initData(mActivity, mAdapter.getMarginOffset(), mAdapter, true);
	}

	public interface OnTagClickListener {
		public void onTagClick(int position);
	}
}
