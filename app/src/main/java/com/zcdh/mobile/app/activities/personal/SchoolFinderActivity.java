/**
 * 
 * @author jeason, 2014-5-13 下午3:28:25
 */
package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-5-13 下午3:28:25
 * 学校选择
 */
@EActivity(R.layout.school_finder)
public class SchoolFinderActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnItemClickListener {
	private static final String TAG = SchoolFinderActivity.class.getSimpleName();

	private FinalDb dbTool;

	@ViewById(R.id.search_bar)
	EditText search_bar;
	@ViewById(R.id.listview)
	PullToRefreshListView lvMajor;
	SchoolsAdapter adapter;
	List<DbModel> dbms_cache;
	int current_page;
	boolean has_next = false;
	View empty_view;
	LayoutInflater inflater;

	EditText et_school;
	Button btn_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "学校选择");
		dbTool = DbUtil.create(this);
		dbms_cache = new ArrayList<DbModel>();
		inflater = LayoutInflater.from(this);
		empty_view = inflater.inflate(R.layout.school_empty_view, null);
		et_school = (EditText) empty_view.findViewById(R.id.et_school);
		btn_add = (Button) empty_view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_school();
			}

		});
	}

	private void add_school() {
		if (StringUtils.isBlank(et_school.getText().toString())) {
			Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
		}
		Intent intent = new Intent();
		intent.putExtra("school_name", et_school.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 
	 * @author jeason, 2014-5-13 下午3:50:18
	 */
	@AfterViews
	void initView() {
		search_bar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				onSearch(s.toString(), 0);
			}
		});
		lvMajor.setMode(Mode.PULL_FROM_END);
		adapter = new SchoolsAdapter();
		lvMajor.setAdapter(adapter);
		lvMajor.setOnRefreshListener(this);
		lvMajor.setOnItemClickListener(this);
		lvMajor.setEmptyView(empty_view);
		onSearch("", 0);

	}

	/**
	 * 
	 * @author jeason, 2014-5-13 下午3:51:23
	 */
	protected void onSearch(String content, int target_page) {
		current_page = target_page;
		if (target_page == 0) dbms_cache.clear();
		List<DbModel> temp_dbms = dbTool.findDbModelListBySQL(getsql(content, target_page));
		if (!temp_dbms.isEmpty()) {
			has_next = true;
			dbms_cache.addAll(temp_dbms);
		} else {
			has_next = false;
		}
		adapter.notifyDataSetChanged();
	}

	private String getsql(String keyword, int target_page) {
		String sql = "select * from zcdh_school where school_name like '%" + keyword + "%' order by school_code asc limit (" + target_page + "*10),10;";
		Log.i(TAG, sql);
		return sql;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (has_next) add_more();
		setOnRefresh();
	}

	/**
	 * 
	 * @author jeason, 2014-5-13 下午6:03:50
	 */
	void add_more() {
		onSearch(search_bar.getText().toString(), current_page + 1);
	}

	@UiThread
	void setOnRefresh() {
		lvMajor.onRefreshComplete();
	}

	private class SchoolsAdapter extends BaseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount() 
		 */
		@Override
		public int getCount() {
			return dbms_cache.size();
		}

		@Override
		public DbModel getItem(int position) {
			return dbms_cache.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.simple_listview_item_accessory, null);
			TextView school = (TextView) convertView.findViewById(R.id.itemNameText);
			school.setText(getItem(position).getString("school_name"));
			return convertView;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		DbModel item = dbms_cache.get(arg2 - 1);

		Intent intent = new Intent();
		intent.putExtra("school_code", item.getString("school_code"));
		intent.putExtra("school_name", item.getString("school_name"));
		setResult(RESULT_OK, intent);
		finish();
	}
}
