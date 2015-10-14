package com.zcdh.mobile.framework.activities;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.K;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author yangjiannan
 *         首次打开app，新用户指引页面
 */
public class FWIntroduceActivity extends AppCompatActivity implements OnClickListener {

    protected int[] imageResIds; //要显示的图片的资源ID数组

    protected ArrayList<IntroducePageFragment> pages; //要显示的内容页

    protected ViewPager viewPager;

//    protected PageIndicator viewPageIndicator;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.framework_introduce);
        pages = new ArrayList<>();
        // 如果有资源ID 数组传入
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageResIds = extras.getIntArray(K.Extras.RES_IDS);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPageIndicator = (PageIndicatorCircle) findViewById(R.id.indicator);
        init();
    }


    //初始化所有页
    private void init() {
        if (imageResIds != null && imageResIds.length > 0) {
            for (int resId : imageResIds) {
                pages.add(IntroducePageFragment.newInstance(resId));
            }
        }

        viewPager.setAdapter(new IntroducePageAdapter(getSupportFragmentManager(),pages));
//        viewPageIndicator.setViewPager(viewPager);
    }

    /**
     * 添加一页
     */
    static class IntroducePageAdapter extends FragmentStatePagerAdapter {

        private ArrayList<IntroducePageFragment> list;

        public IntroducePageAdapter(FragmentManager fm, ArrayList<IntroducePageFragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image) {
            Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
        }
    }
}

