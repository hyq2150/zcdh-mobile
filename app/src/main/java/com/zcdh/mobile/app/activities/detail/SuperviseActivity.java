package com.zcdh.mobile.app.activities.detail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 举报
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_supervise)
@OptionsMenu(R.menu.action_supervise)
public class SuperviseActivity extends BaseActivity implements RequestListener{
	
	IRpcJobEnterpriseService jobenterpriseService;
	
	String kREQ_ID_addInform;
	
	@ViewById(R.id.contentScroll)
	ScrollView contentScroll;

	@ViewById(R.id.listView)
	ListView listView;
	
	@ViewById(R.id.remarkEditTxt)
	EditText remarkEditTxt;
	
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<String> subTitles = new ArrayList<String>();
	
	//诈骗类型
	ArrayList<String> typeCode = new ArrayList<String>();
	
	int selected = -1;
	
	@Extra
	long entId;

	private SuperviseAdapter adapter;
	
	@AfterViews
	void init(){
		
		//
		jobenterpriseService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
		
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.supervise));
		
		titles.add("骗子公司");
		subTitles.add("特点：职位无须有，骗钱骗色");
		titles.add("黑社会组织");
		subTitles.add("特点:以暴力垄断等手段获得利益");
		titles.add("传销组织");
		subTitles.add("特点:通过发展人加入组织计算报酬");
		titles.add("邪教组织");
		subTitles.add("特点:冒用宗教、气功等骗人钱财");
		titles.add("其他");
		subTitles.add("特点：给人第一感觉就不是正规公司");
		
		typeCode.add("001");
		typeCode.add("002");
		typeCode.add("003");
		typeCode.add("004");
		typeCode.add("005");
		if(adapter ==null){
			adapter = new SuperviseAdapter();
		}
		listView.setAdapter(adapter);
		/*remarkEditTxt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					contentScroll.fullScroll(View.FOCUS_DOWN);
					remarkEditTxt.requestFocus();
				}
			}
		});*/
		
	}
	
	
	@OptionsItem(R.id.action_supervise)
	void onOptionItem(){
		if(selected==-1){
			Toast.makeText(this,"请选择一个典型", Toast.LENGTH_SHORT
					).show();
		}else if(StringUtils.isBlank(remarkEditTxt.getText().toString())){
			Toast.makeText(this,"请提供详细的描述", Toast.LENGTH_SHORT
					).show();
			
		}else{
			submit();
		}
	}
	
	@ItemClick(R.id.listView)
	void onItemSelected(int position){
		selected = position;
		adapter.notifyDataSetChanged();
	}
	
	@Background
	void submit(){
		jobenterpriseService.addInform(ZcdhApplication.getInstance().getZcdh_uid(),
				entId, typeCode.get(selected), remarkEditTxt.getText().toString())
				.identify(kREQ_ID_addInform=RequestChannel.getChannelUniqueID(), this);
		
	}
	
	/**
	 * 
	 * @author yangjiannan
	 * 
	 */

	class SuperviseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public Object getItem(int p) {
			return titles.get(p);
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
				contentView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.supervise_item, null);

				h.itemName = (TextView) contentView
						.findViewById(R.id.itemNameText);
				h.itemDescriptionName =  (TextView) contentView.findViewById(R.id.itemDescription);
				h.checkIcon = (ImageView) contentView
						.findViewById(R.id.checkImg);
				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();
			}
			
			h.itemName.setText(titles.get(p));
			h.itemDescriptionName.setText(subTitles.get(p));
			
			if (selected==p) {
				h.checkIcon.setVisibility(View.VISIBLE);
			} else {
				h.checkIcon.setVisibility(View.GONE);
			}

			return contentView;
		}

		class Holder {
			TextView itemName = null;
			TextView itemDescriptionName = null;
			ImageView checkIcon = null;
		}

	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_ID_addInform)){
			int supervise = -1;
			if(result!=null){
				supervise = (Integer) result;
				
			}
			String msg = "";
			switch (supervise) {
			case 0:
				msg = "举报已提交";
				finish();
				break;
			case 1:
				msg ="系统繁忙";
				break;
			case 14:
				msg ="该企业已被举报";
				break;
			default:
				break;
			}
			
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "服务繁忙", Toast.LENGTH_SHORT).show();
	}
	
	
}
