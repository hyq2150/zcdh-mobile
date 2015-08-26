package com.zcdh.mobile.app.activities.nearby;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.api.IRpcJobPostService;
import com.zcdh.mobile.api.model.JobSpecialRequirementsMatchDTO;
import com.zcdh.mobile.api.model.UnMatchTeclDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.R;
/**
 * 匹配详情
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_match_details)
public class MatchDetailsActivity extends BaseActivity implements RequestListener, DataLoadInterface{
	
	
	String kREQ_findUnMatchTeclByUserId;
	
	@ViewById(R.id.contentView)
	LinearLayout contentView;
	
	@ViewById(R.id.emptyView)
	EmptyTipView emptyView;
	
	@ViewById(R.id.matchListView)
	PullToRefreshListView matchListView;
	
	@ViewById(R.id.title)
	TextView titleText;
	
	IRpcJobPostService jobPostService;
	
	List<UnMatchTeclDTO> noMatchedSkills = new ArrayList<UnMatchTeclDTO>();
	
	MatchDetailsAdapter adapter = null;

	@Extra
	long postId;
	
	/**
	 * 匹配度
	 */
	@Extra
	int matchRate;
	
	@AfterViews
	void bindViews(){
		
		jobPostService = RemoteServiceManager.getRemoteService(IRpcJobPostService.class);
		
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "匹配详情");
		
		adapter = new MatchDetailsAdapter();
		matchListView.setAdapter(adapter);
		
		titleText.setText("您与岗位的匹配度为："+matchRate +"%, 以下是您未匹配的技能项：");
		
		loadData();
		
	}
	
	@Background
	public void loadData(){
		jobPostService.findUnMatchTeclByUserId(getUserId(), postId).identify(kREQ_findUnMatchTeclByUserId=RequestChannel.getChannelUniqueID(), this);
	}

	
	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_findUnMatchTeclByUserId)){
			if(result!=null){
				noMatchedSkills = (List<UnMatchTeclDTO>) result;
				adapter.notifyDataSetChanged();
			}
			if(noMatchedSkills.size()>0){
				emptyView.isEmpty(false);
				contentView.setVisibility(View.VISIBLE);
			}else{
				emptyView.isEmpty(true);
				contentView.setVisibility(View.GONE);
			}
		}
			
	}

	@Override
	public void onRequestFinished(String reqId) {
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}
	
	
	class MatchDetailsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return noMatchedSkills.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return noMatchedSkills.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if(convertView==null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.match_detail_item, null);
				h = new ViewHolder();
				h.skillNameText = (TextView) convertView.findViewById(R.id.skillNameText);
				h.matchRateText = (TextView) convertView.findViewById(R.id.matchRateText);
				convertView.setTag(h);
			}else{
				h = (ViewHolder) convertView.getTag();
			}
			UnMatchTeclDTO dto = noMatchedSkills.get(position);
			if(dto!=null){
				h.skillNameText.setText(dto.getTel_name());
				h.matchRateText.setText(dto.getParam_value());
				
			}
			return convertView;
		}
		
		class ViewHolder{
			TextView skillNameText;
			TextView matchRateText;
		}
	}
	
}
