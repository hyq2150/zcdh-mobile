package com.huanxin.activity;

import com.huanxin.fragment.ImageGridFragment;
import com.zcdh.mobile.framework.activities.BaseActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * 环信拍摄视频页面
 * User: xyh
 * Date: 2015/9/14
 * Time: 10:36
 */
public class ImageGridActivity extends BaseActivity {

    private static final String TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }
}
