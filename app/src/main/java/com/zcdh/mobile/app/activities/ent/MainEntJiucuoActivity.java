package com.zcdh.mobile.app.activities.ent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.LeaveMessageDTO;
import com.zcdh.mobile.api.model.LeaveMsgDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 纠错
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_jiucuo)
@OptionsMenu(R.menu.action_tijiao)
public class MainEntJiucuoActivity extends BaseActivity implements RequestListener{
	
	String kREQ_ID_addLeaveMessage;

	IRpcJobEnterpriseService enterpriseService;

	@ViewById(R.id.msgEditText)
	EditText msgEditText;

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
	
	/**
	 * 企业id
	 */
	@Extra
	long entId;

	
	private String msg;

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(),
				getString(R.string.activity_title_jiucuo));
		enterpriseService = RemoteServiceManager.
				getRemoteService(IRpcJobEnterpriseService.class);
	}
	
	@OptionsItem(R.id.action_tijiao)
	void onCommit(){
		String msg = msgEditText.getText().toString();
		if(!StringUtils.isBlank(msg)){
			if(msg.length()<10){
				Toast.makeText(this, "输入内容不小于10个字", Toast.LENGTH_SHORT).show();
				return;
			}
			LeaveMsgDTO leaveMessages = new LeaveMsgDTO();
			leaveMessages.setContent(msg);
			leaveMessages.setEntId(entId);
			leaveMessages.setIsUserSubmit(1);
			leaveMessages.setUserId(ZcdhApplication.getInstance().getZcdh_uid());
			leaveMessages.setType("003");
			enterpriseService.addLeaveMessage(leaveMessages)
			.identify(kREQ_ID_addLeaveMessage=RequestChannel.getChannelUniqueID(), this);
		}
	}
	
	@Click(R.id.yuyinBtn)
	void onYuyingBtn() {

		YuYinInputView speechWindow = new YuYinInputView(this, new YuyinInputListner() {
			
			@Override
			public void onComplete(String content) {
				if(!StringUtils.isBlank(content)){
					msgEditText.setText(content);
				}
			}
		});
		speechWindow.setOutsideTouchable(true);
		speechWindow.showAtParent(findViewById(R.id.body));
	}
	@TextChange(R.id.msgEditText)
	void onTextChangesOnHelloTextView(CharSequence text, TextView hello,
			int before, int start, int count) {
		if (!StringUtils.isBlank(text.toString()) && text.length() <= 100) {
			havingLettingTxt.setText("还可以输入 " + (100 - text.length()) + "字");
			msg = text.toString();
		}
		if (text.toString().length() > 100) {
			msgEditText.setText(msg);
		}
		if (StringUtils.isBlank(text.toString())) {
			havingLettingTxt.setText("描述 10-100字");
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_ID_addLeaveMessage)){
			if(result!=null){
				int success = (Integer) result;
				if(success==0){
					Toast.makeText(this, "谢谢您的意见！您的支持使我们不断进步!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		
	}
	
}
