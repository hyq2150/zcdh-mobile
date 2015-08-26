package com.zcdh.mobile.app.activities.settings;

import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobFeedBackListDTO;
import com.zcdh.mobile.api.model.JobUserPortraitDTO;
import com.zcdh.mobile.app.views.HeadByGender;
import com.zcdh.mobile.app.views.HeadByGender_;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_feedback)
public class FeedBackActivity extends BaseActivity implements RequestListener {

	String kREQ_ID_findUserFeedBackDTOByUserId;
	String KREQ_ID_FINDUSERPORTRAIT;
	String kREQ_ID_addUserFeedBack;

	IRpcJobUservice jobUservice;

	JobUserPortraitDTO portrait;

	/**
	 * 反馈内容输入框
	 */
	@ViewById(R.id.feedBackEditText)
	EditText feedBackEditText;

	/**
	 * 发送反馈按钮
	 */
	@ViewById(R.id.sendBtn)
	Button sendFeedBackBtn;

	/**
	 * 意见反馈列表
	 */
	@ViewById(R.id.feedBackListView)
	ListView feedBackListView;
	FeedBackAdapter feedBackAdapter;

	/**
	 * 反馈列表
	 */
	List<JobFeedBackListDTO> feedBackList = new ArrayList<JobFeedBackListDTO>();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	JobFeedBackListDTO currentSendFeedBack;

	@AfterViews
	void bindViews() {

		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.setting_feedback));
		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		feedBackAdapter = new FeedBackAdapter();
		feedBackListView.setAdapter(feedBackAdapter);
		if(getUserId()>0){
			loadFeedBack();
		}
	}

	@Background
	void loadFeedBack() {
		jobUservice.findUserPortrait(getUserId()).identify(KREQ_ID_FINDUSERPORTRAIT = RequestChannel.getChannelUniqueID(), this);
		jobUservice.findUserFeedBackDTOByUserId(getUserId(), 1, 1000).identify(kREQ_ID_findUserFeedBackDTOByUserId = RequestChannel.getChannelUniqueID(), this);
	}


	@Background
	void doSendFeedBack(String content) {
		currentSendFeedBack = new JobFeedBackListDTO();
		currentSendFeedBack.setContent(content);
		//currentSendFeedBack.setCreateTime(new Date());
		currentSendFeedBack.setType(0);
		jobUservice.addUserFeedBack(getUserId(), content).identify(kREQ_ID_addUserFeedBack = RequestChannel.getChannelUniqueID(), this);
	}

	@OptionsItem(android.R.id.home) 
	void onBack() {
		finish();
	}

	@Click(R.id.sendBtn)
	void onSendFeedBack() {
		String content = feedBackEditText.getText().toString();
		if (!StringUtils.isBlank(content)) {
			doSendFeedBack(content);
			/*if(content.length()>=10){
			}else{
				Toast.makeText(this, "请输入大于10个字内容", Toast.LENGTH_SHORT).show();
			}*/
		} else {
			Toast.makeText(this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
		}
		
	}

	class FeedBackAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return feedBackList.size();
		}

		@Override
		public Object getItem(int position) {
			return feedBackList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.feedback_item, null);
				h.tallContainer1 = (RelativeLayout) convertView.findViewById(R.id.tallContainer1);
				h.tallContainer2 = (RelativeLayout) convertView.findViewById(R.id.tallContainer2);

				h.tallText1 = (TextView) convertView.findViewById(R.id.tall1);
				h.tallText2 = (TextView) convertView.findViewById(R.id.tall2);

				h.taller1 = (TextView) convertView.findViewById(R.id.taller1);
				h.taller2 = (TextView) convertView.findViewById(R.id.taller2);
//
				h.head = (HeadByGender_) convertView.findViewById(R.id.Head);
				h.head1 = (HeadByGender_) convertView.findViewById(R.id.Head1);

				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			JobFeedBackListDTO feedBackItem = null;
			if(getUserId()==-1){
				feedBackItem = feedBackList.get(position);
			}else{
				feedBackItem = feedBackList.get(feedBackList.size() - position - 1);
			}
			
			if (feedBackItem.getType() == 0) { // 用户反馈

				h.tallContainer1.setVisibility(View.VISIBLE);
				h.tallContainer2.setVisibility(View.GONE);

				h.tallText1.setText(feedBackItem.getContent());
				
				h.taller1.setText(sdf.format(feedBackItem.getCreateTime()) + " 我");
				if (portrait != null) h.head.initHeadWithUserPortrait(portrait);

			}
			if (feedBackItem.getType() == 1) { // 系统回复
				h.tallContainer1.setVisibility(View.GONE);
				h.tallContainer2.setVisibility(View.VISIBLE);
				//Toast.makeText(getApplicationContext(), sdf.format(feedBackItem.getCreateTime())., duration)
				h.tallText2.setText(feedBackItem.getContent());
				h.taller2.setText(" 客服 " + sdf.format(feedBackItem.getCreateTime()));

			}

			return convertView;
		}

		class ViewHolder {
			HeadByGender head1;
			HeadByGender head;
			RelativeLayout tallContainer1;
			RelativeLayout tallContainer2;

			TextView taller1;
			TextView taller2;

			TextView tallText1;
			TextView tallText2;
		}

	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 提交反馈
		if (reqId.equals(kREQ_ID_addUserFeedBack)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					if(getUserId()==-1){
						currentSendFeedBack.setCreateTime(new Date());
						feedBackList.add(currentSendFeedBack);
						feedBackAdapter.notifyDataSetChanged();
						
					}else if(getUserId()>0){
						loadFeedBack();
					}	
					feedBackEditText.setText("");
				} else {
					Toast.makeText(this, "服务异常, 提交反馈失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
		// 反馈列表
		if (reqId.equals(kREQ_ID_findUserFeedBackDTOByUserId)) {
			if (result != null) {
				Page<JobFeedBackListDTO> page = (Page<JobFeedBackListDTO>) result;
				if (page.getElements() != null) {
					feedBackList = page.getElements();
				}
				feedBackAdapter.notifyDataSetChanged();
			}
		}

		if (reqId.equals(KREQ_ID_FINDUSERPORTRAIT)) {
			if (result != null) {
				portrait = (JobUserPortraitDTO) result;
				if (feedBackAdapter != null) {
					feedBackAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		if (reqId.equals(kREQ_ID_addUserFeedBack)) sendFeedBackBtn.setEnabled(true);
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

}
