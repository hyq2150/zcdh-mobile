package com.zcdh.mobile.app.activities.vacation;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.FitPostListDTO;

public class FitPostsListAdapter extends BaseAdapter {
	
	private Context context;
	
	/*** *适合用户的岗位列表*/
	private List<FitPostListDTO> fitPostsList;
	
	public FitPostsListAdapter(Context cont,List<FitPostListDTO> list){
		
		fitPostsList = list;
		context = cont;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fitPostsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fitPostsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder h = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.fitpostslist_popwindow, null);
			h = new Holder();
			h.cityNameTxt = (TextView) convertView.findViewById(R.id.cityname_text);
			h.postCountTxt = (TextView) convertView.findViewById(R.id.post_count_text);
			convertView.setTag(h);
			
			FitPostListDTO postDto = fitPostsList.get(position);
			h.cityNameTxt.setText(postDto.getAreaName());
			h.postCountTxt.setText(postDto.getFitPostTotals().toString());
			
		}
		return convertView;
	}
	
	class Holder {
		TextView cityNameTxt;// 城市名称
		TextView postCountTxt;// 岗位数量
	}
}
