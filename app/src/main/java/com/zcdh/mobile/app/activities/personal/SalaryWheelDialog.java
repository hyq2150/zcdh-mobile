package com.zcdh.mobile.app.activities.personal;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.views.wheel.OnWheelChangedListener;
import com.zcdh.mobile.app.views.wheel.WheelView;
import com.zcdh.mobile.app.views.wheel.adapters.AbstractWheelTextAdapter;

/**
 * 
 * @author yangjiannan
 *
 */
public class SalaryWheelDialog extends AlertDialog implements  
View.OnClickListener, OnWheelChangedListener{
	
	private SalaryDialogWheelListner dialogWheelListner;
	
//	private Context context;
	/**
	 * 选择最低月薪
	 */
	private WheelView minSalaryWheelView;
	/**
	 * 选择最高月薪
	 */
	private WheelView maxSalaryWheelView;
	
	/**
	 * 面议按钮
	 */
	private Button mianyiBtn;
	
	/**
	 *确定按钮 
	 */
	private Button okBtn;
	
	private int min_scope = 1000000;
	private int max_scope = 1000000;
	private int stepValue = 500;
	
//	private int minWheelValue = 0;
	private int maxWheelValue = 1;
	
	
	private int minSalary =stepValue;
	private int maxSalary =stepValue*(maxWheelValue+1);

	private SalaryAdapter minSalaryAdapter;

	private SalaryAdapter maxSalaryAdapter;

	protected SalaryWheelDialog(Context context, SalaryDialogWheelListner dialogWheelListner) {
		super(context);
//		this.context = context;
		this.dialogWheelListner = dialogWheelListner;
	}
	
	protected SalaryWheelDialog(Context context, int theme) {
		super(context, theme);
//		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.salary_wheel_dialog);
		
		minSalaryAdapter = new SalaryAdapter(getContext(), true, stepValue); 
		maxSalaryAdapter = new SalaryAdapter(getContext(), false, stepValue);
		
		minSalaryWheelView = (WheelView) findViewById(R.id.minSalaryWheelView);
		minSalaryWheelView.setVisibleItems(5); // Number of items
		minSalaryWheelView.setWheelBackground(R.drawable.wheel_bg_holo);
		minSalaryWheelView.setWheelForeground(R.drawable.wheel_val_holo);
		minSalaryWheelView.setShadowColor(0xFF000000, 0x88000000, 0x00000000);
		minSalaryWheelView.setViewAdapter(minSalaryAdapter);
		minSalaryWheelView.setCurrentItem(0);
		minSalaryWheelView.addChangingListener(this);
		
		maxSalaryWheelView = (WheelView) findViewById(R.id.maxSalaryWheelView);
		maxSalaryWheelView.setVisibleItems(5); // Number of items
		maxSalaryWheelView.setWheelBackground(R.drawable.wheel_bg_holo);
		maxSalaryWheelView.setWheelForeground(R.drawable.wheel_val_holo);
		maxSalaryWheelView.setShadowColor(0xFF000000, 0x88000000, 0x00000000);
		maxSalaryWheelView.setViewAdapter(maxSalaryAdapter);
		maxSalaryWheelView.setCurrentItem(1);
		maxSalaryWheelView.addChangingListener(this);
		
		mianyiBtn = (Button) findViewById(R.id.mianYiBtn);
		mianyiBtn.setOnClickListener(this);
		okBtn = (Button) findViewById(R.id.okBtn);
		okBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.mianYiBtn){
			dialogWheelListner.onSalary(true, 0, 0);
		}
		if(v.getId()==R.id.okBtn){
			dialogWheelListner.onSalary(false, maxSalary, minSalary);
		}
		dismiss();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		
		Log.i("newValue:", newValue+"");
		
		if(wheel.getId()==R.id.maxSalaryWheelView){
			maxWheelValue = newValue;
			maxSalary = (newValue+1)*stepValue;
			maxSalary+= minSalary;
			
		}
		if(wheel.getId()==R.id.minSalaryWheelView){
//			minWheelValue = newValue;
			minSalary = (newValue+1)*stepValue;
			maxSalary = minSalary+(maxWheelValue*stepValue)+stepValue;
			maxSalaryAdapter = new SalaryAdapter(getContext(), false, minSalary+stepValue);
			maxSalaryWheelView.setViewAdapter(maxSalaryAdapter);
		}
	}

	/**
	 * Adapter for countries
	 */
	private class SalaryAdapter extends AbstractWheelTextAdapter {

		boolean isMin = true;
		
		int start ;
		/**
		 * Constructor
		 */
		protected SalaryAdapter(Context context, boolean isMin, int start) {
			super(context, R.layout.wheel_item, NO_RESOURCE);
			setItemTextResource(R.id.item_name);
			this.isMin = isMin;
			this.start = start;
		}
		/**
		 * Constructor
		 */
		protected SalaryAdapter(Context context, boolean isMin) {
			super(context, R.layout.wheel_item, NO_RESOURCE);
			setItemTextResource(R.id.item_name);
			this.isMin = isMin;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			if(isMin)return min_scope;
			return max_scope;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return ((index*stepValue)+start) + "";
		}
	}

}
