/**
 * 
 * @author jeason, 2014-6-4 下午8:36:55
 */
package com.zcdh.mobile.app.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.CommentDTO;
import com.zcdh.mobile.api.model.CommentTagDTO;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-6-4 下午8:36:55
 * 评论view
 */
public class CommentItemView extends RelativeLayout {

	TagsContainer commentsContainer;
	TextView tv_comment;
	HeadByGender head;

	public CommentItemView(Context context) {
		this(context, null);
	}

	public CommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(getContext(), R.layout.company_comment_item, this);
		commentsContainer = (TagsContainer) findViewById(R.id.commentsContainer);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		head = (HeadByGender_) findViewById(R.id.imghead);
	}

	public void initData(Activity activity, final CommentDTO comment) {
		commentsContainer.setAdapter(activity, new PredicateAdapter() {

			@Override
			public void setLayout(ViewGroup parentView) {
				// TODO Auto-generated method stub

			}

			@Override
			public View getView(int position, ViewGroup parentView) {
				TextView tag_name = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tag_item, null);
				tag_name.setText(getItem(position).getTagName());
				return tag_name;
			}

			@Override
			public int getMarginOffset() {
				return 100 + 5 * 2 + 5 + 15;
			}

			private CommentTagDTO getItem(int position) {
				return comment.getTagList().get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if (comment.getTagList() == null) return 0;
				return comment.getTagList().size();
			}
		});

		if (comment.getIsNickName() == 1) {
			head.initHeadWithGender("匿名", HeadByGender.ANONYMOUS);
		} else {
			if (comment.getHeadPortrait() != null) {

				head.displayImg(comment.getHeadPortrait().getBig());
			} else {

				head.initHeadWithGender(comment.getUserName(), comment.getGender());

			}
		}
		tv_comment.setText(comment.getCommContent());

	}
}
