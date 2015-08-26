package com.zcdh.mobile.app.activities.nearby;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.views.TagsContainer;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchResultAdapter extends BaseAdapter {

	private static final String TAG = SearchResultAdapter.class.getSimpleName();

	private ArrayList<JobEntPostDTO> mPosts = new ArrayList<JobEntPostDTO>();

	private Activity context;

	public SearchResultAdapter(Activity context) {
		this.context = context;
	}

	public void updateItems(ArrayList<JobEntPostDTO> posts) {
		mPosts = posts;
		notifyDataSetChanged();
	}

	public ArrayList<JobEntPostDTO> getItems() {
		return mPosts;
	}

	public void clearItems() {
		mPosts.clear();
		notifyDataSetChanged();
	}

	public void addItemsToTop(List<JobEntPostDTO> posts) {
		mPosts.addAll(0, posts);
		notifyDataSetChanged();
	}

	public void addItemsToBottom(List<JobEntPostDTO> posts) {
		mPosts.addAll(posts);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.post_item, parent, false);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.view=convertView.findViewById(R.id.line);
			holder.ll_tags_container = (TagsContainer) convertView
					.findViewById(R.id.ll_tags);
		
			holder.location_and_requirement = (TextView) convertView
					.findViewById(R.id.location_and_education_and_matchrate);
			holder.publish_time = (TextView) convertView
					.findViewById(R.id.publish_time);
			holder.workDate=(TextView)convertView.findViewById(R.id.workdate);
			holder.salary = (TextView) convertView.findViewById(R.id.salary);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.renzhengImg = (ImageView) convertView
					.findViewById(R.id.renzhengImg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		JobEntPostDTO post = mPosts.get(position);
		Log.i("match rate:", post.getMatchRate() + "");
		if (Constants.POST_PROPERTY_JIANZHI.equals(post.getPostPropertyCode())
				|| Constants.POST_PROPERTY_JIAQI.equals(post
						.getPostPropertyCode())) {
			holder.location_and_requirement.setText(post.getAreaName()
					+ (post.getMajorName() == null ? "|专业不限" : "|"
							+ post.getMajorName())
					+ (post.getMatchRate() > 0 ? "|匹配度:" + post.getMatchRate()
							+ "%" : ""));
		} else {
			holder.location_and_requirement.setText(post.getAreaName()
					+ (post.getDegree() == null ? "|学历不限" : "|"
							+ post.getDegree())
					+ (post.getMatchRate() > 0 ? "|匹配度:" + post.getMatchRate()
							+ "%" : ""));
		}
		holder.publish_time.setVisibility(View.VISIBLE);
		holder.publish_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(post.getPublishDate()));
//				post.getPublishDate(), "MM-dd"));// .getDateByFormatNUM(post.getPublishDate()));
		holder.salary.setText(post.getSalary());
		holder.title.setText(post.getPostAliases());
		holder.content.setText(post.getEntName());
		holder.workDate.setText("工作经验:"+post.getWorkExperience());
		Log.i(TAG, post.getEntName() + " > " + post.getIsLegalize());
		if (post.getIsLegalize() != null && post.getIsLegalize() == 1) {
			holder.renzhengImg.setVisibility(View.VISIBLE);
		} else {
			holder.renzhengImg.setVisibility(View.GONE);
		}
		double distance = post.getDistance();
		if (distance == 0) { // 如果为0 不显示
			holder.distance.setVisibility(View.GONE);
			holder.view.setVisibility(View.GONE);
		} else if (distance < 1000) { // 如果小于1千米，以米为单位
			String d = distance + "";
			holder.distance.setText(d.subSequence(0, d.indexOf(".")) + "米");
		} else if (distance > 1000) {
			double d = distance / 1000;
			Log.e("destance:", distance + "");
			BigDecimal b = new BigDecimal(d);
			double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			holder.distance.setText(String.valueOf(f1 + "千米"));
		}

		holder.ll_tags_container.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);
		if (mPosts.get(position).getTagNames() != null) {
			if (mPosts.get(position).getTagNames().size() < 3) {

				holder.ll_tags_container.initData(
						mPosts.get(position)
								.getTagNames()
								.subList(
										0,
										mPosts.get(position).getTagNames()
												.size()), null, Gravity.LEFT);
			} else {

				holder.ll_tags_container.initData(mPosts.get(position)
						.getTagNames().subList(0, 3), null, Gravity.LEFT);
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView publish_time;
		TextView content;
		TextView workDate;
		View view;
		/**
		 * 企业是否认证
		 */
		ImageView renzhengImg;

		TextView salary;
		TextView location_and_requirement;
		TextView distance;
		TagsContainer ll_tags_container;
	}
}
