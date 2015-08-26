package com.zcdh.mobile.app.activities.detail;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.framework.activities.FWTabBarFragmentActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

/**
 * 职位详情页，包含三个tab FragmentPostDetails 职位详情  EntDetailFragment 公司详情 MainEntMorePostsFragment 所有职位
 *
 * @author yangjiannan
 */
@OptionsMenu(R.menu.action_details)
@EActivity(R.layout.activity_details_frame)
public class DetailsFrameActivity extends FWTabBarFragmentActivity implements
        FWTabBarFragmentActivity.PagerSelectedListener {

    private static final String TAG = DetailsFrameActivity.class
            .getSimpleName();

    protected boolean cancelFavorite;

    private PostDetailsFragment_ postDetailsFragment;

    private EntDetailFragment_ entDetailFragment;

    private MainEntMorePostsFragment_ morePostsList;

    @Extra
    boolean isFair = false;

    @Extra
    long fairID;

    //职位ID
    @Extra
    long postId;

    @Extra
    int currentIndex;

    @Extra
    List<JobEntPostDTO> posts;

    @Extra
    boolean switchable = false;

    // 企业ID
    @Extra
    public long entId;

    @AfterViews
    void bindViews() {
        SystemServicesUtils.setActionBarCustomTitle(this,
                getSupportActionBar(), "正在加载...");
        initFragments();
    }

    /**
     * 初始化fragment 与 Viewpager 组合
     */
    private void initFragments() {
        tabTitle = new String[]{getString(R.string.post_detail),
                getString(R.string.ent_detail), getString(R.string.all_posts)};
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.kENT_ID, entId);
        bundle.putLong(Constants.kPOST_ID, postId);
        bundle.putBoolean(Constants.KFLAG_ENT, false);

        postDetailsFragment = new PostDetailsFragment_();
        entDetailFragment = new EntDetailFragment_();
        morePostsList = new MainEntMorePostsFragment_();
        Log.i(TAG, "entId:" + entId);
        entDetailFragment.setArguments(bundle);
        morePostsList.setArguments(bundle);

        fragments.add(postDetailsFragment);
        fragments.add(entDetailFragment);
        fragments.add(morePostsList);

        pagerSelectedListener = this;
        super.initParent();
    }

    @OptionsItem(android.R.id.home)
    void onBack() {
        if (cancelFavorite) {
            Intent data = new Intent();
            data.putExtra(Constants.kPOST_ID, postId);
            setResult(RESULT_OK, data);

        }
        finish();
    }

    @OptionsItem(R.id.action_jubao)
    void onSupervise(MenuItem item) {
        if (ZcdhApplication.getInstance().getZcdh_uid() != -1) {
            if (entId >= 0) {
                SuperviseActivity_.intent(this).entId(entId).start();
            }
        } else {
            LoginActivity_.intent(this).start();
        }
    }

    public void goesPager(int page) {
        mViewPager.setCurrentItem(page);
    }

    @OnActivityResult(Constants.REQUEST_CODE_COMMENT)
    void onCommentBack(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            entDetailFragment.loadData();
        }
    }

    public long getPostId() {
        return this.postId;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }

    public List<JobEntPostDTO> getPosts() {
        return posts;
    }

    public boolean switchable() {
        return switchable;
    }

    @Override
    public void onPageSelected(int page) {
        if (page == 1) {
            entDetailFragment.loadData();
        }
        if (page == 2) {
            morePostsList.loadData();
        }
    }

    public void refresh(long entId) {
        entDetailFragment.loadData(entId);
        morePostsList.loadData(entId);
    }
}
