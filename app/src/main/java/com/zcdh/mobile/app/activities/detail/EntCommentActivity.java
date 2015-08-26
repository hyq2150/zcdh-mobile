/**
 * 
 * @author jeason, 2014-6-5 下午5:18:12
 */
package com.zcdh.mobile.app.activities.detail;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.BasicSysCommentTagDTO;
import com.zcdh.mobile.api.model.CommentTagDTO;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-6-5 下午5:18:12
 * 公司评论Activity
 */
@EActivity(R.layout.activity_self_evaluation)
public class EntCommentActivity extends BaseActivity implements EditableDialogListener, RequestListener, TagsDialogListener {

	public static final String TAG = EntCommentActivity.class.getSimpleName();

	private LayoutInflater inflater;

	/**
	 * 联网services
	 * */
	private IRpcJobEnterpriseService entService;

	@ViewById(R.id.container_selected)
	TagsContainer expressionTags;

	@ViewById(R.id.eval_count)
	TextView expression_count;


	@ViewById(R.id.et_comment)
	EditText et_comment;

	@ViewById(R.id.anonymousToggleBtn)
	ToggleButton anonymous;

	private ExpressionsTagAdapter adapter;

	private BasicImpressionTagsAdapter basicAdapter;

	private long entId;

	private String kREQ_ID_addEntComment;

	private String kREQ_ID_findBasicSysEntCommentTagDTO;

	private ProcessDialog loading;

	//公司印象选择dialog
	private TagsDialog tagDialog;

	private Page<BasicSysCommentTagDTO> basicCommentTagPage;

	private int current_page;

	private final int PAGE_SIZE = 10;

	private String comment;

