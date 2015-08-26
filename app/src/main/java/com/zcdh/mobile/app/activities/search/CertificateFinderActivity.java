/**
 * 
 * @author jeason, 2014-5-29 上午9:52:03
 */
package com.zcdh.mobile.app.activities.search;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.biz.entities.ZcdhCertificate;
import com.zcdh.mobile.biz.entities.ZcdhMajor;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 证书
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_cert)
public class CertificateFinderActivity extends BaseActivity implements OnItemClickListener, RemoveItemListner, YuyinInputListner  {

	@ViewById(R.id.categoryListView)
	StickyListHeadersListView categoryListView;

	StickyListHeadersAdapter categoryAdapter;
	public static final int kREQUEST_CERTIFICATE = 0x09;
	public static final String kDATA_CODE = "cer_code";
	public static final String kDATA_NAME = "cer_name";
	// 职位类别分类
	List<ZcdhCertificate> categorys = new ArrayList<ZcdhCertificate>();
	// 职位类别
	List<ZcdhCertificate> categorysCertificate = new ArrayList<ZcdhCertificate>();

	String[] sections;
	int[] sectionIndices;

	FinalDb finalDb;
	
	/* 搜索相关 */
	@ViewById(R.id.searchResultsListView)
	ListView searchResultsListView;

	CertChosenAdapter certChosenAdapter;

	List<ZcdhCertificate> searchResult = new ArrayList<ZcdhCertificate>();

	@ViewById(R.id.resultPannel)
	View resultPannel;

	@ViewById(R.id.resultCountText)
	TextView resultCountText;

	/**
	 * 关键字输入框
	 */
	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;

	@ViewById(R.id.clearBtn)
	ImageButton clearBtn;
	
	YuYinInputView yuyinInputView;

	/**
	 * 保存的关键字
	 */
	String keyword;
	/* end 搜索相关 */
	
	/* end 搜索相关 */

	public void onCreate(Bundle b) {
		super.onCreate(b);
	}

	@AfterViews
	void bindViews() {
		//findViewById(R.id.searchPannel).setVisibility(View.GONE);
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "获取证书");
		categoryListView.setOnItemClickListener(this);
		
