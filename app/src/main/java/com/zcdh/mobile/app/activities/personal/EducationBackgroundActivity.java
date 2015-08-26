/**
 * 
 * @author jeason, 2014-5-9 上午9:01:36
 */
package com.zcdh.mobile.app.activities.personal;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEducationExperienceDTO;
import com.zcdh.mobile.api.model.JobEducationListDTO;
import com.zcdh.mobile.api.model.JobTrainListDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EducationExperenceItem;
import com.zcdh.mobile.app.views.EducationExperenceItem_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.TranningExperenceItem;
import com.zcdh.mobile.app.views.TranningExperenceItem_;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author jeason, 2014-5-9 上午9:01:36
 * 教育背景Activity
 */
@EActivity(R.layout.edu_background)
public class EducationBackgroundActivity extends BaseActivity implements RequestListener, ExperenceListener
, DataLoadInterface{

	IRpcJobUservice userService;

	public String kREQ_ID_FINDJOBEDUCATIONBYUSERID;
	JobEducationExperienceDTO eduExps;
	@ViewById(R.id.ll_edu_experences_container)
	LinearLayout ll_edu_experences_container;

	@ViewById(R.id.ll_tranning_experences_container)
	LinearLayout ll_tranning_experences_container;
	
	@ViewById(R.id.scrollView)
	PullToRefreshScrollView scrollView;
	
	@ViewById(R.id.emptyTipView)
	EmptyTipView emptyTipView;
	
	@ViewById(R.id.contentView)
	LinearLayout contentView;

	View emptyView;
	
	boolean delete_mode = false;
	private String kREQ_ID_removeEdu;
	private String kREQ_ID_removeTrainId;

	public final int ADD_EDU_REQUEST_CODE = 0x01;

	public final int ADD_TRANNING_REQUEST_CODE = 0x02;

	public final int EDIT_EDU_REQUEST_CODE = 0x03;

	public final int EDIT_TRAINNING_REQUEST_CODE = 0x04;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
	}

	@AfterViews
	void afterViews() {
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "教育经历");
		emptyView = ll_edu_experences_container.getChildAt(0);
		loadData();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (showEdit()) {
			getMenuInflater().inflate(R.menu.action_edit, menu);
			MenuItem menu_item = menu.findItem(R.id.action_edit);
			if (delete_mode) {
				menu_item.setTitle(R.string.done);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * 
	 * @author jeason, 2014-5-9 上午11:52:36
	 */
	@Background
	public void loadData() {
		userService.findJobEducationByUserId(getUserId()).identify(kREQ_ID_FINDJOBEDUCATIONBYUSERID = RequestChannel.getChannelUniqueID(), this);
	}

	@Click(R.id.btn_add_edu_exp)
	void addEduExp() {
		AddEduExpActivity_.intent(this).startForResult(ADD_EDU_REQUEST_CODE);
	}

	@Click(R.id.btn_add_tranning_exp)
	void addTranningExp() {
		AddTranningExpActivity_.intent(this).startForResult(ADD_TRANNING_REQUEST_CODE);
	}

	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDJOBEDUCATIONBYUSERID)) {
			eduExps = (JobEducationExperienceDTO) result;
			stuffViews();
			supportInvalidateOptionsMenu();
		}
		ResumeActivity.FLAG_REFRESH = 1;
		emptyTipView.isEmpty(false);
		contentView.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @author jeason, 2014-5-9 下午12:02:51
	 */
	@UiThread
	void stuffViews() {
		if (eduExps != null) {
			if (eduExps.getEdus() != null) {
				ll_edu_experences_container.removeAllViews();

				for (JobEducationListDTO item : eduExps.getEdus()) {
					EducationExperenceItem eduItem = EducationExperenceItem_
						.build(this);
					eduItem.initData(this, item, delete_mode);
					ll_edu_experences_container.addView(eduItem);
					
				}
			}

			if (eduExps.getTrains() != null) {
				ll_tranning_experences_container.removeAllViews();

				for (JobTrainListDTO item : eduExps.getTrains()) {

					TranningExperenceItem tranningItem = TranningExperenceItem_
						.build(this);
					tranningItem.initData(this, item, delete_mode);

					ll_tranning_experences_container.addView(tranningItem);

				}
			}
		}
	}

	@OptionsItem(R.id.action_edit)
	void setDeleteMode() {
		delete_mode = !delete_mode;
		if (eduExps != null && eduExps.getEdus() != null && !eduExps.getEdus().isEmpty()) {
			for (int i = 0; i < ll_edu_experences_container.getChildCount(); i++) {
				EducationExperenceItem item = (EducationExperenceItem) ll_edu_experences_container.getChildAt(i);
				item.setDeleteMode(delete_mode);
			}
		}
		if (eduExps != null && eduExps.getTrains() != null && !eduExps.getTrains().isEmpty()) {
			for (int i = 0; i < ll_tranning_experences_container.getChildCount(); i++) {
				TranningExperenceItem item = (TranningExperenceItem) ll_tranning_experences_container.getChildAt(i);
				item.setDeleteMode(delete_mode);

			}
		}
		supportInvalidateOptionsMenu();
	}

	@Override
	public void onRequestError(String reqId, Exception error) {
		emptyTipView.showException((ZcdhException)error, this);
	}

	@Override
	public void onRequestFinished(String reqId) {
		if (reqId.equals(kREQ_ID_FINDJOBEDUCATIONBYUSERID)) {

		}
	}

	public void onEduExpItemDelete(long id) {
		int index = -1;
		index = getEduIndexById(id);
		if (index != -1) {
			ll_edu_experences_container.removeViewAt(index);
			try {
				if (eduExps != null && eduExps.getEdus() != null) {
					removeEduInList(id);
				}
			} catch (Exception e) {

			}
			doRemoveEdu(id);
		}
		supportInvalidateOptionsMenu();
	}

	/**
	 * @param id
	 * @author jeason, 2014-7-10 上午10:52:00
	 */
	private void removeEduInList(long id) {
		for (JobEducationListDTO jtld : eduExps.getEdus()) {
			if (jtld.getEduId().equals(id)) {
				eduExps.getEdus().remove(jtld);
			}
		}

		if (eduExps.getEdus().isEmpty()) {
			ll_edu_experences_container.addView(emptyView);
		}
	}

	@Background
	void doRemoveEdu(long id) {
		userService.removeEdu(id).identify(kREQ_ID_removeEdu = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void doRemoveTranning(long id) {
		userService.removeTrainId(id).identify(kREQ_ID_removeTrainId = RequestChannel.getChannelUniqueID(), this);
	}

	public void onTranningExpItemDelete(long id) {
		int index = -1;
		index = getTranningIndexById(id);
		if (index != -1) {
			ll_tranning_experences_container.removeViewAt(index);
			try {
				if (eduExps != null && eduExps.getTrains() != null) {
					removeTrainInList(id);
				}
			} catch (Exception e) {

			}
			doRemoveTranning(id);
		}
		supportInvalidateOptionsMenu();
	}

	private void removeTrainInList(long id) {
		for (JobTrainListDTO jtld : eduExps.getTrains()) {
			if (jtld.getTrainId().equals(id)) {
				eduExps.getTrains().remove(jtld);
			}
		}
		if (eduExps.getTrains().isEmpty()) {
			ll_tranning_experences_container.addView(emptyView);
		}
	}

	private int getEduIndexById(long id) {
		for (int i = 0; i < ll_edu_experences_container.getChildCount(); i++) {
			EducationExperenceItem item = (EducationExperenceItem) ll_edu_experences_container.getChildAt(i);
			if (item.getmItem().getEduId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	private int getTranningIndexById(long id) {
		for (int i = 0; i < ll_tranning_experences_container.getChildCount(); i++) {
			TranningExperenceItem item = (TranningExperenceItem) ll_tranning_experences_container.getChildAt(i);
			JobTrainListDTO litem = item.getmItem();
			if (litem.getTrainId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onEduExpItemClick(long id) {
		ActivityDispatcher.toEditEduExpActivity(this, id, EDIT_EDU_REQUEST_CODE);
	}

	@Override
	public void onTranningExpItemClick(long id) {
		ActivityDispatcher.toEditTrainExpActivity(this, id, EDIT_TRAINNING_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			loadData();
		}
	}

	private boolean showEdit() {
		if (eduExps != null && ((eduExps.getEdus() != null && !eduExps.getEdus().isEmpty()) || (eduExps.getTrains() != null && !eduExps.getTrains().isEmpty()))) {
			return true;
		}
		return false;
	}
}
