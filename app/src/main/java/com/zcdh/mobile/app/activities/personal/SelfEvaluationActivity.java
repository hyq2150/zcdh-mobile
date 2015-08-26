/**
 * 
 * @author jeason, 2014-5-16 上午11:10:41
 */
package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.BasicSysCommentTagDTO;
import com.zcdh.mobile.api.model.CommentTagDTO;
import com.zcdh.mobile.api.model.UserCommentDTO;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-5-16 上午11:10:41
 */
@EActivity(R.layout.activity_self_evaluation)
public class SelfEvaluationActivity extends BaseActivity implements
		RequestListener, TagsDialogListener, EditableDialogListener {

	public static final String TAG = SelfEvaluationActivity.class.getSimpleName();
	@ViewById(R.id.container_selected)
	TagsContainer container_selected;
	@ViewById(R.id.et_comment)
	EditText et_comment;
	@ViewById(R.id.eval_count)
	TextView eval_count;

	private IRpcJobUservice userService;
	private int current_page = 1;
	private final int page_size = 10;
	
	ProcessDialog loading;
	
	private String kREQ_ID_FINDBASICSYSCOMMENTTAGDTO;

	private String kREQ_ID_FINDUSERCOMMENT;

	Page<BasicSysCommentTagDTO> basic_tags;

	UserCommentDTO userCommentDto;

	private final int ADD_CUSTOMIZE = 0x01;
	private String kREQ_ID_ADDUSERCOMMENT;

	TagsDialog tagsDialog;

	LayoutInflater inflater;

	TagsAdapter adapter;

	TagsAdapter basicAdapter;
	private boolean is_edited;
	private boolean hasUpdatedTags;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"自我评价");
		userService = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
	
		inflater = LayoutInflater.from(this);
	}

	/**
	 * 
	 * @author jeason, 2014-5-16 下午2:59:25
	 */
	@Background
	void getBasicTags() {
		userService
				.findBasicSysCommentTagDTO(current_page, page_size)
				.identify(
						kREQ_ID_FINDBASICSYSCOMMENTTAGDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void getMyTags() {
		userService.findUserComment(getUserId()).identify(
				kREQ_ID_FINDUSERCOMMENT = RequestChannel.getChannelUniqueID(),
				this);
	}

	private void setCount(int count) {

		eval_count.setText(String.format("评价标签:%d/5", count));
	}

	@AfterViews
	void bindView() {
		loading = new ProcessDialog(this);
		tagsDialog = new TagsDialog(this, true, this);
		tagsDialog.setSearchInvisible();
		adapter = new TagsAdapter(true);
		basicAdapter = new TagsAdapter(false);
		tagsDialog.setAdapter(basicAdapter);
		tagsDialog.setTitle("评价标签");
		container_selected.setAdapter(this, adapter);
		setCount(adapter.getCount() - 1);
		getMyTags();
		getBasicTags();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			confirmSave();
		}
		return false;
	}
	
	@Override
	public void onBackPressed(){
		confirmSave();
	}
	
	void confirmSave(){
		String comment = "";
		if (userCommentDto == null) {
			finish();
			return;
		}
		if (userCommentDto.getComment() != null) {
			comment = userCommentDto.getComment();
		}
		if (!et_comment.getText().toString().equals(comment) 
				|| hasUpdatedTags) {
			new AlertDialog.Builder(this)
					.setCancelable(true)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							finish();
						}
					}).setTitle(R.string.friendly_hint)
					.setNegativeButton(R.string.cancel, null)
					.setMessage(R.string.edit_mode_exit).create().show();
			
		} else {
			finish();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang
	 * .String)
	 */
	@Override
	public void onRequestStart(String reqId) {
		loading.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (result != null) {
			if (reqId.equals(kREQ_ID_FINDBASICSYSCOMMENTTAGDTO)) {
				basic_tags = (Page<BasicSysCommentTagDTO>) result;
				basicAdapter.updateItems(basic_tags.getElements());
				/*if(basicAdapter.getCount()>0){
					container_selected.setVisibility(View.VISIBLE);
				}else{
					container_selected.setVisibility(View.GONEs);
				}*/
			}
			if (reqId.equals(kREQ_ID_FINDUSERCOMMENT)) {

				userCommentDto = (UserCommentDTO) result;

				if (!StringUtils.isBlank(userCommentDto.getComment()))
					et_comment.setText(userCommentDto.getComment());

				adapter.updateItems(userCommentDto.getTagList());
				basicAdapter.notifyDataSetChanged();
				setCount(userCommentDto.getTagList().size());
				if (userCommentDto.getTagList().size() > 0
						|| !TextUtils.isEmpty(userCommentDto.getComment())) {
					is_edited = true;
					supportInvalidateOptionsMenu();
				}
			}

			if (reqId.equals(kREQ_ID_ADDUSERCOMMENT)) {
				Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
				finish();
				// TO NEXT PAGE
			}
		}
		ResumeActivity.FLAG_REFRESH = 1;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String comment = "";
			if (userCommentDto == null)
				return false;
			if (userCommentDto.getComment() != null) {
				comment = userCommentDto.getComment();
			}
			if (!et_comment.getText().toString().equals(comment)) {
				new AlertDialog.Builder(this)
						.setCancelable(true)
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).setTitle(R.string.friendly_hint)
						.setNegativeButton(R.string.cancel, null)
						.setMessage(R.string.edit_mode_exit).create().show();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.
	 * lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang
	 * .String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		loading.dismiss();

	}

	public void onSelectTag(String tag_name, String tag_code) {
		// Toast.makeText(this, "selected", Toast.LENGTH_SHORT).show();
		if (userCommentDto.getTagList().size() >= 5) {
			Toast.makeText(this, "最多只有五个标签", Toast.LENGTH_SHORT).show();
			return;
		}
		CommentTagDTO temp_tag = new CommentTagDTO();
		temp_tag.setTagCode(tag_code);
		temp_tag.setTagName(tag_name);
		userCommentDto.getTagList().add(temp_tag);

		// stuffBasicTags();
	}

	public void onRemoveTag(String tag_name, String tag_code) {
		// Toast.makeText(this, "removed", Toast.LENGTH_SHORT).show();
	}

	public void removeSelected(String tag_name, String tag_code) {
		List<CommentTagDTO> tags = userCommentDto.getTagList();
		for (CommentTagDTO commentTagDTO : tags) {
			if (StringUtils.isBlank(tag_code)) {
				if (commentTagDTO.getTagName().equals(tag_name)) {
					tags.remove(commentTagDTO);
					break;
				}
			} else {
				if (commentTagDTO.getTagCode().equals(tag_code)) {
					tags.remove(commentTagDTO);
					break;
				}
			}
		}
	}

	public void onShift() {
		if (basic_tags == null) {
			current_page = 1;
		} else if (!basic_tags.getOnMaxPage()) {
			current_page++;
		} else {
			// Toast.makeText(this, "no more page and back to first page",
			// Toast.LENGTH_SHORT).show();
			current_page = 1;
		}
		getBasicTags();
	}
	
	@Click(R.id.addCommentTags)
	void onAddTag(){
		show_dialog();
	}

	@Click(R.id.customize_tag)
	void onAddCustomTag() {
		new EditableDialog(this).initData(this, ADD_CUSTOMIZE, "自定义标签").show();
	}

	@Override
	public void onEditableConfirm(int key, String content) {
		if (StringUtils.isBlank(content)) {
			return;
		}
		if (userCommentDto.getTagList().size() >= 5) {
			Toast.makeText(this, "最多只有五个标签", Toast.LENGTH_SHORT).show();
			return;
		}
		CommentTagDTO tag = new CommentTagDTO();
		tag.setTagName(content);
		tag.setTagCode("");
		userCommentDto.getTagList().add(tag);
		adapter.addItem(tag);
		basicAdapter.notifyDataSetChanged();
	}

	@Click(R.id.btn_save)
	void onSave() {

		String comment = et_comment.getText().toString();
		if (comment.length() < 10) {

			Toast.makeText(this, "评价不能少于10个字", Toast.LENGTH_SHORT).show();
			return;
		}
		// if (!StringUtils.isBlank(comment)) {
		userCommentDto.setComment(comment);
		// }
		loading.show();
		doSave();

	}

	@Background
	void doSave() {
		userService.addUserComment(getUserId(), userCommentDto.getTagList(),
				userCommentDto.getComment()).identify(
				kREQ_ID_ADDUSERCOMMENT = RequestChannel.getChannelUniqueID(),
				this);
	}

	private class TagsAdapter extends PredicateAdapter {

		private List expressions;
		private boolean mWithAddBtn = false;

		public TagsAdapter(boolean withAddBtn) {
			mWithAddBtn = withAddBtn;
			this.expressions = new ArrayList<Object>();
			//setAddBtn();
		}

		public void updateItems(List items) {
			expressions.clear();
			expressions.addAll(items);
			notifyDataSetChanged();
			//setAddBtn();
			Log.i(TAG, expressions.size() + ""
					);
			if(expressions.size()>0){
				container_selected.setVisibility(View.VISIBLE);
			}else{
				container_selected.setVisibility(View.GONE);
			}
			
		}

		public List<Object> getItems() {
			return expressions;
		}

/*		public void setAddBtn() {
			if (!mWithAddBtn) {
				notifyDataSetChanged();
				return;
			}
			expressions.remove(null);
			expressions.add(null);
			notifyDataSetChanged();
		}

		private View getAddButton() {
			View add_btn = inflater.inflate(R.layout.add_button, null);
			add_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					show_dialog();
				}

			});
			return add_btn;
		}
*/
		public void addItem(Object item) {
			expressions.remove(null);
			expressions.add(item);
			//setAddBtn();
			notifyDataSetChanged();
			if(expressions.size()>0){
				container_selected.setVisibility(View.VISIBLE);
			}else{
				container_selected.setVisibility(View.GONE);
			}
			hasUpdatedTags = true;
		}

		@Override
		public View getView(final int position, ViewGroup parentView) {
			View view = null;
			final Object expression = expressions.get(position);
			/*if (expression == null) {
				return getAddButton();
			}*/
			if (expression instanceof CommentTagDTO) {
				CommentTagDTO tag = (CommentTagDTO) expression;

				view = inflater.inflate(R.layout.basic_evaluation_tag, null);
				TextView tag_name = (TextView) view
						.findViewById(R.id.tv_tag_name);
				tag_name.setText(tag.getTagName());
				view.findViewById(R.id.tag).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (edit_mode) {
									removeItemAt(position);
									if (basicAdapter != null) {
										basicAdapter.notifyDataSetChanged();
									}
								}
							}
						});
				ImageView selectIV = (ImageView) view
						.findViewById(R.id.iv_indicator);
				if (edit_mode) {
					selectIV.setVisibility(View.VISIBLE);
				} else {
					selectIV.setVisibility(View.GONE);
				}

			} else {
				BasicSysCommentTagDTO tag = (BasicSysCommentTagDTO) expression;
				view = inflater.inflate(R.layout.basic_evaluation_tag, null);
				TextView tag_name = (TextView) view
						.findViewById(R.id.tv_tag_name);
				ImageView selectIV = (ImageView) view
						.findViewById(R.id.iv_selected);
				tag_name.setText(tag.getTagName());
				view.findViewById(R.id.tag).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!isSelected(getItem(position))) {
									onSelect(getItem(position));
								} else {
									onRemoveSelect(getItem(position));
								}
							}

						});

				if (isSelected(getItem(position))) {
					selectIV.setVisibility(View.VISIBLE);
					selectIV.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							onRemoveSelect(getItem(position));
						}
					});
				}
			}
			return view;
		}

		/**
		 * @param position
		 * @return
		 * @author jeason, 2014-7-7 下午3:33:44
		 */
		protected Object getItem(int position) {
			// TODO Auto-generated method stub
			return expressions.get(position);
		}

		/**
		 * @param position
		 * @author jeason, 2014-6-6 上午11:09:00
		 */
		protected void removeItemAt(int position) {
			CommentTagDTO dto = (CommentTagDTO) expressions.get(position);
			removeSelected(dto.getTagName(), dto.getTagCode());

			expressions.remove(position);
			//setAddBtn();
			notifyDataSetChanged();
			setCount(getCount());
			hasUpdatedTags = true;
		}

		@Override
		public int getCount() {
			return getItems().size();
		}

		@Override
		public void setLayout(ViewGroup parentView) {
			int unit = 40; // UnitTransfer.dip2px(getActivity(), 40);
			int height = expressions.size() * unit;
			parentView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, height));
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
			return margin * 2 + 20 + 10;
		}

	}

	private void show_dialog() {
		tagsDialog.show();
	}

	private void onSelect(Object item) {
		if (adapter.getCount() - 1 >= 4) {
			Toast.makeText(this, "最多只有五个标签", Toast.LENGTH_SHORT).show();
			return;
		}
		BasicSysCommentTagDTO basicItem = (BasicSysCommentTagDTO) item;
		CommentTagDTO tag = new CommentTagDTO();
		tag.setTagName(basicItem.getTagName());
		tag.setTagCode(basicItem.getTagCode());
		userCommentDto.getTagList().add(tag);
		adapter.addItem(tag);
		basicAdapter.notifyDataSetChanged();
		setCount(adapter.getCount());
		
	}

	private void onRemoveSelect(Object item) {
		BasicSysCommentTagDTO basicItem = (BasicSysCommentTagDTO) item;
		int i = 0;
		for (Object objSelected : adapter.getItems()) {
			CommentTagDTO myTag = (CommentTagDTO) objSelected;
			if (myTag != null) {
				if (basicItem.getTagCode().equals(myTag.getTagCode())) {
					adapter.removeItemAt(i);
					break;
				}
			}
			i++;
		}
		basicAdapter.notifyDataSetChanged();
		setCount(adapter.getCount() - 1); 
	}

	private boolean isSelected(Object tag) {
		BasicSysCommentTagDTO basicTag = (BasicSysCommentTagDTO) tag;
		for (Object dto : adapter.getItems()) {
			CommentTagDTO mytag = (CommentTagDTO) dto;
			if (mytag != null)
				if (mytag.getTagCode().equals(basicTag.getTagCode())) {
					return true;
				}
		}

		return false;
	}

	boolean edit_mode = false;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if (is_edited) {
			getMenuInflater().inflate(R.menu.action_edit, menu);
			MenuItem menu_item = menu.findItem(R.id.action_edit);
			if (edit_mode) {
				menu_item.setTitle(R.string.done);
			}
		}
		return true;
	}

	@OptionsItem(R.id.action_edit)
	void onEdit() {
		edit_mode = !edit_mode;
		supportInvalidateOptionsMenu();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		if (basicAdapter != null) {
			basicAdapter.notifyDataSetChanged();
		}
	}

	@Click(R.id.voice_btn)
	void onVoice() {
		YuYinInputView speechWindow = new YuYinInputView(this,
				new YuyinInputListner() {

					@Override
					public void onComplete(String content) {
						if (!StringUtils.isBlank(content)) {

							et_comment.setText(et_comment.getText().toString()
									+ content);
						}
					}
				});
		speechWindow.setOutsideTouchable(true);
		speechWindow.showAtParent(findViewById(R.id.et_comment));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener
	 * #onInitialTags()
	 */
	@Override
	public void onInitialTags() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener
	 * #onDialogDismiss()
	 */
	@Override
	public void onDialogDismiss() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener
	 * #onSearchWordChanged(java.lang.String)
	 */
	@Override
	public void onSearchWordChanged(String content) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener
	 * #onSearch(java.lang.String)
	 */
	@Override
	public void onSearch(String keyword) {
		// TODO Auto-generated method stub

	}
}
