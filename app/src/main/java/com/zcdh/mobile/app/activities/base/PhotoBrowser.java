/**
 * @author jeason, 2014-6-9 上午9:17:15
 */
package com.zcdh.mobile.app.activities.base;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photoview.PhotoView;
import com.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.HackyViewPager;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

import net.tsz.afinal.PrepareBitmapListner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * @author jeason, 2014-6-9 上午9:17:15 图片浏览 传入需要浏览的图片地址数组urls
 */
@EActivity(R.layout.activity_photoview)
public class PhotoBrowser extends BaseActivity {

    @ViewById(R.id.viewpager)
    HackyViewPager mViewPager;

    @ViewById(R.id.indicator)
    CirclePageIndicator indicator;

    private String imgUrl;

    private String[] urls;

    private int selection = 0;

    private ProcessDialog processDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void bindViews() {
        SystemServicesUtils.displayCustomTitle(this, getSupportActionBar(),
                "查看图片");
        SystemServicesUtils.hideActionbar(this);
        processDialog = new ProcessDialog(this);
        processDialog.show();

        getLocals();

        mViewPager.setAdapter(new SamplePagerAdapter(this, urls,
                getSupportActionBar(), indicator));
        mViewPager.setCurrentItem(selection);
        indicator.setViewPager(mViewPager);
    }

    /**
     * @author jeason, 2014-6-9 上午10:20:57
     */
    private void getLocals() {
        Bundle data = getIntent().getExtras();

        imgUrl = data.getString("imgUrl");
        urls = getIntent().getStringArrayExtra("urls");

        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            if (url.equals(imgUrl)) {
                selection = i;
            }
        }
    }

    class SamplePagerAdapter extends PagerAdapter implements
            PrepareBitmapListner {

        private String[] urls = null;

        private ActionBar actionBar;

        private DisplayImageOptions options;

        private CirclePageIndicator indicator;

        public SamplePagerAdapter(Context context, String[] urls,
                ActionBar actionBar, CirclePageIndicator indicator) {
            this.urls = urls;
            this.actionBar = actionBar;
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true).build();
            this.indicator = indicator;
        }

        @Override
        public int getCount() {
            if (urls == null) {
                return 0;
            }
            return urls.length;
        }

        @Override
        public void onPrepareDone() {
            processDialog.dismiss();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setBackgroundColor(container.getResources().getColor(
                    R.color.black));
            String url = urls[position];

            ImageLoader.getInstance().displayImage(url, photoView, options);
            container.addView(photoView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (actionBar.isShowing()) {
                        actionBar.hide();
                        indicator.setVisibility(View.GONE);
                    } else {
                        actionBar.show();
                        indicator.setVisibility(View.VISIBLE);
                    }
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
