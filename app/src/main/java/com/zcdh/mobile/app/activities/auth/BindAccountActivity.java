package com.zcdh.mobile.app.activities.auth;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;
import com.zcdh.mobile.R;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yangjiannan
 *绑定新账号(新建账号、与已有账号绑定)
 */
@Deprecated
@EActivity(R.layout.activity_bind_account)
public class BindAccountActivity extends AppCompatActivity {

	
	@ViewById(R.id.indicator)
	TabPageIndicator indicator;

	@ViewById(R.id.pager)
	ViewPager pager;
	
	List<Fragment> fragments = new ArrayList<Fragment>();
	
	BindNewUserFragment_ bindNewUserFragment;
	BindExsitingUserFragment_ bindExsitingUserFragment;
	
	MainFragmentsAdapter fragmentsAdapter;
	
	@Extra
	String third_open_id;
	
	@Extra
	String login_type;
	
	@AfterViews
	void bindViews(){
		
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "绑定账号");
		
		bindNewUserFragment = new BindNewUserFragment_();
		bindExsitingUserFragment = new BindExsitingUserFragment_();
		fragments.add(bindNewUserFragment);
		fragments.add(bindExsitingUserFragment);
		
		fragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
		pager.setAdapter(fragmentsAdapter);
		indicator.setViewPager(pager);
		indicator.setTabIconLocation(TabPageIndicator.LOCATION_RIGHT);
	}
	
	
	@OptionsItem(android.R.id.home)
	void onBack(){
		finish();
	}
	
	
	private class MainFragmentsAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		int[] titleRes = { R.string.perfect_info, R.string.bind_exsiting_account };

		/**
		 * @author jeason, 2014-6-11 下午2:59:08
		 * @param fm
		 */
		public MainFragmentsAdapter(FragmentManager fm) {
			super(fm);
		}


		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int p) {
			return fragments.get(p);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getString(titleRes[position]);
		}


		@Override
		public int getIconResId(int index) {
			return 0;
		}

	}
}
