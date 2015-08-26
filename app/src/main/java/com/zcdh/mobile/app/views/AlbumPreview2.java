package com.zcdh.mobile.app.views;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.zcdh.mobile.R;
/**
 * 相册预览
 * @author jeason, 2014-7-25 下午2:55:28
 */
public class AlbumPreview2 extends RelativeLayout {
	GridView grid_view;

	AlbumAdapter adapter;

	private List<HashMap<String, String>> mResults;

	public AlbumPreview2(Context context) {
		this(context, null);
	}
	
	private OnItemClickListener itemClickListener;

	public AlbumPreview2(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.album_preview2, this);
		bindViews();
	}

	/**
	 * 
	 * @author jeason, 2014-4-16 上午9:09:45
	 */
	private void bindViews() {
		grid_view = (GridView) findViewById(R.id.grid_view);
		grid_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(itemClickListener!=null)itemClickListener.onItemClick(parent, view, position, id);
			}
		});
      
	}

	public void initData(List<HashMap<String, String>> results, OnItemClickListener clickListener) {
		this.itemClickListener = clickListener;
		mResults = results;
		adapter = new AlbumAdapter();
		grid_view.setAdapter(adapter);
		if (mResults == null || mResults.isEmpty()) {
			grid_view.setEmptyView(new EmptyTipView(getContext()));
		}
	}

	private class AlbumAdapter extends BaseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mResults.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AlbumCover cover = AlbumCover_.build(getContext());
			
			cover.switchSize();
			cover.initData(mResults.get(position).get("img_url"));
			return cover;
		}

	}
}
