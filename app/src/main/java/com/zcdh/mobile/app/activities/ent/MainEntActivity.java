package com.zcdh.mobile.app.activities.ent;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.EntHomePageDTO;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.detail.EntDetailFragment_;
import com.zcdh.mobile.app.activities.detail.MainEntMorePostsFragment_;
import com.zcdh.mobile.app.activities.detail.SuperviseActivity_;
import com.zcdh.mobile.framework.activities.FWTabBarFragmentActivity;
import com.zcdh.mobile.framework.adapters.FragmentViewPagerAdapter.OnExtraPageChangeListener;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @author yangjiannan 企业主页
 */
@EActivity(R.layout.activity_main_ent)
public class MainEntActivity extends FWTabBarFragmentActivity implements
        OnExtraPageChangeListener, RequestListener,
        FWTabBarFragmentActivity.PagerSelectedListener {

    private static final String TAG = MainEntActivity.class.getSimpleName();

    private String kREQ_ID_findEntHomePageDTO;

    private String kREQ_ID_updateJobBlack;

    private String kREQ_ID_updateAttentionEnt;

    private String kREQ_ID_findEntShareDTO;

    private Menu menu;

    /**
     * 公司详情
     */
    private EntDetailFragment_ entDetailsFragment;

    /**
     * 产品
     */
    private MainEntProductFragment_ productsFragment;

    /**
     * 评价
     */
    private MainEntCommentFragment_ commentFragment;

    /**
     * 招聘
     */
    private MainEntMorePostsFragment_ jobfairFragment;

    /**
     * 行业名称
     */
    @ViewById(R.id.entIndustryTxt)
    TextView enIndustryTxt;

    /**
     * 公司名称
     */
    @ViewById(R.id.entNameTxt)
    TextView entNameTxt;

    @ViewById(R.id.renzhengImg)
    ImageView renzhengImg;

    /**
     * 企业LOGO
     */
    @ViewById(R.id.entLogoImg)
    ImageView entLogoImg;

    /**
     * 关注
     */
    @ViewById(R.id.foucsTxt)
    TextView foucsTxt;

    @ViewById(R.id.foucsImg)
    ImageView foucsImg;

    /**
     * 主页信息
     */
    EntHomePageDTO homePageDTO;

    @Extra
    long entId;

    @Extra
    int default_index = 0;

    @Extra
    long jobfair_id;

    private IRpcJobEnterpriseService enterpriseService;

    private DisplayImageOptions options;

    /**
     * 企业分享的内容
     */
    private List<JobEntShareDTO> shareContents;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            String entid = getIntent().getExtras().getString(Constants.kENT_ID);
            if (!TextUtils.isEmpty(entid)) {
                entId = Long.valueOf(entid);
            }
        }
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .considerExifParams(true).build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_ent_menu, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @AfterViews
    void bindViews() {
        enterpriseService = RemoteServiceManager
                .getRemoteService(IRpcJobEnterpriseService.class);
        SystemServicesUtils.setActionBarCustomTitle(getApplicationContext(),
                getSupportActionBar(),
                getString(R.string.activity_title_main_ent));
        entDetailsFragment = new EntDetailFragment_();
        productsFragment = new MainEntProductFragment_();
        commentFragment = new MainEntCommentFragment_();
        jobfairFragment = new MainEntMorePostsFragment_();

        Log.i(TAG, "entId:" + entId);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.kENT_ID, entId);
        bundle.putBoolean("hide_home", true);
        bundle.putBoolean(Constants.KFLAG_ENT, true);
        bundle.putLong(Constants.JOBFAIR_ID_KEY, jobfair_id);

        entDetailsFragment.setArguments(bundle);
        productsFragment.setArguments(bundle);
        commentFragment.setArguments(bundle);
        jobfairFragment.setArguments(bundle);

        tabTitle = new String[]{getString(R.string.ent_main),
                getString(R.string.ent_products),
                getString(R.string.ent_comments),
                getString(R.string.ent_job_fair)};

        fragments.add(entDetailsFragment);
        fragments.add(productsFragment);
        fragments.add(commentFragment);
        fragments.add(jobfairFragment);

        pagerSelectedListener = this;
        super.initParent();
        if (default_index > 0) {
            specialIndex = true;
        }
        loadData();
        mViewPager.setCurrentItem(default_index);
    }

    @OptionsItem(android.R.id.home)
    void onBack() {
        finish();
    }

    @OptionsItem(R.id.action_fansList)
    void onFansList() {
        MainEntFansActivity_.intent(this).entId(entId).start();
    }

    /**
     * 取消或加入黑名单
     */
    @OptionsItem(R.id.action_blacklist)
    void onBlackList() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(this,
                    getResources().getString(R.string.login_first),
                    Toast.LENGTH_SHORT).show();
            LoginActivity_.intent(this).startForResult(
                    Constants.REQUEST_CODE_LOGIN);
        } else {
            updateUpdateBlack();
        }
    }

    @Click(R.id.foucsRl)
    void onUpdateFoucs() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(this,
                    getResources().getString(R.string.login_first),
                    Toast.LENGTH_SHORT).show();
            LoginActivity_.intent(this).startForResult(
                    Constants.REQUEST_CODE_LOGIN);
        } else {
            if (SystemServicesUtils.isCompleteInfo(this)) {
                updateFoucs();
            }
        }
    }

    /**
     * 留言
     */
    @OptionsItem(R.id.action_liuyan)
    void onLiuyan() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.login_first_then_message),
                    Toast.LENGTH_SHORT).show();
            ActivityDispatcher.to_login(this);
        } else {
            if (SystemServicesUtils.isCompleteInfo(this)) {
                MainEntLiuyanActivity_.intent(this).entId(entId).type("001")
                        .start();
            }
        }
    }

    /**
     * 纠错
     */
    @OptionsItem(R.id.action_jiucuo)
    void onJiucuo() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.login_first),
                    Toast.LENGTH_SHORT).show();
            ActivityDispatcher.to_login(this);
        } else {
            MainEntJiucuoActivity_.intent(this).entId(entId).start();
        }
    }

    /**
     * 举报
     */
    @OptionsItem(R.id.action_jubao)
    void onJubao() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.login_first_then_message),
                    Toast.LENGTH_SHORT).show();
            ActivityDispatcher.to_login(this);
        } else {
            SuperviseActivity_.intent(this).entId(entId).start();
        }
    }

    /**
     * 显示企业二维码
     */
    @OptionsItem(R.id.action_showqrcode)
    void onshowQrcode() {
        if (homePageDTO == null) {
            return;
        }
        String entLogo = homePageDTO.getEntLogo() == null ? "" : homePageDTO
                .getEntLogo().getBig();
        String qrcodeContent = homePageDTO.getErCode();
        EntQrcodeActivity_
                .intent(MainEntActivity.this)
                .qrcodecontent(
                        TextUtils.isEmpty(qrcodeContent) ? "http://www.joblbs.com/"
                                : qrcodeContent).entLogo(entLogo)
                .entName(homePageDTO.getEntDetailDTO().getFullEntName())
                .start();
    }

    /**
     * 分享
     */
    @OptionsItem(R.id.action_share)
    void onShareEnt() {
        SystemServicesUtils.initShareSDK(this);
        if (shareContents == null) {
            findSharecontent();
        } else {
            // shareEnt();
            Share.showShare(this, shareContents, false, null);
        }
    }

    @Background
    void loadData() {
        enterpriseService
                .findEntHomePageDTO(
                        ZcdhApplication.getInstance().getZcdh_uid(), entId)
                .identify(
                        kREQ_ID_findEntHomePageDTO = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
     * 查找企业分享内容
     */
    @Background
    void findSharecontent() {
        enterpriseService.findEntShareDTO(entId).identify(
                kREQ_ID_findEntShareDTO = RequestChannel.getChannelUniqueID(),
                this);
    }

    /**
     * 取消或加入黑名单
     */
    @Background
    void updateUpdateBlack() {
        enterpriseService.updateJobBlack(
                ZcdhApplication.getInstance().getZcdh_uid(), entId).identify(
                kREQ_ID_updateJobBlack = RequestChannel.getChannelUniqueID(),
                this);
    }

    /**
     * 取消或关注
     */
    @Background
    void updateFoucs() {
        enterpriseService
                .updateAttentionEnt(
                        ZcdhApplication.getInstance().getZcdh_uid(), entId)
                .identify(
                        kREQ_ID_updateAttentionEnt = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
     * 显示主页信息
     */
    void showHomePage() {
        // Toast.makeText(this, "showHomePage", Toast.LENGTH_SHORT).show();
        // 企业名称
        entNameTxt.setText(homePageDTO.getEntDetailDTO().getFullEntName());
        if (homePageDTO.getEntDetailDTO().getIsLegalize() != null
                && homePageDTO.getEntDetailDTO().getIsLegalize() == 1) {
            entNameTxt.setVisibility(View.GONE);
            renzhengImg.setVisibility(View.VISIBLE);
        } else {
            entNameTxt.setVisibility(View.VISIBLE);
            renzhengImg.setVisibility(View.GONE);
        }

        // 企业所属行业
        enIndustryTxt.setText(homePageDTO.getEntDetailDTO().getIndustry());
        // 企业图标
        if (homePageDTO.getEntLogo() != null) {

            ImageLoader.getInstance().displayImage(
                    homePageDTO.getEntLogo().getBig(), entLogoImg, options);
        }
        // 黑名单
        if (menu != null) {
            if (homePageDTO.getIsAddBlack() == 1) {
                menu.findItem(R.id.action_blacklist).setTitle("取消黑名单");
            } else {
                menu.findItem(R.id.action_blacklist).setTitle("加入黑名单");
            }
        }
        // 是否关注
        if (homePageDTO.getIsAttention() == 1) {
            foucsTxt.setText("已关注");
            foucsImg.setVisibility(View.VISIBLE);
        } else {
            foucsTxt.setText("关注");
            foucsImg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {

        // 主页信息
        if (reqId.equals(kREQ_ID_findEntHomePageDTO)) {
            if (result != null) {
                homePageDTO = (EntHomePageDTO) result;
                showHomePage();
            }
        }

        // 黑名单
        if (reqId.equals(kREQ_ID_updateJobBlack)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 0) {
                    if (homePageDTO.getIsAddBlack() == 0) {
                        Toast.makeText(getApplicationContext(), "加入黑名单成功",
                                Toast.LENGTH_SHORT).show();
                        menu.findItem(R.id.action_blacklist).setTitle("已加入黑名单");
                        homePageDTO.setIsAddBlack(1);
                    } else if (homePageDTO.getIsAddBlack() == 1) {
                        Toast.makeText(getApplicationContext(), "已取消黑名单",
                                Toast.LENGTH_SHORT).show();
                        menu.findItem(R.id.action_blacklist).setTitle("加入黑名单");
                        homePageDTO.setIsAddBlack(0);
                    }
                }
            }
        }

        // 关注
        if (reqId.equals(kREQ_ID_updateAttentionEnt)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 0) {
                    if (homePageDTO.getIsAttention() == 0) {
                        Toast.makeText(getApplicationContext(), "关注成功",
                                Toast.LENGTH_SHORT).show();
                        foucsTxt.setText("已关注");
                        foucsImg.setVisibility(View.VISIBLE);
                        homePageDTO.setIsAttention(1);
                    } else if (homePageDTO.getIsAttention() == 1) {
                        Toast.makeText(getApplicationContext(), "已取消关注",
                                Toast.LENGTH_SHORT).show();
                        foucsTxt.setText("关注");
                        foucsImg.setVisibility(View.GONE);
                        homePageDTO.setIsAttention(0);
                    }

                }
            }
        }

        if (reqId.equals(kREQ_ID_findEntShareDTO)) {
            if (result != null) {
                shareContents = (List<JobEntShareDTO>) result;
                // shareEnt();
                Share.showShare(this, shareContents, false, null);
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

    }

    @Override
    public void onRequestError(String reqID, Exception error) {

    }

    @Override
    public void onExtraPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onExtraPageSelected(int i) {

    }

    @Override
    public void onExtraPageScrollStateChanged(int i) {

    }

    @OnActivityResult(Constants.REQUEST_CODE_COMMENT)
    void onCommentBack(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            commentFragment.refresh();
        }
    }

    @Override
    public void onPageSelected(int page) {
    }
}
