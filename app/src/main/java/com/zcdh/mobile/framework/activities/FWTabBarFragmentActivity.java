package com.zcdh.mobile.framework.activities;

import com.zcdh.mobile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author yangjiannan
 * 具有顶部tab 页切换效果
 * 使用：
 * 1）继承此类
 * 2) 在布局文件中including  tab_layout.xml 布局文件,此布局文件是主体部分，包含了tab 切换的title，和
 * 切换Fragment 的ViewPager
 * 3）在子类中初始化 tabTitle 和fragments 并赋值
 * 4) 在子类调用初始化方法initParent ,注意此方法要在以上两步完成之后调用
 */
public class FWTabBarFragmentActivity extends AppCompatActivity {

	public static final String ARGUMENTS_NAME = "arg";
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private int indicatorWidth;
	private LayoutInflater mInflater;
	private int currentIndicatorLeft = 0;

	protected ViewPager mViewPager;
	protected TabFragmentPagerAdapter mAdapter;
	/**
	 * 以下tabTitle 和 fragments 有子类在onCreate时初始化值
	 */
	protected String[] tabTitle = { "选项1", "选项2", "选项3", "选项4", "选项5" };	//标题
	protected List<Fragment> fragments = new ArrayList<Fragment>();
	protected int screenHeight;
	protected int screenWidth;
//	private boolean isFirst = true;
	protected boolean specialIndex;
	
	protected PagerSelectedListener pagerSelectedListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
	}
	
	/**
	 * 由子类调用，onCreate 中调用
	 */
	protected void initParent() {
		findViewById();
		initView();
		((RadioButton)rg_nav_content.getChildAt(0)).performClick();
		setListener();
	}

	private void setListener() {
		
		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
				if(rg_nav_content!=null && rg_nav_content.getChildCount()>position){
					((RadioButton)rg_nav_content.getChildAt(position)).performClick();
				}
				if(pagerSelectedListener!=null){
					pagerSelectedListener.onPageSelected(position);
				}
			}
		});
		
		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(rg_nav_content.getChildAt(checkedId)!=null){
					
					if(specialIndex){
						specialIndex = false;
						int currentIndicatorLeft_first = screenWidth/mAdapter.getCount()*mViewPager.getCurrentItem();
						TranslateAnimation animation = new TranslateAnimation(
								currentIndicatorLeft ,
								currentIndicatorLeft_first, 0f, 0f);
						animation.setInterpolator(new LinearInterpolator());
						animation.setDuration(200);
						animation.setFillAfter(true);
						
						//执行位移动画
						iv_nav_indicator.startAnimation(animation);
						
						mViewPager.setCurrentItem(checkedId);	//ViewPager 跟随一起 切换
					}else{
						
						TranslateAnimation animation = new TranslateAnimation(
								currentIndicatorLeft ,
								((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
						animation.setInterpolator(new LinearInterpolator());
						animation.setDuration(200);
						animation.setFillAfter(true);
						
						//执行位移动画
						iv_nav_indicator.startAnimation(animation);
						
						mViewPager.setCurrentItem(checkedId);	//ViewPager 跟随一起 切换
						
						//记录当前 下标的距最左侧的 距离
						currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
					}

				}
			}
		});
	}

	private void initView() {
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		indicatorWidth = dm.widthPixels / tabTitle.length;
		
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);

		//获取布局填充器
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

		initNavigationHSV();
		
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}

	private void initNavigationHSV() {
		
		
		rg_nav_content.removeAllViews();
		
		for(int i=0;i<tabTitle.length;i++){
			
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.fw_tab_nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
		
	}

	private void findViewById() {
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mViewPager.setOffscreenPageLimit(tabTitle.length);
	}
	
	public class TabFragmentPagerAdapter extends FragmentPagerAdapter{

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			
			return fragments.get(position);
		}
		@Override
		public int getCount() {
			return tabTitle.length;
		}
		
	}

	public interface PagerSelectedListener{
		void onPageSelected(int page);
	}
}

