/**
 * 
 * @author jeason, 2014-5-24 上午11:25:09
 */
package com.zcdh.mobile.app.activities.personal;

import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobLanguageDTO;
import com.zcdh.mobile.app.activities.personal.widget.LanguageDialog;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.app.views.MyLanguageItem;
import com.zcdh.mobile.app.views.MyLanguageItem_;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-5-24 上午11:25:09
 * 语言技能编辑
 */
@EActivity(R.layout.language_edit)
public class LanguageEditActivity extends BaseActivity implements RequestListener {
	// @ViewById(R.id.btn_add)
	// RelativeLayout btn_add;

	@ViewById(R.id.ll_languages)
	LinearLayout container;

	@ViewById(R.id.skill_count)
	TextView skill_count;

	public static final int REQUEST_CODE_LANGUAGE = 0x09;
	/**
	 * 技能弹出框
	 */
	LanguageDialog dialog;

	IRpcJobUservice uService;

	List<JobLanguageDTO> myLanguages;

	Page<JobLanguageDTO> mAllLanguages;

	LayoutInflater inflater;

	int current_page = 1;

	private final int PAGE_SIZE = 20;

	LoadingIndicator loading;

	public String kREQ_ID_FINDALLLANGUAGE;

	public String kREQ_ID_FINDJOBLANGUAGEDTOBYUSERID;

	public String kREQ_ID_ADDJOBLANGUAGE;

	public String kREQ_ID_REMOVEJOBLANGUAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loading = new LoadingIndicator(this);
		uService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "语言掌握");
	}

	@AfterViews
	void afterView() {
		inflater = LayoutInflater.from(this);
		myLanguages = new ArrayList<JobLanguageDTO>();

		dialog = new LanguageDialog(this);
		loading.show();
		getMyLanguages();

		setCount(0);
		setResult();
	}

	private void setCount(int count) {
		skill_count.setText(String.format("语言掌握:%d/30", count));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.activities.BaseActivity#onOptionsItemSelected
	 * (com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			setResult();
		}
		return super.onOptionsItemSelected(item);
	}

	@Background
	void getMyLanguages() {
		uService.findJobLanguageDTOByUserId(getUserId()).identify(kREQ_ID_FINDJOBLANGUAGEDTOBYUSERID = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void getAllLanguages() {
		uService.findAllLanguage(getUserId(), current_page, PAGE_SIZE).identify(kREQ_ID_FINDALLLANGUAGE = RequestChannel.getChannelUniqueID(), this);
	}

	@Click(R.id.btn_add)
	void onAdd() {
		dialog.show();
	}

	public void onReInitialLanguage() {
		current_page = 1;
		loading.show();
		getAllLanguages();
	}

	@Override
	public void onRequestStart(String reqId) {
		loading.show();
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (result != null) {
			if (reqId.equals(kREQ_ID_FINDALLLANGUAGE)) {
				mAllLanguages = (Page<JobLanguageDTO>) result;
				reInitialLanguages(mAllLanguages, myLanguages);
			}

			if (reqId.equals(kREQ_ID_FINDJOBLANGUAGEDTOBYUSERID)) {
				myLanguages = (List<JobLanguageDTO>) result;
				resetMyLanguage();
				reInitialLanguages(mAllLanguages, myLanguages);

			}

			if (reqId.equals(kREQ_ID_ADDJOBLANGUAGE)) {
				setResult();
			}

			if (reqId.equals(kREQ_ID_REMOVEJOBLANGUAGE)) {
				setResult();
			}
		}

	}

	void reInitialLanguages(Page<JobLanguageDTO> allTags, List<JobLanguageDTO> selectedTags) {
		if (allTags != null) dialog.reInitialLanguage(allTags.getElements(), selectedTags);
	}

	private void add_skill() {
		dialog.show();
	}

	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		loading.dismiss();
	}

	public void onSelect(JobLanguageDTO item) {
		if (myLanguages.size() >= 30) {
			return;
		}
		if (item != null) {
			loading.show();
			doAddLanguage(item);
		}

		myLanguages.add(item);
		setCount(myLanguages.size());

		// 重设我的语言列表
		resetMyLanguage();

		reInitialLanguages(mAllLanguages, myLanguages);
	}

	/**
	 * 
	 * @author jeason, 2014-5-24 下午4:10:17
	 */
	private void resetMyLanguage() {
		container.removeAllViews();
		for (JobLanguageDTO item : myLanguages) {
			MyLanguageItem v = MyLanguageItem_.build(this);
			v.initData(this, item, edit_mode);
			container.addView(v);
		}
		setCount(myLanguages.size());
	}

	/**
	 * @param item
	 * @author jeason, 2014-5-23 下午12:11:06
	 */
	@Background
	void doAddLanguage(JobLanguageDTO item) {
		item.setUserId(getUserId());
		uService.addJobLanguage(item).identify(kREQ_ID_ADDJOBLANGUAGE = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void doRemoveSkill(JobLanguageDTO item) {
		item.setUserId(getUserId());
		uService.removeJobLanguage(item).identify(kREQ_ID_REMOVEJOBLANGUAGE = RequestChannel.getChannelUniqueID(), this);
	}

	public void onRemoveSelect(JobLanguageDTO item) {
		for (JobLanguageDTO dto : myLanguages) {
			if (dto.getLanguageCode().equals(item.getLanguageCode())) {
				doRemoveSkill(dto);
				myLanguages.remove(dto);
				break;
			}
		}
		resetMyLanguage();
		reInitialLanguages(mAllLanguages, myLanguages);
	}

	void setResult() {
		int count = 0;
		if (myLanguages != null) {
			count = myLanguages.size();
		}
		Intent data = new Intent();
		data.putExtra("language_count", count);
		setResult(RESULT_OK, data);
	}

	boolean edit_mode = false;

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.action_edit, menu);
		MenuItem menu_item = menu.findItem(R.id.action_edit);
		if (edit_mode) {
			menu_item.setTitle(R.string.done);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@OptionsItem(R.id.action_edit)
	void onEdit() {
		edit_mode = !edit_mode;
		supportInvalidateOptionsMenu();
		resetMyLanguage();
	}
}