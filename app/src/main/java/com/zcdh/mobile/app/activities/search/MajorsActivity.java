package com.zcdh.mobile.app.activities.search;

import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.biz.entities.ZcdhMajor;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 职位选择
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_major_chosen)
public class MajorsActivity extends BaseActivity implements RemoveItemListner {

	public static final String kDATA_CODE = "major_code";
	public static final String kDATA_NAME = "major_name";

	public static final int kREQUEST_MAJOR = 2006;

	public static final String kDATA_MAJOR_BUNLDE = "majors_bunlde";

	FinalDb finalDb;

	/**
	 * 该类别下得职位
	 */
	List<ZcdhMajor> majors;

	/**
	 * 所属职位类别编码
	 */
	@Extra
	String categoryCode;
	ZcdhMajor category;

	/**
	 * 标识是否单选
	 */
	@Extra
	boolean signle = true;

	/**
	 * 已选意向职位职位
	 */
	@Extra
	HashMap<String, ZcdhMajor> selectedMajors = new HashMap<String, ZcdhMajor>();

	/**
	 * 选择显示面板
	 */
	protected MultSelectionPannel multSelectionPannel;

	public void onCreate(Bundle b) {
		super.onCreate(b);
	}

	/**
	 * 基础数据职位列表
	 */
	@ViewById(R.id.majorsListView)
	ListView majorsListView;

	private MajorsAdapter majorsAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// if(!signle){
		// MenuInflater inflater = getSupportMenuInflater();
		// inflater.inflate(R.menu.action_ok, menu);
		// }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 多选确定
		if (!signle) {

			Intent data = new Intent();
			data.putExtra(kDATA_MAJOR_BUNLDE, selectedMajors);
			setResult(RESULT_OK, data);
		}
		finish();
		return true;
	}

	@AfterViews
	void bindViews() {

		if (!signle) {
			multSelectionPannel = new MultSelectionPannel(this, this);
		}
		loadData();
	}

	@Background
	void loadData() {
		if (finalDb == null) {
			finalDb = DbUtil.create(this);
		}
		if (!StringUtils.isBlank(categoryCode)) {
			majors = finalDb.findAllByWhere(ZcdhMajor.class, String.format("parent_code='%s'", categoryCode));
			category = finalDb.findAllByWhere(ZcdhMajor.class, String.format("code='%s'", categoryCode)).get(0);
		}

		showData();
	}

	@UiThread
	void showData() {
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), category.getMajor_name());
		if (majorsAdapter == null) {
			majorsAdapter = new MajorsAdapter();
		}
		majorsListView.setAdapter(majorsAdapter);
		showSelectItems();
	}

	/**
	 * 显示已选择的项
	 */
	void showSelectItems() {
		HashMap<String, String> _items = new HashMap<String, String>();
		for (String key : selectedMajors.keySet()) {
			_items.put(key, selectedMajors.get(key).getMajor_name());
		}
		if (_items.size() > 0) {
			multSelectionPannel.refresh(_items);
		}
	}

	@ItemClick(R.id.majorsListView)
	void onItemClick(int position) {
		ZcdhMajor param = majors.get(position);
		String code = param.getCode();
		String name = param.getMajor_name();

		if (signle) { // 如果是单选， 没选中一个，关闭当前Activity
			Intent data = new Intent();
			data.putExtra(kDATA_CODE, code);
			data.putExtra(kDATA_NAME, name);
			setResult(RESULT_OK, data);
			finish();
		} else { // 如果是多选
			if (selectedMajors.containsKey(code)) {
				selectedMajors.remove(code);
			} else {
				if (MultSelectionPannel.max > selectedMajors.size()) {
					ZcdhMajor objPost = new ZcdhMajor();
					objPost.setCode(code);
					objPost.setMajor_name(name);
					selectedMajors.put(code, objPost);
				} else {
					Toast.makeText(this, "最多选择" + MultSelectionPannel.max + "个职位", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			showSelectItems();
			majorsAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onRemoveItem(Object key) {
		selectedMajors.remove(key);
	}

	class MajorsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return majors.size();
		}

		@Override
		public Object getItem(int p) {
			return majors.get(p);
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View contentView, ViewGroup arg2) {

			Holder h = null;
			if (contentView == null) {
				h = new Holder();
				contentView = LayoutInflater.from(getBaseContext()).inflate(R.layout.simple_listview_item_checker, null);

				h.itemName = (TextView) contentView.findViewById(R.id.itemNameText);
				h.checkerImg = (ImageView) contentView.findViewById(R.id.checkerImg);

				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();

			}

			h.itemName.setText(majors.get(p).getMajor_name());

			String code = majors.get(p).getCode();
			if (selectedMajors.containsKey(code)) {
				h.checkerImg.setVisibility(View.VISIBLE);
			} else {
				h.checkerImg.setVisibility(View.GONE);
			}

			return contentView;
		}

		class Holder {
			TextView itemName = null;
			ImageView checkerImg = null;
		}

	}

}