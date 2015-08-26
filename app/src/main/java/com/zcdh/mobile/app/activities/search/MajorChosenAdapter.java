package com.zcdh.mobile.app.activities.search;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.biz.entities.ZcdhMajor;

/**
 * 显示专业搜索结果
 * @author YJN
 *
 */
public class MajorChosenAdapter extends BaseAdapter{
	
	List<ZcdhMajor> majors; 
	
	HashMap<String, ZcdhMajor> selectedMajors = new HashMap<String, ZcdhMajor>();
	
	Context context;
	
	boolean heightlight = false;

	public MajorChosenAdapter(Context context, List<ZcdhMajor> majors) {
		this.majors =majors;
		this.context = context;
	}
	public void setHightlinght(boolean h){
		this.heightlight = h;
	}
	
	public void updateItems(List<ZcdhMajor> posts ){
		this.majors = posts;
		notifyDataSetChanged();
	}
	
	public void updateSelected(HashMap<String, ZcdhMajor> selectedMajors){
		this.selectedMajors = selectedMajors;
		notifyDataSetChanged();
	}

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
			contentView = LayoutInflater.from(context)
					.inflate(R.layout.simple_listview_item_checker, null);
			
			h.itemName = (TextView) contentView
					.findViewById(R.id.itemNameText);
			h.checkerImg = (ImageView) contentView.findViewById(R.id.checkerImg);
			
			contentView.setTag(h);
		} else {
			h = (Holder) contentView.getTag();
			
		}
		
		h.itemName.setText(majors.get(p).getMajor_name());
		if(heightlight){
			h.itemName.setTextColor(context.getResources().getColor(R.color.result_hightlight));
		}else{
			h.itemName.setTextColor(context.getResources().getColor(R.color.font_color));
		}
		
		String code = majors.get(p).getCode();
		if(selectedMajors.containsKey(code)){
			h.checkerImg.setVisibility(View.VISIBLE);
		}else{
			h.checkerImg.setVisibility(View.GONE);
		}
		
		return contentView;
	}

	class Holder{
		TextView itemName = null; 
		ImageView checkerImg = null;
	}
	
}

