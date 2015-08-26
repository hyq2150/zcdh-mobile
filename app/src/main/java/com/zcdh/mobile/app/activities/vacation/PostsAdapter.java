/**
 * 
 * @author jeason, 2014-4-24 上午9:48:18
 */
package com.zcdh.mobile.app.activities.vacation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.EntPostSearchDTO;
import com.zcdh.mobile.utils.DateUtils;

/**
 * @author jeason, 2014-4-24 上午9:48:18
 * 职位数据Adapter
 */
public class PostsAdapter extends BaseAdapter {
	private Context mContext;
	private boolean is_vacation = false;

	private class ViewHolder {
		TextView title;
		TextView publish_time;
		TextView content;
		TextView salary;
		TextView location_and_requirement;
		TextView distance;
		FrameLayout img_cover;
	}

	public PostsAdapter(Context context) {
		mContext = context;
	}

	private List<EntPostSearchDTO> mPosts = new ArrayList<EntPostSearchDTO>();

	public void updateItems(List<EntPostSearchDTO> posts) {
		mPosts = posts;
		notifyDataSetChanged();
	}

	public List<EntPostSearchDTO> getItems() {
		return mPosts;
	}

	public void clearItems() {
		mPosts.clear();
		notifyDataSetChanged();
	}

	public void addItemsToTop(List<EntPostSearchDTO> posts) {
		mPosts.addAll(0, posts);
		notifyDataSetChanged();
	}

	public void addItemsToBottom(List<EntPostSearchDTO> posts) {
		mPosts.addAll(posts);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	@Override
	public EntPostSearchDTO getItem(int position) {
		return mPosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.post_item_vacation, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.location_and_requirement = (TextView) convertView.findViewById(R.id.location_and_education);
			holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
			holder.salary = (TextView) convertView.findViewById(R.id.salary);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.img_cover = (FrameLayout) convertView.findViewById(R.id.cover);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// TODO stuff data
		EntPostSearchDTO post = mPosts.get(position);
		holder.location_and_requirement.setText(post.getAreaName() + "/" + post.getDegreeName());
		holder.publish_time.setText(DateUtils.getDateByFormat(post.getPublishDate(), "yyyy-MM-dd"));// .getDateByFormatNUM(post.getPublishDate()));
		holder.salary.setText(post.getSalaryName());
		holder.title.setText(post.getPostName());
		holder.content.setText(mPosts.get(position).getEntName());
		if (mPosts.get(position).getIsFilled() && isIs_vacation()) {
			holder.img_cover.setVisibility(View.VISIBLE);
		} else {
			holder.img_cover.setVisibility(View.GONE);
		}

		return convertView;
	}

	public boolean isIs_vacation() {
		return is_vacation;
	}

	public void setIs_vacation(boolean is_vacation) {
		this.is_vacation = is_vacation;
	}
}