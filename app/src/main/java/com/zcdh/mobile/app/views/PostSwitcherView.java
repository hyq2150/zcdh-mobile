/**
 * 
 * @author jeason, 2014-4-18 下午3:25:32
 */
package com.zcdh.mobile.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;
import com.handmark.pulltorefresh.library.internal.LoadingLayout.OnPullGetTextListener;

/**
 * @author jeason, 2014-4-18 下午3:25:32
 * 拉动切换职位ScrollView
 */
public class PostSwitcherView extends PullToRefreshScrollView {

	/**
	 * @author jeason, 2014-4-18 下午3:25:55
	 * @param context
	 */
	public PostSwitcherView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author jeason, 2014-4-18 下午3:55:49
	 */
	public PostSwitcherView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}

	private String getTips(Mode mode, String post_name) {
		switch (mode) {
		case PULL_FROM_START:
			return String.format("<center><font color='#71706c'>%s</font></center><br><center><font color='#f8943a'>%s</font></center>", post_name, "查看上一条");
		case PULL_FROM_END:
			return String.format("<center><font color='#71706c'>%s</font></center><br><center><font color='#f8943a'>%s</font></center>", post_name, "查看下一条");
		default:
			break;
		}
		return "";
	}

	private OnPullGetTextListener mGetTextListener;

	@Override
	protected LoadingLayout createLoadingLayout(Context context, Mode mode, TypedArray attrs) {

		LoadingLayout layout = super.createLoadingLayout(context, mode, attrs);
		layout.setGetTextListener(new OnPullGetTextListener() {

			@Override
			public String getText(Mode mode) {

				return getTips(mode, mGetTextListener.getText(mode));
			}
		});
		return layout;
	}

	public OnPullGetTextListener getmGetTextListener() {
		return mGetTextListener;
	}

	public void setmGetTextListener(OnPullGetTextListener mGetTextListener) {
		this.mGetTextListener = mGetTextListener;
	}
}
