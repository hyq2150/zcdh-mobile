package com.zcdh.mobile.app.views.filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.adapter.TextAdapter;

/**
 * 顶部菜单弹出筛选条件
 * 由两个listview组成
 * @author jeason, 2014-7-25 下午2:53:09
 */
public class FilterView extends LinearLayout implements ViewBaseAction {

	private ListView firstListView;
	private ListView secondListView;
	private ArrayList<HashMap<String, String>> mGroups;// = new
														// ArrayList<HashMap<String,
														// String>>();
	private LinkedList<HashMap<String, String>> childrenItem = new LinkedList<HashMap<String, String>>();
	private SparseArray<LinkedList<HashMap<String, String>>> mChildren;// = new
																		// SparseArray<LinkedList<HashMap<String,
																		// String>>>();
	private TextAdapter secondListViewAdapter;
	private TextAdapter firstListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int level1Position = 0;
	private int level2Position = 0;
	private String showString = "不限";

	public static final int NON_SELECTED_POSITION = -0x1000;
	private int mFilterType;

	public FilterView(Context context) {
		this(context, null);
	}

	public FilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// public void updateShowText(String showArea, String showBlock) {
	// if (showArea == null || showBlock == null) {
	// return;
	// }
	// for (int i = 0; i < groups.size(); i++) {
	// if (groups.get(i).equals(showArea)) {
	// earaListViewAdapter.setSelectedPosition(i);
	// childrenItem.clear();
	// if (i < children.size()) {
	// childrenItem.addAll(children.get(i));
	// }
	// tEaraPosition = i;
	// break;
	// }
	// }
	// for (int j = 0; j < childrenItem.size(); j++) {
	// if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
	// plateListViewAdapter.setSelectedPosition(j);
	// tBlockPosition = j;
	// break;
	// }
	// }
	// setDefaultSelect();
	// }

	public void init(Context context, int filter_type, ArrayList<HashMap<String, String>> groups, SparseArray<LinkedList<HashMap<String, String>>> children) {
		mGroups = groups;
		mChildren = children;
		mFilterType = filter_type;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		firstListView = (ListView) findViewById(R.id.listView);
		secondListView = (ListView) findViewById(R.id.listView2);
		if (mChildren == null) secondListView.setVisibility(View.GONE);
		// setBackgroundResource(R.drawable.choosearea_bg_mid);

		firstListViewAdapter = new TextAdapter(context, mGroups, R.drawable.choose_item_selected, R.drawable.choose_eara_item_selector);
		firstListViewAdapter.setTextSize(17);
		firstListViewAdapter.setSelectedPositionNoNotify(level1Position);
		firstListView.setAdapter(firstListViewAdapter);
		firstListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				// if (position < mChildren.size()) {
				if (secondListViewAdapter != null) {
					childrenItem.clear();
					childrenItem.addAll(mChildren.get(position));
					secondListViewAdapter.notifyDataSetChanged();
				}

				level1Position = position;

				if (secondListViewAdapter == null || secondListViewAdapter.getCount() == 0) {
					secondListView.setVisibility(View.GONE);
					if (mOnSelectListener != null) {
						// 当没有第二级过滤条件时回传第一级位置id 第二级默认为:NON_SELECTED_POSITION
						mOnSelectListener.getValue(mFilterType, mGroups.get(level1Position).get("value"), level1Position, NON_SELECTED_POSITION, mGroups.get(level1Position).get("name"));
					}
				} else {
					secondListView.setVisibility(View.VISIBLE);
				}
			}
			// }
		});

		if (mChildren != null) {
			if (level1Position < children.size()) childrenItem.addAll(children.get(level1Position));

			secondListViewAdapter = new TextAdapter(context, childrenItem, R.drawable.choose_item_right, R.drawable.choose_plate_item_selector);
			secondListViewAdapter.setTextSize(15);
			secondListViewAdapter.setSelectedPositionNoNotify(level2Position);
			secondListView.setAdapter(secondListViewAdapter);
			secondListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

				@Override
				public void onItemClick(View view, final int position) {

					showString = childrenItem.get(position).get("name");
					if (mOnSelectListener != null) {
						// 回传第一第二级位置id
						level2Position = position;
						mOnSelectListener.getValue(mFilterType, childrenItem.get(level2Position).get("value"), level1Position, level2Position, showString);
					}

				}
			});
		}

		if (secondListViewAdapter == null || secondListViewAdapter.getCount() == 0) {
			secondListView.setVisibility(View.GONE);
		} else {
			secondListView.setVisibility(View.VISIBLE);
		}

		if (level2Position < childrenItem.size()) showString = childrenItem.get(level2Position).get("name");

		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		firstListView.setSelection(level1Position);
		secondListView.setSelection(level2Position);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		// public void getValue(String valueId, String showText);
		// public void getValue(String valueId, String showText);
				public void onTimeSelector(int filter_type, Date start_date, Date end_date);

		public void getValue(int filter_type, String value, int level1_selected_position, int level2_selected_position, String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
	
	Button btn_confirm;
	EditText starTime;
	EditText endTime;
	Calendar calendar_start;
	Calendar calendar_end;
	TextView tv_clear;
	public void initTimeFiter(final Context context, int filtre_type) {
		mFilterType = filtre_type;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.time_filter, this, true);
		starTime = (EditText) findViewById(R.id.start_time);
		
		endTime = (EditText) findViewById(R.id.end_time);
		calendar_start = Calendar.getInstance();
		calendar_end = Calendar.getInstance();
		starTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						calendar_start.set(year, monthOfYear, dayOfMonth);
						starTime.setText(calendar_start.getTime().toLocaleString());

					}
				}, calendar_start.get(Calendar.YEAR), calendar_start.get(Calendar.MONTH), calendar_start.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		endTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						calendar_end.set(year, monthOfYear, dayOfMonth);
						endTime.setText(calendar_end.getTime().toLocaleString());

					}
				}, calendar_end.get(Calendar.YEAR), calendar_end.get(Calendar.MONTH), calendar_end.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!calendar_end.after(calendar_start)) {
					Toast.makeText(getContext(), "结束时间必须晚于开始时间", Toast.LENGTH_SHORT).show();
					return;
				}
				if(mOnSelectListener!=null)
				mOnSelectListener.onTimeSelector(mFilterType, calendar_start.getTime(), calendar_end.getTime());
			}
		});
		tv_clear = (TextView)findViewById(R.id.tv_clear);
		tv_clear.setText(Html.fromHtml("<u>"+"清除条件"+"</u>"));
		tv_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				starTime.setText("");
				endTime.setText("");
				if(mOnSelectListener!=null)
					mOnSelectListener.onTimeSelector(mFilterType, null, null);
			}
		});
	}

}