	// 用于判断标签是否被编辑过
	private boolean tagEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "公司评价");
		inflater = LayoutInflater.from(this);
		entId = getIntent().getLongExtra("entId", 0l);
	}

	private void setCount() {
		expression_count.setText(String.format("公司印象:%d/2", adapter.getCount()));
	}

	@AfterViews
	void bindView() {
		// anonymous.setText("匿名", "实名");
		loading = new ProcessDialog(this);
		
		tagDialog = new TagsDialog(this, true,this);
		tagDialog.setSearchInvisible();
		
		adapter = new ExpressionsTagAdapter();
		basicAdapter = new BasicImpressionTagsAdapter(this);
		
		tagDialog.setAdapter(basicAdapter);
		tagDialog.setTitle("公司印象");
		
		anonymous.setVisibility(View.VISIBLE);
		
		et_comment.setHint(getString(R.string.company_comment_hint));
		
		expressionTags.setAdapter(this, adapter);
		setCount();
	}

	private class ExpressionsTagAdapter extends PredicateAdapter {

		private List<CommentTagDTO> expressions;

		public ExpressionsTagAdapter() {
			this.expressions = new ArrayList<CommentTagDTO>();
		//	setAddBtn();
		}

		public void updateItems(List<CommentTagDTO> items) {
			expressions.clear();
			expressions.addAll(items);
			notifyDataSetChanged();
			//setAddBtn();
		}

		public void removeItem(BasicSysCommentTagDTO item) {
			for (CommentTagDTO exp : getItems()) {
				if (exp != null) {
					if (item.getTagCode().equals(exp.getTagCode())) {
						expressions.remove(exp);
						notifyDataSetChanged();
						break;
					}
				}
			}
			
			//setAddBtn();
		}

		public List<CommentTagDTO> getItems() {
			return expressions;
		}

		public void addItem(CommentTagDTO item) {
			expressions.add(item);
			//setAddBtn();
			notifyDataSetChanged();
			if(expressions.size()>0){
				expressionTags.setVisibility(View.VISIBLE);
			}else{
				expressionTags.setVisibility(View.GONE);
			}
			tagEdit = true;
		}

		@Override
		public View getView(final int position, ViewGroup parentView) {
			final CommentTagDTO expression = expressions.get(position);
			/*if (expression == null) {
				return getAddButton();
			}*/
			View view = inflater.inflate(R.layout.basic_evaluation_tag, null);
			TextView tag_name = (TextView) view.findViewById(R.id.tv_tag_name);
			tag_name.setText(expression.getTagName());
			tag_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					removeItemAt(position);

				}
			});
			ImageView selectIV = (ImageView) view.findViewById(R.id.iv_indicator);
			selectIV.setVisibility(View.VISIBLE);
			Log.i(TAG, "........." + position);
			return view;
		}

		/**
		 * @param position
		 * @author jeason, 2014-6-6 上午11:09:00
		 */
		protected void removeItemAt(int position) {
			expressions.remove(position);
			//setAddBtn();
			basicAdapter.notifyDataSetChanged();
			setCount();
			notifyDataSetChanged();
			if(getCount()==0)expressionTags.setVisibility(View.GONE);
		}

		@Override
		public int getCount() {
			return getItems().size();
		}

		@Override
		public void setLayout(ViewGroup parentView) {
			int unit = 40; // UnitTransfer.dip2px(getActivity(), 40);
			int height = expressions.size() * unit;
			parentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.zcdh.mobile.framework.adapters.PredicateAdapter#getMarginOffset()
		 */
		@Override
		public int getMarginOffset() {
			int margin = 20;
			return margin * 2;
		}

	}

	/**
	 * 
	 * @author jeason, 2014-6-5 下午7:33:53
	 */
	protected void add_skill() {
		tagDialog.show();
	}

	@Click(R.id.addCommentTags)
	void onCustom() {
		//new EditableDialog(this).initData(this, ADD_EXPRESSION, "添加印象").show();
		add_skill();
	}
	

	@Override
	public void onEditableConfirm(int key, String content) {
		if (adapter.getCount() - 1 >= 2) {
			Toast.makeText(this, "最多可选两个标签", Toast.LENGTH_SHORT).show();
			return;
		}
		CommentTagDTO tag = new CommentTagDTO();
		tag.setTagName(content);
		tag.setTagCode("");
		adapter.addItem(tag);
		setCount();
	}

	@Click(R.id.btn_save)
	void onSubmit() {
		if (!RegisterUtil.isRegisterUser(getApplicationContext())) {
			RegisterUtil.intercepterNotRegisterUser(this, "请登录后进行评价");
		} else {
			sendComment();
		}
	}

	void sendComment() {
		if (!check_content()) {
			return;
		}
		loading.show();
		doSendComment();
	}

	@Background
	void doSendComment() {

		List<CommentTagDTO> tags = adapter.getItems();
		tags.remove(null);
		int anonymous_status = anonymous.isChecked() ? 1 : 0;
		entService.addEntComment(getUserId(), entId, anonymous_status, adapter.getItems(), comment).identify(kREQ_ID_addEntComment = RequestChannel.getChannelUniqueID(), this);
		tags.add(null);
	}

	/**
	 * @return
	 * @author jeason, 2014-6-9 上午11:55:55
	 */
	private boolean check_content() {
		comment = et_comment.getText().toString();
		// position = et_position.getText().toString();

		// if(StringUtils.isBlank(position)){
		// Toast.makeText(this, "请填写职位", Toast.LENGTH_SHORT).show();
		// return false;
		// }

		if (StringUtils.isBlank(comment)) {
			Toast.makeText(this, "请填写评论", Toast.LENGTH_SHORT).show();
			return false;
		}

		/*if (!(comment.length() >255)) {
			Toast.makeText(this, "评论不能少于10或超过100个字", Toast.LENGTH_SHORT).show();
			return false;
		}*/
		return true;
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_addEntComment)) {
			Toast.makeText(this, R.string.submit_successfully, Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			finish();
		}

		if (reqId.equals(kREQ_ID_findBasicSysEntCommentTagDTO)) {
			if (result != null) {
				basicCommentTagPage = (Page<BasicSysCommentTagDTO>) result;
				basicAdapter.updateItems(basicCommentTagPage.getElements());
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	public void onInitialTags() {
		onShift();
	}

	@Background
	void findTags() {
		entService.findBasicSysEntCommentTagDTO(current_page, PAGE_SIZE).identify(kREQ_ID_findBasicSysEntCommentTagDTO = RequestChannel.getChannelUniqueID(), this);
	}

	public void onDialogDismiss() {

	}

	public void onSearchWordChanged(String keyword) {

	}

	public void onSearch(String keyword) {

	}

	public void onSelect(BasicSysCommentTagDTO item) {
		if (adapter.getCount()>= 2) {
			Toast.makeText(this, "最多可选两个标签", Toast.LENGTH_SHORT).show();
			return;
		}
		tagEdit = true;
		CommentTagDTO tag = new CommentTagDTO();
		tag.setTagName(item.getTagName());
		tag.setTagCode(item.getTagCode());
		adapter.addItem(tag);
		basicAdapter.notifyDataSetChanged();
		setCount();

	}

	public void onRemoveSelect(BasicSysCommentTagDTO item) {
		tagEdit = true;

		adapter.removeItem(item);
		basicAdapter.notifyDataSetChanged();
		setCount();

	}

	public boolean isSelected(BasicSysCommentTagDTO item) {
		for (CommentTagDTO dto : adapter.getItems()) {
			if (dto != null) if (dto.getTagCode().equals(item.getTagCode())) {
				return true;
			}
		}

		return false;
	}

	public void onShift() {
		if (basicCommentTagPage == null) {
			current_page = 1;
		} else if (!basicCommentTagPage.getOnMaxPage()) {
			current_page++;
		} else {
			current_page = 1;
		}
		loading.show();
		findTags();

	}

	@Click(R.id.voice_btn)
	void onVoice() {
		YuYinInputView speechWindow = new YuYinInputView(this, new YuyinInputListner() {

			@Override
			public void onComplete(String content) {
				if (!StringUtils.isBlank(content)) {
					et_comment.setText(et_comment.getText().toString() + content);
				}
			}
		});
		speechWindow.setOutsideTouchable(true);
		speechWindow.showAtParent(findViewById(R.id.et_comment));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (isShowTip()) {
				showTip();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	void showTip() {
		new AlertDialog.Builder(this).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setTitle(R.string.friendly_hint).setNegativeButton(R.string.cancel, null).setMessage(R.string.edit_mode_exit).create().show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (isShowTip()) {
				showTip();
				return false;
			} else {
				finish();
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean isShowTip() {
		comment = et_comment.getText().toString();
		if (!StringUtils.isBlank(comment)) {
			return true;
		}
		return tagEdit;
	}

}
