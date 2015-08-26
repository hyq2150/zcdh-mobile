/**
 * 
 * @author jeason, 2014-5-19 下午4:42:42
 */
package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.InformationDTO;
import com.zcdh.mobile.api.model.JobApplyDTO;
import com.zcdh.mobile.api.model.JobApplyListDTO;
import com.zcdh.mobile.api.model.JobEntAccessListDTO;
import com.zcdh.mobile.api.model.JobInterviewListDTO;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;

/**
 * @author jeason, 2014-5-19 下午4:42:42
 */
public class InfoAdapter extends BaseAdapter {

	private Context mContext;
	private List mItems;

	public InfoAdapter(Context context) {
		mContext = context;
		mItems = new ArrayList();
	}

	public void updateAllItems(List items) {
		mItems.clear();
		mItems.addAll(items);
		notifyDataSetChanged();
	}

	public void addToBottom(List items) {
		mItems.addAll(items);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InfoViewHolder holder;
		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(R.layout.info_item, null);

			holder = new InfoViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.flag = (TextView) convertView.findViewById(R.id.flag);
			holder.note = (TextView) convertView.findViewById(R.id.tv_note);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);

		} else {
			holder = (InfoViewHolder) convertView.getTag();
		}

		// 申请职位填充数据
		if (mItems.get(position) instanceof JobApplyListDTO) {
			holder.initWithApplication((JobApplyListDTO) mItems.get(position));
		}
		
		if (mItems.get(position) instanceof JobApplyDTO) {
			holder.initWithApplication((JobApplyDTO) mItems.get(position));
		}

		// 面试邀请填充数据
		if (mItems.get(position) instanceof JobInterviewListDTO) {
			holder.initWithJobInterviewListDTO((JobInterviewListDTO) mItems.get(position));
		}

		// 谁访问我填充数据
		if (mItems.get(position) instanceof JobEntAccessListDTO) {
			holder.initWithJobEntAccessListDTO((JobEntAccessListDTO) mItems.get(position));
		}

		// 企业回信
		if (mItems.get(position) instanceof InformationDTO) {
			holder.initWithInformationDTO((InformationDTO) mItems.get(position));
		}
		return convertView;
	}

	public class InfoViewHolder {
		private  final String TAG = InfoAdapter.class.getSimpleName();
		
		public ImageView icon;
		public TextView title;
		public TextView note;
		public TextView flag;
		public TextView tv_time;

		public void initWithApplication(JobApplyListDTO apply) {
			icon.setImageResource(R.drawable.logo05);

			title.setText(apply.getEntName());
			note.setText(apply.getPostName());
			// 0 表示hr已读， 1 表示hr未读
			switch (apply.getStatus()) {
			case 0:
				flag.setText("hr已读");
				break;
			case 1:
				flag.setText("hr未读");
				break;
			default:
				break;
			}

			tv_time.setText(DateUtils.getDateByFormatYMDHM(apply.getApplyTime()));
		}
		
		public void initWithApplication(JobApplyDTO apply) {
			icon.setImageResource(R.drawable.xiaoxilogo03);

			title.setText(apply.getEntName());
			note.setText(apply.getPostName());
			// 0 表示hr已读， 1 表示hr未读
//			switch (apply.getStatus()) {
//			case 0:
//				flag.setText("hr已读");
//				break;
//			case 1:
//				flag.setText("hr未读");
//				break;
//			default:
//				break;
//			}
			
			flag.setText(apply.getApplyStatus());

			tv_time.setText(DateUtils.getDateByFormatYMDHM(apply.getApplyDate()));
		}
		/**
		 * @param informationDTO
		 * @author jeason, 2014-7-4 上午11:44:03 企业回信
		 */
		public void initWithInformationDTO(InformationDTO informationDTO) {
			Log.i(TAG, "isRead:" + informationDTO.getIsRead());
			if (informationDTO.getIsRead() == 0) {
				icon.setImageResource(R.drawable.cpymsgclose);
				title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.redpoint, 0);
				title.setCompoundDrawablePadding(-5);
			} else if(informationDTO.getIsRead() == 1){
				icon.setImageResource(R.drawable.cpymsgopen);
			}
			if (!StringUtils.isBlank(informationDTO.getTitle())) title.setText(informationDTO.getTitle());

			String entName = (informationDTO.getEntName() == null) ? "未知企业" : informationDTO.getEntName();
			if(!StringUtils.isBlank(entName))note.setText(entName);
			note.setText(entName);
			//if (!StringUtils.isBlank(informationDTO.getDesc())) note.setText(Html.fromHtml(informationDTO.getDesc() + "<br/>" + entName));
			flag.setVisibility(View.GONE);
			tv_time.setText(DateUtils.getDateByFormatYMDHM(informationDTO.getPushTime()));
		}

		public void initWithJobEntAccessListDTO(JobEntAccessListDTO access) {
			icon.setImageResource(R.drawable.logo01);

			title.setText(access.getEntName());

			note.setText(access.getContact());
			flag.setVisibility(View.GONE);
			tv_time.setText(DateUtils.getDateByFormatYMDHM(access.getAccessTime()));
		}

		public void initWithJobInterviewListDTO(JobInterviewListDTO interview) {
			icon.setImageResource(R.drawable.logo05);
			title.setText(interview.getEntName());
			note.setText(interview.getContent());
			flag.setVisibility(View.GONE);
			tv_time.setText(DateUtils.getDateByFormatYMDHM(interview.getSendTime()));
		}

	}

}
