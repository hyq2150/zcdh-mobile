package com.zcdh.mobile.app.activities.ent;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserPortraitDTO;
import com.zcdh.mobile.api.model.LeaveMessageDTO;
import com.zcdh.mobile.api.model.LeaveMsgDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.HeadByGender;
import com.zcdh.mobile.app.views.HeadByGender_;
import com.zcdh.mobile.app.views.iflytek.MyVoiceRecognizerListner;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;

/**
 * 留言
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_liuyan)
public class MainEntLiuyanActivity extends BaseActivity implements
		RequestListener, MyVoiceRecognizerListner {

	String kREQ_ID_addLeaveMessage;
	String kREQ_ID_findEntLeaveMessageDTO;
	String kREQ_ID_findPostLeaveMessageDTO;

	IRpcJobEnterpriseService enterpriseService;
	IRpcJobUservice uService;

	@ViewById(R.id.llpost_item)
	LinearLayout headLl;

	/**
	 * 职位名称
	 */
	@ViewById(R.id.postNameText)
	TextView postNameTxt;

	/**
	 * 反馈内容输入框
	 */
	@ViewById(R.id.msgEditText)
	EditText msgEditText;

	/**
	 * 意见反馈列表
	 */
	@ViewById(R.id.liuyanListView)
	ListView liuyanListView;
	LeavMsgAdapter msgAdapter;

	/**
	 * 剩余字数
	 */
	@ViewById(R.id.havingLetterTxt)
	TextView havingLettingTxt;

	/**
	 * 语音输入
	 */
	@ViewById(R.id.yuyinBtn)
	Button yuyingBtn;

	YuYinInputView speechWindow;

	List<LeaveMessageDTO> leaveMessagesList = new ArrayList<LeaveMessageDTO>();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	LeaveMsgDTO leaveMessages;

	JobUserPortraitDTO portrait;

	String K_REQ_ID_JOBUSERPORTRAITDTO;

	/**
	 * 企业id
	 */
	@Extra
	long entId;

	/**
	 * 留言类型 （留言或岗位咨询） 001：咨询或意见，002：求职咨询，003：纠错意见
	 */
	@Extra
	String type;
	/**
	 * 职位
	 */
	@Extra
	Long postId;

	/**
	 * 职位名称
	 */
	@Extra
	String postName;

	private String msg;

	ProcessDialog processDialog;

	public void onDestory() {
		speechWindow.dismiss();
		processDialog.dismiss();
		super.onDestroy();
	}

	@AfterViews
	void bindViews() {

		enterpriseService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);
		uService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		String title = "";
		if ("002".equals(type)) {
			title = getString(R.string.activity_title_zixun);
			msgEditText.setHint("有什么岗位疑问");
		}
		if ("001".equals(type)) {
			title = getString(R.string.activity_title_liuyan);
			msgEditText.setHint("说些公司意见");
		}
		if ("003".equals(type)) {
			title = getString(R.string.activity_title_jiucuo);
			msgEditText.setHint("纠错意见");
		}
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), title);

		if (Constants.kTYPE_ZIXUN.equals(type)) {// 求职
			headLl.setVisibility(View.VISIBLE);
			postNameTxt.setText("期望职位：" + postName);
		} else {
			headLl.setVisibility(View.GONE);
		}

		msgAdapter = new LeavMsgAdapter();
		liuyanListView.setAdapter(msgAdapter);

		processDialog = new ProcessDialog(MainEntLiuyanActivity.this);

		loadData();
	}

	@Background
	void loadData() {
		uService.findUserPortrait(getUserId())
				.identify(
						K_REQ_ID_JOBUSERPORTRAITDTO = RequestChannel
								.getChannelUniqueID(),
						this);

		if (Constants.kTYPE_LIUYAN.equals(type) || Constants.kTYPE_JIUCUO.equals(type)) {
			enterpriseService
					.findEntLeaveMessageDTO(
							ZcdhApplication.getInstance().getZcdh_uid(), entId,
							type)
					.identify(
							kREQ_ID_findEntLeaveMessageDTO = RequestChannel
									.getChannelUniqueID(),
							this);
		}
		if (Constants.kTYPE_ZIXUN.equals(type)) {
			enterpriseService
					.findPostLeaveMessageDTO(
							ZcdhApplication.getInstance().getZcdh_uid(), postId)
					.identify(
							kREQ_ID_findPostLeaveMessageDTO = RequestChannel
									.getChannelUniqueID(),
							this);
		}

	}

	/**
	 * 点击期望岗位
	 */
	@Click(R.id.llpost_item)
	void onPostClick() {
		DetailsFrameActivity_.intent(this).postId(postId).switchable(false)
				.entId(entId).start();
	}

	@Click(R.id.submitBtn)
	void onCommit() {
		msg = msgEditText.getText().toString();
		if (TextUtils.isEmpty(msg)) {
			if ("002".equals(type)) {
				Toast.makeText(this, "请输入咨询内容", Toast.LENGTH_SHORT).show();
			}
			if ("001".equals(type)) {
				Toast.makeText(this, "请输入意见内容", Toast.LENGTH_SHORT).show();
			}
			if ("003".equals(type)) {
				Toast.makeText(this, "请输入纠错意见", Toast.LENGTH_SHORT).show();
			}
		} else {
			leaveMessages = new LeaveMsgDTO();
			leaveMessages.setContent(msg);
			leaveMessages.setEntId(entId);
			leaveMessages.setIsUserSubmit(1);
			leaveMessages
					.setUserId(ZcdhApplication.getInstance().getZcdh_uid());
			leaveMessages.setPostId(postId);
			leaveMessages.setType(type);

			processDialog.show();
			msgEditText.clearFocus();
			doAddLeaveMessage();
		}
	}

	@Background
	void doAddLeaveMessage() {
		enterpriseService.addLeaveMessage(leaveMessages).identify(
				kREQ_ID_addLeaveMessage = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Click(R.id.yuyinBtn)
	void onYuyingBtn() {

		if (speechWindow == null) {
			speechWindow = new YuYinInputView(this, new YuyinInputListner() {

				@Override
				public void onComplete(String content) {
					if (!StringUtils.isBlank(content)) {
						msgEditText.setText(msgEditText.getText().toString()
								+ content);
					}
				}
			});
			speechWindow.setOutsideTouchable(true);
		}
		speechWindow.showAtParent(findViewById(R.id.body));
	}

	@TextChange(R.id.msgEditText)
	void onTextChangesOnHelloTextView(CharSequence text, TextView hello,
			int before, int start, int count) {
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntLeaveMessageDTO)
				|| reqId.equals(kREQ_ID_findPostLeaveMessageDTO)) {
			if (result != null) {
				leaveMessagesList = (List<LeaveMessageDTO>) result;
				msgAdapter.notifyDataSetChanged();

			}
		}

		if (reqId.equals(kREQ_ID_addLeaveMessage)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					loadData();
					msgEditText.setText("");
					msg = "";
				}
			}
		}

		if (reqId.equals(K_REQ_ID_JOBUSERPORTRAITDTO)) {
			if (result != null) {
				portrait = (JobUserPortraitDTO) result;
				if (msgAdapter != null) {
					msgAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		if (reqId.equals(kREQ_ID_addLeaveMessage)) {
			processDialog.dismiss();
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	class LeavMsgAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return leaveMessagesList.size();
		}

		@Override
		public Object getItem(int position) {
			return leaveMessagesList.get(position);
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
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.feedback_item, null);
				h.tallContainer1 = (RelativeLayout) convertView
						.findViewById(R.id.tallContainer1);
				h.tallContainer2 = (RelativeLayout) convertView
						.findViewById(R.id.tallContainer2);

				h.tallText1 = (TextView) convertView.findViewById(R.id.tall1);
				h.tallText2 = (TextView) convertView.findViewById(R.id.tall2);

				h.taller1 = (TextView) convertView.findViewById(R.id.taller1);
				h.taller2 = (TextView) convertView.findViewById(R.id.taller2);

				h.head = (HeadByGender_) convertView.findViewById(R.id.Head);
				h.head1 = (HeadByGender_) convertView.findViewById(R.id.Head1);

				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}

			LeaveMessageDTO item = leaveMessagesList.get(position);
			if (item.getIsUserSubmit() == 1) { // 用户反馈

				h.tallContainer1.setVisibility(View.VISIBLE);
				h.tallContainer2.setVisibility(View.GONE);

				h.tallText1.setText(item.getContent());
				h.taller1.setText(sdf.format(item.getLeaveDate()) + " 我");

				if (portrait != null)
					h.head.initHeadWithUserPortrait(portrait);
			}
			if (item.getIsUserSubmit() == 0) { // 系统回复
				h.tallContainer1.setVisibility(View.GONE);
				h.tallContainer2.setVisibility(View.VISIBLE);

				h.tallText2.setText(item.getContent());
				h.taller2.setText(" 公司 " + sdf.format(item.getLeaveDate()));
				if (item.getEntLogo() != null
						&& !TextUtils.isEmpty(item.getEntLogo().getMedium())) {
					h.head1.displayImg(item.getEntLogo().getMedium());
				}
			}

			return convertView;
		}

		class ViewHolder {
			RelativeLayout tallContainer1;
			RelativeLayout tallContainer2;

			TextView taller1;
			TextView taller2;

			TextView tallText1;
			TextView tallText2;

			HeadByGender head;
			HeadByGender head1;
		}

	}

	@Override
	public void onVoiceSearchOK() {

	}

	@Override
	public void doSearch(String keyWord) {

	}

	@Override
	public void onError(String error) {

	}

}
