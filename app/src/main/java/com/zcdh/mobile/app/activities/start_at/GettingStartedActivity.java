package com.zcdh.mobile.app.activities.start_at;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.activities.main.NewMainActivity_;
import com.zcdh.mobile.framework.activities.FWIntroduceActivity;
import com.zcdh.mobile.utils.BitmapUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

//import com.zcdh.mobile.app.activities.main.NewMainActivity_;

/**
 * @author yangjiannan
 */
public class GettingStartedActivity extends FWIntroduceActivity implements
        OnPageChangeListener {

    public static final String kGETTING_STARTED = "kGETTING_STARTED";

    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageResIds = new int[]{R.drawable.guide001, R.drawable.guide002, R.drawable.guide003,
                R.drawable.guide004};
        super.onCreate(savedInstanceState);
        viewPageIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int page) {
        this.page = page;
    }

    public void onClick(View v) {
//		Log.e("TAG","******************************* " + page);
        if (page == imageResIds.length - 1) {
            boolean getting_stated = SharedPreferencesUtil.getValue(this,
                    kGETTING_STARTED, false);
            if (getting_stated) {
                finish();
                for (int i = 0; i < imageResIds.length - 1; i++) {
                    BitmapUtils.BpRecyle(getResources(), imageResIds[i]);
                }
            } else {
                NewMainActivity_.intent(this)
                        .flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT).start();
                finish();
                SharedPreferencesUtil.putValue(this, kGETTING_STARTED, true);
                for (int i = 0; i < imageResIds.length - 1; i++) {
                    BitmapUtils.BpRecyle(getResources(), imageResIds[i]);
                }
            }
        }
    }

}
