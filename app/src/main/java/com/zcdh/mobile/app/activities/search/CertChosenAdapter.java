
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
import com.zcdh.mobile.biz.entities.ZcdhCertificate;

/**
 * 显示证书搜索结果
 * @author YJN
 *
 */
public class CertChosenAdapter extends BaseAdapter{
	
	List<ZcdhCertificate> certs; 
	
	HashMap<String, ZcdhCertificate> selectedMajors = new HashMap<String, ZcdhCertificate>();
	
	Context context;
	
	boolean heightlight = false;

	public CertChosenAdapter(Context context, List<ZcdhCertificate> majors) {
		this.certs =majors;
		this.context = context;
	}
	public void setHightlinght(boolean h){
		this.heightlight = h;
	}
	
	public void updateItems(List<ZcdhCertificate> posts ){
		this.certs = posts;
		notifyDataSetChanged();
	}
	
	public void updateSelected(HashMap<String, ZcdhCertificate> selectedMajors){
		this.selectedMajors = selectedMajors;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return certs.size();
	}

	@Override
	public Object getItem(int p) {
		return certs.get(p);
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
		
		h.itemName.setText(certs.get(p).getCer_name());
		if(heightlight){
			h.itemName.setTextColor(context.getResources().getColor(R.color.result_hightlight));
		}else{
			h.itemName.setTextColor(context.getResources().getColor(R.color.font_color));
		}
		
		String code = certs.get(p).getCer_code();
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
