package com.zcdh.mobile.app.activities;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.nearby.NearbyMapFragment;
import com.zcdh.mobile.app.activities.newsmodel.FragmentNewsModel_;
import com.zcdh.mobile.app.activities.personal.FragmentMyHome_;
import com.zcdh.mobile.app.activities.search.FragmentMainSearch_;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity_;
import com.zcdh.mobile.framework.adapters.FragmentViewPagerAdapter;
import com.zcdh.mobile.framework.adapters.FragmentViewPagerAdapter.OnExtraPageChangeListener;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;
/**
 * 
 * @author yangjiannan 主页
 */
@EActivity(R.layout.activity_main)
public class BackupMainActivity extends AppCompatActivity implements
		OnExtraPageChangeListener, Subscriber {

	public static final int REQUEST_CODE_LOGIN = 0x01;

	public static final String kMSG_RESTART_APP = "restart";

	@ViewById(R.id.pager)
	ViewPager pager;

	@ViewById(R.id.mainMenu)
	TableLayout mainMenu;

	@ViewById(R.id.tabItemMyhome)
	Button tabItemMyhome;

	@ViewById(R.id.tabItemNearby)
	Button tabItemNearby;

	@ViewById(R.id.tabItemNewsmodel)
	Button tabItemNewsmodel;

	@ViewById(R.id.tabItemSearch)
	Button tabItemSearch;
	// 是否已经登录
	boolean isSign = false;

	FragmentViewPagerAdapter pagerAdapter;
	ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

	NearbyMapFragment nearby;
	FragmentMainSearch_ mainSearch;
	FragmentMyHome_ myhome;
	FragmentNewsModel_ newsModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyEvents.register(this);
	}

	/**
	 * 
	 * @author jeason, 2014-4-16 下午4:33:37
	 */
	private void check_if_login() {

		if (SystemServicesUtils.getZCDH_UID(this) != 0l) {
			this.isSign = true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu
	 * (com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_setting, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MyEvents.unregister(this);
	}

	@AfterViews
	void init() {
		check_if_login();

		// 设置标题
		SystemServicesUtils.setActionBarCustomTitleAndIcon(this,
				getSupportActionBar(),
				getString(R.string.activity_main_search_title),
				R.drawable.icon_personal_normal);

		nearby = new NearbyMapFragment();
		mainSearch = new FragmentMainSearch_();
		myhome = new FragmentMyHome_();
		newsModel = new FragmentNewsModel_();

		// 判断是否登录
		if (isSign) {

			fragmentList.add(nearby);
			fragmentList.add(mainSearch);
			fragmentList.add(myhome);
			fragmentList.add(newsModel);

			// 隐藏actionbar， 显示主菜单
			getSupportActionBar().hide();
			mainMenu.setVisibility(View.VISIBLE);

		} else {

//			fragmentList.add(mainSearch);

			// 隐藏主菜单， 显示actionbar
			getSupportActionBar().show();
			mainMenu.setVisibility(View.GONE);

		}

		// 设置ViewPager显示各子页
		pagerAdapter = new FragmentViewPagerAdapter(
				getSupportFragmentManager(), pager, fragmentList, 1, this);

	}

	/* ============ UI 事件处理 ================== */

	// ////////////// 未登录是得左右 actionbar menuItem 事件
	@OptionsItem(android.R.id.home)
	void onSettingMenu() {
		LoginActivity_.intent(this).startForResult(REQUEST_CODE_LOGIN);
	}

	@OptionsItem(R.id.action_settings)
	void onLoginMenu() {
		// ActivityDispatcher.to_login(this);
		SettingsHomeActivity_.intent(this).start();
	}

	// //PagerView 切换事件
	@Override
	public void onExtraPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onExtraPageSelected(int p) {
		setMenuStateStyle(p);
	}

	@Override
	public void onExtraPageScrollStateChanged(int i) {

	}

	// /////////// 已登录时主菜单的onTouch 事件
	@Touch(R.id.tabItemNearby)
	boolean onTabItemNearby(View v, MotionEvent event) {
		tabItemNearby.setSelected(true);
		setMenuStateStyle(0);
		pager.setCurrentItem(0);
		return true;
	}

	@Touch(R.id.tabItemSearch)
	boolean onTabItemSearch(View v, MotionEvent event) {
		tabItemSearch.setSelected(true);
		setMenuStateStyle(1);
		pager.setCurrentItem(1);
		return true;
	}

	@Touch(R.id.tabItemMyhome)
	boolean onTabItemMyhome(View v, MotionEvent event) {
		tabItemMyhome.setSelected(true);
		setMenuStateStyle(2);
		pager.setCurrentItem(2);
		return true;
	}

	@Touch(R.id.tabItemNewsmodel)
	boolean onTabItemNewsmodel(View v, MotionEvent event) {
		tabItemMyhome.setSelected(true);
		setMenuStateStyle(3);
		pager.setCurrentItem(3);
		return true;
	}

	// / 切换不同的pager设置菜单的样式
	void setMenuStateStyle(int current) {
		if (current == 0) {
			tabItemNearby.setSelected(true);
			tabItemSearch.setSelected(false);
			tabItemMyhome.setSelected(false);
			tabItemNewsmodel.setSelected(false);

		}
		if (current == 1) {
			tabItemNearby.setSelected(false);
			tabItemSearch.setSelected(true);
			tabItemMyhome.setSelected(false);
			tabItemNewsmodel.setSelected(false);
		}
		if (current == 2) {
			tabItemNearby.setSelected(false);
			tabItemSearch.setSelected(false);
			tabItemMyhome.setSelected(true);
			tabItemNewsmodel.setSelected(false);
		}
		if (current == 3) {
			tabItemNearby.setSelected(false);
			tabItemSearch.setSelected(false);
			tabItemMyhome.setSelected(false);
			tabItemNewsmodel.setSelected(true);
		}

	}

	@Override
	public void receive(String key, Object msg) {

		if (kMSG_RESTART_APP.equals(key)) { // 退出
			MyEvents.post(SettingsHomeActivity.kMSG_AUTH_STATU, 1);
		}
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		boolean result = false;
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_MOVE:
//			if (fragmentList.size() == 1) {
//				result = false;
//			} else {
//				result = true;
//			}
//
//			break;
//		}
//
//		return result;
//
//	}

}