		certChosenAdapter = new CertChosenAdapter(CertificateFinderActivity.this, searchResult);
		searchResultsListView.setAdapter(certChosenAdapter);
		loadData();
	}

	@Background
	void loadData() {
		if (finalDb == null) {
			finalDb = DbUtil.create(this);
		}

		categorys = finalDb.findAllByWhere(ZcdhCertificate.class, "parent_code=-1");
		categorysCertificate = finalDb.findAllByWhere(ZcdhCertificate.class, "length(parent_code)==3");

		sections = new String[categorys.size()];
		sectionIndices = new int[categorys.size()];
		int i = 0;
		String last = null;
		for (int j = 0; j < categorysCertificate.size(); j++) {

			ZcdhCertificate p = categorysCertificate.get(j);

			String parent_code = p.getParent_code();

			if (!StringUtils.isBlank(parent_code) && !parent_code.equals(last)) {
				String sql = String.format("select * from zcdh_jobhunte_certificate where cer_code = '%s' order by cer_code asc limit 1", parent_code);
				DbModel parent = finalDb.findDbModelBySQL(sql);
				if (parent == null) {
					continue;
				}

				sections[i] = parent_code;
				sectionIndices[i] = j;

				i++;
			}
			last = parent_code;
		}

		showData();
	}
	
	

	@UiThread
	void showData() {
		if (categoryAdapter == null) {
			categoryAdapter = new CateogryStickAdapter();
			categoryListView.setAdapter(categoryAdapter);
		}
	}
	
	/**
	 * 搜索
	 */
	void search() {
		searchResult = finalDb.findAllByWhere(ZcdhCertificate.class,
				"cer_name like '%" + keyword + "%'");
		if (searchResult != null && searchResult.size() > 0) {
			resultCountText.setText("搜索结果有 " + searchResult.size() + "个");
		} else {
			resultCountText.setText("没有搜索到结果");
		}
		certChosenAdapter.updateItems(searchResult);
		resultPannel.setVisibility(View.VISIBLE);
		categoryListView.setVisibility(View.GONE);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		ZcdhCertificate param = categorysCertificate.get(position);
		String code = param.getCer_code();
		String name = param.getCer_name();

		Intent data = new Intent();
		data.putExtra(kDATA_CODE, code);
		data.putExtra(kDATA_NAME, name);
		setResult(RESULT_OK, data);
		finish();
	}
	
	@ItemClick(R.id.searchResultsListView)
	void onSearchItemClick(int position) {
		ZcdhCertificate param = searchResult.get(position);
		String code = param.getCer_code();
		String name = param.getCer_name();

		Intent data = new Intent();
		data.putExtra(kDATA_CODE, code);
		data.putExtra(kDATA_NAME, name);
		setResult(RESULT_OK, data);
		finish();
	}
	

	@TextChange(R.id.keywordEditText)
	void keywordChange(CharSequence text, TextView hello, int before,
			int start, int count) {
		keyword = keywordEditText.getText().toString();
		if (!StringUtils.isBlank(keyword)) {
			clearBtn.setVisibility(View.VISIBLE);
			search();
		} else {
			clearBtn.setVisibility(View.GONE);
			searchResult.clear();
			certChosenAdapter.updateItems(searchResult);
			resultPannel.setVisibility(View.GONE);
			categoryListView.setVisibility(View.VISIBLE);

		}
	}
	
	@Click(R.id.clearBtn)
	void onClearBtn(){
		keywordEditText.setText("");
	}
	
	@Click(R.id.micBtn)
	void onMicBtn() {
		if (yuyinInputView == null) {
			yuyinInputView = new YuYinInputView(this, this);
		}
		yuyinInputView.showAtParent(findViewById(R.id.body));
	}
	

	@Override
	public void onComplete(String content) {
		this.keywordEditText.setText(content);
	}



	class CateogryStickAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

		@Override
		public int getCount() {
			return categorysCertificate.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.simple_listview_item_accessory, parent, false);
				holder.text = (TextView) convertView.findViewById(R.id.itemNameText);
				holder.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ZcdhCertificate post = categorysCertificate.get(position);

			holder.text.setText(post.getCer_name());
			holder.iv_right.setVisibility(View.GONE);
			// // 设置选中颜色
			// if (position == areaSelected) {
			// convertView.setBackgroundResource(R.drawable.selected_area);
			// } else {
			// convertView.setBackgroundResource(R.drawable.area_selector);
			// }

			return convertView;
		}

		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= sectionIndices.length) {
				section = sectionIndices.length - 1;
			} else if (section < 0) {
				section = 0;
			}
			return sectionIndices[section];
		}

		@Override
		public int getSectionForPosition(int position) {
			for (int i = 0; i < sectionIndices.length; i++) {
				if (position < sectionIndices[i]) {
					return i - 1;
				}
			}
			return sectionIndices.length - 1;
		}

		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			HeaderViewHolder holder;

			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.section_header, parent, false);
				holder.text = (TextView) convertView.findViewById(R.id.headText);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}

			// set header text as first char in name
			// CharSequence headerChar = mCountries[position].subSequence(0, 1);
			String parent_code = categorysCertificate.get(position).getParent_code();
			List<ZcdhCertificate> parents = finalDb.findAllByWhere(ZcdhCertificate.class, String.format("cer_code='%s'", parent_code));
			if (parents != null && parents.size() > 0) {
				holder.text.setText(parents.get(0).getCer_name());
			}

			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			String parent_code = categorysCertificate.get(position).getParent_code();

			return Long.valueOf(parent_code);
		}

		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView text;
			ImageView iv_right;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.app.activities.search.RemoveItemListner#onRemoveItem(
	 * java.lang.Object)
	 */
	@Override
	public void onRemoveItem(Object key) {
		// TODO Auto-generated method stub

	}

}
