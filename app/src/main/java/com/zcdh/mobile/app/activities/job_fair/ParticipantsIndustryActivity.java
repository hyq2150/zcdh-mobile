package com.zcdh.mobile.app.activities.job_fair;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.job_fair.CategoryDropDownFilter.onFilterListener;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 行业类别
 * 
 * @author yangjiannan 1) 支持多选和单选， 传递参数
 */
@EActivity(R.layout.participants_industry)
public class ParticipantsIndustryActivity extends BaseActivity {

	private static final String TAG = ParticipantsIndustryActivity.class
			.getSimpleName();

	private Intent data = new Intent();

	@ViewById(R.id.search_online_layout)
	LinearLayout search_online_layout;
	@ViewById(R.id.search_offline_layout)
	LinearLayout search_offline_layout;
	@ViewById(R.id.search_online)
	Button search_online;
	@ViewById(R.id.search_offline)
	Button search_offline;
	/**
	 * 行业列表
	 */
	@ViewById(R.id.industryListView)
	CategoryDropDownFilter industryListView;

	@Extra
	long fairID;

	@Extra
	int type;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.title_industry));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getSupportMenuInflater();
//		inflater.inflate(R.menu.action_ok, menu);
		return true;
	}

	/**
	 * 更具View 的id 绑定到变量，初始化一些UI
	 */
	@AfterViews
	void bindViews() {
		search_online.setSelected(true);
		search_offline.setSelected(true);
		industryListView.setOnFilterListener(new onFilterListener() {

			@Override
			public void onFilter(String code, String name) {
				// TODO Auto-generated method stub
				data.putExtra(Constants.kDATA_CODE, code);
				data.putExtra(Constants.kDATA_NAME, name);
				
				if (search_offline.isSelected()) {
					data.putExtra(Constants.kDATA_OFFLINE, 1);
				} else {
					data.putExtra(Constants.kDATA_OFFLINE, 0);
				}

				if (search_online.isSelected()) {
					data.putExtra(Constants.kDATA_ONLINE, 1);
				} else {
					data.putExtra(Constants.kDATA_ONLINE, 0);
				}
				setResult(RESULT_OK, data);
				finish();
			}
		});
		search_offline_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (search_offline.isSelected()) {
					search_offline.setSelected(false);
				} else {
					search_offline.setSelected(true);
				}
			}
		});
		search_online_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (search_online.isSelected()) {
					search_online.setSelected(false);
				} else {
					search_online.setSelected(true);
				}
			}
		});
		industryListView.loadData(fairID, type);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
//		menu.clear();
//		getSupportMenuInflater().inflate(R.menu.sure_select, menu);
		return super.onPrepareOptionsMenu(menu);
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
////		if (search_offline.isSelected()) {
////			data.putExtra(Constants.kDATA_OFFLINE, 1);
////		} else {
////			data.putExtra(Constants.kDATA_OFFLINE, 0);
////		}
////
////		if (search_online.isSelected()) {
////			data.putExtra(Constants.kDATA_ONLINE, 1);
////		} else {
////			data.putExtra(Constants.kDATA_ONLINE, 0);
////		}
////		setResult(RESULT_OK, data);
////		finish();
//		return true;
//	}

}
