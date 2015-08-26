/**
 * 
 * @author jeason, 2014-4-23 下午12:06:45
 */
package com.zcdh.mobile.app.activities.vacation;

import com.zcdh.mobile.R;
import com.zcdh.mobile.utils.SystemServicesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

/**
 * @author jeason, 2014-4-23 下午12:06:45
 * 未支付与已支付订单列表
 */
public class OrdersActivity extends AppCompatActivity implements OnClickListener {

	private Button btn_pending;
	private Button btn_paid;

	ViewPager pager;
	FragementPageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "我的订单");
		setContentView(R.layout.orders);
		bindviews();
		setCurrentPage(0);
	}

	/**
	 * 
	 * @author jeason, 2014-4-23 下午4:27:56
	 */
	private void bindviews() {

		// RelativeLayout convertView = (RelativeLayout)
		// LayoutInflater.from(this).inflate(R.id.myToolbar_summerjobs_apply,
		// null);

		btn_pending = (Button) findViewById(R.id.btn_pending);
		btn_paid = (Button) findViewById(R.id.btn_paid);
		btn_pending.setOnClickListener(this);
		btn_paid.setOnClickListener(this);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new FragementPageAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(2);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setCurrentPage(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class FragementPageAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> fms;

		/**
		 * @author jeason, 2014-4-23 下午5:19:11
		 * @param fm
		 */
		public FragementPageAdapter(FragmentManager fm) {
			super(fm);
			fms = new ArrayList<Fragment>();
			fms.add(OrdersFmUnPaid.getInstance(OrdersFmPaid.TYPE_PENDING));
			fms.add(OrdersFmPaid.getInstance(OrdersFmPaid.TYPE_PAID));

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int arg0) {
			return fms.get(arg0);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return 2;
		}

	}

	/**
	 * 
	 * @author jeason, 2014-4-23 下午4:27:05
	 */
	private void setCurrentPage(int page_index) {
		if (page_index == 0) {

			btn_pending.setBackgroundResource(R.drawable.order_indicator);
			btn_pending.setTextColor(getResources().getColor(R.color.white));

			btn_paid.setBackgroundResource(android.R.color.transparent);
			btn_paid.setTextColor(getResources().getColor(R.color.black));
		} else {
			btn_paid.setBackgroundResource(R.drawable.order_indicator);
			btn_paid.setTextColor(getResources().getColor(R.color.white));

			btn_pending.setBackgroundResource(android.R.color.transparent);
			btn_pending.setTextColor(getResources().getColor(R.color.black));
		}
		pager.setCurrentItem(page_index, true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pending:
			setCurrentPage(0);
			break;
		case R.id.btn_paid:
			setCurrentPage(1);
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == OrdersFmPaid.REQUEST_CODE_ON_CACEL_ORDER && data != null) {
			// removeOrder(data.getStringExtra("order_num"));
			if (adapter.getItem(pager.getCurrentItem()) instanceof OrdersFmPaid) {
				((OrdersFmPaid) adapter.getItem(pager.getCurrentItem())).removeOrder(data.getStringExtra("order_num"));
			}
			if (adapter.getItem(pager.getCurrentItem()) instanceof OrdersFmUnPaid) {
				((OrdersFmUnPaid) adapter.getItem(pager.getCurrentItem())).removeOrder(data.getStringExtra("order_num"));
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
