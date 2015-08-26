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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.biz.entities.ZcdhParam;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 参数列表
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_params)
public class ParamsActivity extends BaseActivity {

	public final static String kMSG_PARAM_SELECTED = "param_selected";
	public static final int kREQUEST_PARAM = 2005;
	
	/**
	 *  发布时间
	 */
	public final static String kCODE_PARAM_PUBLISH_TIME = "020";
	/**
	 *  薪酬范围
	 */
	public final static String kCODE_PARAM_PAYMENT_SCOPE = "006";
	/**
	 *  公司性质
	 */
	public final static String kCODE_PARAM_COMPANY_NATURE = "010";
	/**
	 *  公司规模
	 */
	public final static String kCODE_PARAM_COMPANY_SCALA = "011";
	/**
	 * 工作性质
	 */
	public final static String kCODE_PARAM_JOB_NATUAL= "007";
	
	/**
	 * 标识返回值是哪种类型参数
	 */
	public static final String kPARAM_CATEGORY_CODE = "param_category_code";
	public static final String kDATA_CODE = "param_name";
	public static final String kDATA_NAME = "param_code";

	
	
	
	@ViewById(R.id.paramsListView)
	ListView paramsListView;
	
	@Extra
	String paramCategoryCode;
	
	/**
	 * 已选的参数code
	 */
	@Extra
	String selectedParamCode;
	
	List<ZcdhParam> params;
	
	FinalDb finalDb;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		
	}
	
	@AfterViews
	void bindViews(){
		
		if(!StringUtils.isBlank(paramCategoryCode)){
			loadData();
		}else{
			String msg = "";
			if(K.debug){
				msg = "paramCategoryCode 为空";
			}else{
				msg = "抱歉，APP 发生了一点故障";
			}
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
		
		String title =null; 
		if(params!=null && params.size()>0 &&params.get(0)!=null){
			title = params.get(0).getRemark();
		}
		if(StringUtils.isBlank(title)){
			title = "请选择";
		}
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), title);
	}
	@Background
	void loadData(){
		if(finalDb==null){
			finalDb = DbUtil.create(this);
		}
		params = finalDb.findAllByWhere(ZcdhParam.class, String.format("param_category_code='%s'", paramCategoryCode));
		showData();
	}
	
	@UiThread
	void showData(){
		paramsListView.setAdapter(new ParamsAdapter());
	}
	
	@ItemClick(R.id.paramsListView)
	void onItemClick(int position){
		ZcdhParam param = params.get(position);
		String code = param.getParam_code();
		String name = param.getParam_name();
		
		Intent data = new Intent();
		data.putExtra(kDATA_CODE, code);
		data.putExtra(kDATA_NAME, name);
		data.putExtra(kPARAM_CATEGORY_CODE, paramCategoryCode);
		
		setResult(RESULT_OK, data);
		finish();
	}
	
	class ParamsAdapter extends BaseAdapter{


		@Override
		public int getCount() {
			return params.size();
		}

		@Override
		public Object getItem(int p) {
			return params.get(p);
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
				contentView = LayoutInflater.from(getBaseContext())
						.inflate(R.layout.simple_listitem, null);
				
				h.itemName = (TextView) contentView
						.findViewById(R.id.itemNameText);
				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();
			}
			
			h.itemName.setText(params.get(p).getParam_name());
			
			return contentView;
		}

		class Holder{
			TextView itemName = null; 
		}
		
	}
}
