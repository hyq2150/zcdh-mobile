package com.zcdh.mobile.app.activities.settings;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 关于我们
 * @author yangjiannan
 *2014-05-21
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends BaseActivity {
	
	private String website = "http://www.jobLBS.com";
	private String weibo = "http://weibo.com/zcdhkj";
	private String contact ="400-0055-100";

	@ViewById(R.id.aboutListView)
	ListView aboutListView;
	
	@ViewById(R.id.appVersionText)
	TextView appVersionText;
	
	/**
	 * 关于内容
	 */
	private String[] abouts =null;
	

	
	
	@AfterViews
	void bindViews(){
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_about));
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			appVersionText.setText("职场导航" + version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		String simple_adapter_key =  "title";
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String>item = null;
		
		item = new HashMap<String, String>();
		item.put(simple_adapter_key, "公司网站:" + website);
		list.add(item);

		item = new HashMap<String, String>();
		item.put(simple_adapter_key, "公司微博:" + weibo);
		list.add(item);

		item = new HashMap<String, String>();
		item.put(simple_adapter_key, "联系我们:" + contact);
		list.add(item);
		
		

		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
				list, R.layout.simple_listview_item_accessory,
				new String[] { simple_adapter_key },
				new int[] { R.id.itemNameText });
		
		aboutListView.setAdapter(adapter);
	}
	
	
	@ItemClick(R.id.aboutListView)
	void onClickItem(int p){
		switch (p) {
		case 0:
			Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
			startActivity(websiteIntent);
			break;
			
		case 1:
			Intent weiboIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(weibo));
			startActivity(weiboIntent);
			break;
			
		case 2:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
			startActivity(intent);
			break;
		}
	}
	
	class AboutAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return abouts.length;
		}

		@Override
		public Object getItem(int position) {
			return abouts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
		
	}
}
