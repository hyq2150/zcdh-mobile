package com.zcdh.mobile.app.activities.search;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobSearchTagDTO;


/**
 * 用于显示搜索面板中的标签组
 * @author yangjiannan
 *
 */
@EFragment(R.layout.fragment_tags_group)
public class FragmentTagsGroup extends Fragment {
	
	@ViewById(R.id.loadingImg)ImageView loadingImg;
	
	@ViewById(R.id.tagsGrideview)GridView tagsGrideView;
	
	private TagsSelectedListner tagsSelectedListner;
	
	private BaseAdapter tagsGrideViewAdapter;
	
	private boolean inited = false;
	private boolean setedData = false;
	
	/** 标签数据 **/
	List<JobSearchTagDTO> tags = new ArrayList<JobSearchTagDTO>();
	
	
	
	public void setTags(List<JobSearchTagDTO> tags){
		this.tags = tags;
		if(inited){
			////给数据后，停止加载动画
			loadingImg.clearAnimation();
			loadingImg.setVisibility(View.GONE);
			
			tagsGrideViewAdapter.notifyDataSetChanged();
			
			tagsGrideView.setVisibility(View.VISIBLE);
			
			//开始显示GridView标签组，淡入动画显示
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
			fadeIn.setDuration(700);
			tagsGrideView.startAnimation(fadeIn);
			
			Log.i("stoploadingtags", "stop");
		}
		setedData = true;
		Log.i("setTags", "settags");
	}
	
	
	
	public FragmentTagsGroup(){
		
	}
	
	public void setTagsSelectedLisnter(TagsSelectedListner listner){
		this.tagsSelectedListner = listner;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	
	
	@AfterViews
	void init(){
		
		tagsGrideViewAdapter = new TagsGrideViewAdapter();
		tagsGrideView.setAdapter(tagsGrideViewAdapter);
		Log.i("TAG","tagsGrideViewAdapter init:ini");
		inited = true;
		//正在加载标签组数据动画
		Log.i("tags:", this.tags+"");
		if(setedData){
			if(this.tags!=null){
				setTags(this.tags);
			}
		}else{
			Animation loadingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);
			loadingImg.startAnimation(loadingAnim);
		}
		tagsGrideViewAdapter.notifyDataSetChanged();
		
	}
	
	
	/*================== UI 事件处理 ===================== */
	@ItemClick(R.id.tagsGrideview)
	void onTagSelected(int position){
		if(this.tagsSelectedListner!=null){
			this.tagsSelectedListner.onTagSelected(tags.get(position));
		}
	}
	
	
	
	
	class TagsGrideViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return tags.size();
		}

		@Override
		public Object getItem(int p) {
			return tags.get(p);
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View view, ViewGroup viewGroup) {
			
			View itemView = null;
			TextView tagNameText = null;
			
			itemView = LayoutInflater.from(getActivity()).inflate(R.layout.tags_item, null);
			
			tagNameText = (TextView) itemView.findViewById(R.id.tagNameText);
			JobSearchTagDTO tagDto = tags.get(p); 
			String tagName = tagDto.getTagName();
			tagNameText.setText(tagName);
			return itemView;
		}
		
	}
	
	public interface TagsSelectedListner{
		// 在onCreateView 完成之后调用此方法
		public void onTagSelected(JobSearchTagDTO tagDto);
	}
	
}
