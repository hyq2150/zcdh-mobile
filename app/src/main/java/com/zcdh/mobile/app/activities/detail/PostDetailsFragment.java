package com.zcdh.mobile.app.activities.detail;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.internal.LoadingLayout.OnPullGetTextListener;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobPostService;
import com.zcdh.mobile.api.model.EntPostByOrderDTO;
import com.zcdh.mobile.api.model.JobEntOtherPostDTO;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobEntPostDetailDTO;
import com.zcdh.mobile.api.model.JobEntRelationPostDTO;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.api.model.JobEntTechniclRequireDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.ent.MainEntLiuyanActivity_;
import com.zcdh.mobile.app.activities.nearby.MatchDetailsActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.PostSwitcherView;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 职位详情页
 *
 * @author yangjiannan
 */
@SuppressWarnings("rawtypes")
@EFragment(R.layout.fragment_post_details)
public class PostDetailsFragment extends BaseFragment implements
        RequestListener, OnClickListener, Subscriber, OnRefreshListener2,
        OnPullGetTextListener, DataLoadInterface {

    // 查询职位详情ID
    private String kREQ_ID_findExtJobEntPostDetailDTO;

    private String KREQ_ID_FINDPOSTCONTENT;

    // 收藏
    private String kREQ_ID_updateFavorite;

    private static final String TAG = PostDetailsFragment.class.getSimpleName();

    @ViewById(R.id.switch_container)
    PostSwitcherView switch_container;

    @ViewById(R.id.emptyTipView)
    EmptyTipView emptyTipView;

    // ScrollView 容器
    @ViewById(R.id.contentScroll)
    ScrollView contentScroll;

    // 基本信息 ===========================
    // 常规全职职位信息
    @ViewById(R.id.postInfos)
    TableLayout postInfos;

    // 显示假期工/兼职的职位信息
    @ViewById(R.id.postInfos_1)
    TableLayout postInfos_1;

    /* ------------- 兼职 /假期工 --------------- */
    @ViewById(R.id.postNameText_1)
    TextView postNameText_1;

    @ViewById(R.id.publishDateTxt_1)
    TextView publishDateTxt_1;

    @ViewById(R.id.companyTxt_1)
    TextView companyTxt;

    @ViewById(R.id.addressTxt)
    TextView addressTxt;

    @ViewById(R.id.durationText_1)
    TextView durationText_1;

    @ViewById(R.id.zhuanyeTxt)
    TextView zhuanyeTxt;

    @ViewById(R.id.zhaopinTxt)
    TextView zhaopinTxt;

    @ViewById(R.id.daiyuTxt)
    TextView daiyuTxt;

    /* ------------- 全职 --------------------- */
    // 职位名称
    @ViewById(R.id.postNameText)
    TextView postNameText;

    // 招聘人数
    @ViewById(R.id.employCountText)
    TextView employCountText;

    // 企业名称
    @ViewById(R.id.entNameText)
    TextView entNameText;

    @ViewById(R.id.renzhengImg)
    ImageView renzhengImg;

    @ViewById(R.id.renzhengImg1)
    ImageView renzhengImg1;

    // 工作类型 (全职)
    @ViewById(R.id.jobCategoryText)
    TextView jobCategoryText;

    // 工作经验
    @ViewById(R.id.jingyangText)
    TextView jingyangText;

    // 学历
    @ViewById(R.id.degreeText)
    TextView degreeText;

    // 薪酬
    @ViewById(R.id.paymentText)
    TextView paymentText;

    // 工作地点
    @ViewById(R.id.areaText)
    TextView areaText;

    // 发布日期
    @ViewById(R.id.publishDateTxt)
    TextView publishDateTxt;

    // 查阅访问人数
    @ViewById(R.id.accessText)
    TextView accessText;

    // 已申请人数
    @ViewById(R.id.appliedCountTxt)
    TextView appliedCountTxt;

    /**
     * 职位匹配度
     */
    @ViewById(R.id.matchRatePb)
    ProgressBar matchRatePb;

    @ViewById(R.id.matchContainer)
    RelativeLayout matchContainer;

    /**
     * 匹配度
     */
    @ViewById(R.id.matchDescText)
    TextView matchDescText;

    // 职位标签
    @ViewById(R.id.tagsContainer)
    TagsContainer tagsContainer;

    // 职位描述 ===========================
    @ViewById(R.id.postDescription)
    TextView postDescription;

    // 技能 ===========================
    @ViewById(R.id.skillsLayout)
    TagsContainer skillsLayout;

    @ViewById(R.id.moreTagsBtn)
    ImageView moreTagsBtn;

    @ViewById(R.id.moreTagsText)
    TextView moreTagsText;

    // 其他职位 ===========================
    @ViewById(R.id.otherPostsGridView)
    GridView otherPostsGridView;

    @ViewById(R.id.moreBtn)
    ImageView moreBtn;

    @ViewById(R.id.moreBtnLl)
    LinearLayout moreBtnLl;

    @ViewById(R.id.tr_duration)
    LinearLayout tr_duration;

    @ViewById(R.id.duration)
    TextView duration;

    // 相关职位 ===========================
    // 相关职位1
    @ViewById(R.id.relaPostRow1)
    LinearLayout relaPostRow1;

    // 相关职位2
    @ViewById(R.id.relaPostRow2)
    LinearLayout relaPostRow2;

    // 相关职位3
    @ViewById(R.id.relaPostRow3)
    LinearLayout relaPostRow3;

    // 底部操作按钮===========================
    // 申请职位
    @ViewById(R.id.applyBtn)
    TextView applyBtn;

    // 收藏
    @ViewById(R.id.favriteBtn)
    TextView favriteBtn;

    // 分享
    @ViewById(R.id.shareBtn)
    TextView shareBtn;

    // 职位详情服务
    private IRpcJobPostService jobPostService;

    // 职位
    private JobEntPostDetailDTO detailDTO;

    // 保存在此页面的显示过的职位的id,用于返回到上一个职位
    private PredicateAdapter tagAdapter;

    private List<JobEntShareDTO> shareContents;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyEvents.register(this);
        jobPostService = RemoteServiceManager
                .getRemoteService(IRpcJobPostService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RemoteServiceManager.removeService(this);
        MyEvents.unregister(this);
        RequestChannel.removeRequestListener(kREQ_ID_findExtJobEntPostDetailDTO,
                kREQ_ID_updateFavorite, KREQ_ID_FINDPOSTCONTENT);
    }

    @AfterViews
    void bindViews() {
        // TODO
        switch_container.setMode(Mode.BOTH);
        switch_container.setmGetTextListener(this);
        switch_container.setOnRefreshListener(this);
        switch_container.setScrollingWhileRefreshingEnabled(true);

        loadData();
    }

	/* =================== UI 事件 ================= */

    // 其他职位，相关职位的详细信息

    // 公司其他职位
    @ItemClick(R.id.otherPostsGridView)
    void onItemClick(int position) {
        JobEntOtherPostDTO other = detailDTO.getEntOtherPostDTO().get(position);
        loadData(other.getPostId());
    }

    // 申请职位
    @Click(R.id.applyClickRl)
    void onApply() {

        if (detailDTO.getIsApplied() == 0) { // 0 未申请
            if (ZcdhApplication.getInstance().getZcdh_uid() > 0) {
                if (SystemServicesUtils.isCompleteInfo(getActivity())) {// 判断资料是否完善
                    // 申请职位
                    if (detailDTO.getPostStatus() != null) {
                        switch (detailDTO.getPostStatus()) {
                            case 0:
                                EntPostByOrderDTO postByOrderDto = new EntPostByOrderDTO();
                                postByOrderDto.setAreaName(detailDTO
                                        .getEntPostDTO().getPostAddress());
                                postByOrderDto.setEntName(detailDTO.getEntPostDTO()
                                        .getEntName());
                                postByOrderDto.setPostId(detailDTO.getEntPostDTO()
                                        .getPostId());
                                postByOrderDto.setPostName(detailDTO
                                        .getEntPostDTO().getPostAliases());
                                postByOrderDto.setWorkTime(DateUtils
                                        .getDateByFormatYMD(detailDTO
                                                .getEntPostDTO().getStartTime())
                                        + " ~ "
                                        + (DateUtils.getDateByFormatYMD(detailDTO
                                        .getEntPostDTO().getEndTime())));
                                ActivityDispatcher.toApplyPost(getActivity(),
                                        postByOrderDto);

                                break;
                            case 1:
                                break;
                            case 2:
                                PostApplyActivity_.intent(getActivity())
                                        .fairID(getActivity().getIntent().getExtras()
                                                .getLong("fairID"))
                                        .isFair(getActivity().getIntent().getExtras()
                                                .getBoolean("isFair"))
                                        .detailDTO(detailDTO).start();
                                break;
                            default:
                                break;
                        }
                    }
                }

            } else {
                LoginActivity_.intent(getActivity()).start();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.post_applied_msg),
                    Toast.LENGTH_SHORT).show();

        }
    }

    // 收藏
    @Click(R.id.favriteClickRl)
    void onFavorite() {
        if (ZcdhApplication.getInstance().getZcdh_uid() > 0) {
            jobPostService
                    .updateFavorite(
                            ZcdhApplication.getInstance().getZcdh_uid(),
                            detailDTO.getEntPostDTO().getPostId())
                    .identify(
                            kREQ_ID_updateFavorite = RequestChannel
                                    .getChannelUniqueID(),
                            this);
        } else {
            LoginActivity_.intent(getActivity()).start();
        }
    }

    // 岗位咨询
    @Click(R.id.zixunClickRl)
    void onZixunBtn() {
        if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.login_first),
                    Toast.LENGTH_SHORT).show();
            LoginActivity_.intent(getActivity()).start();
        } else {

            if (SystemServicesUtils.isCompleteInfo(getActivity())) {
                MainEntLiuyanActivity_.intent(this)
                        .type(Constants.kTYPE_ZIXUN)
                        .postId(detailDTO.getEntPostDTO().getPostId())
                        .postName(detailDTO.getEntPostDTO().getPostAliases())
                        .start();
            }
        }

    }

    @Click(R.id.moreTagsBtnLl)
    void onMoreTagsBtn(View v) {
        int tag = Integer.valueOf(v.getTag() + "");
        // 根据 tag 为0或1 判断是否展开: 0收起 1展开
        skillsLayout.removeAllViews();
        if (tag == 1) { // 变为展开
            moreTagsText.setText("收起");
            Display display = getActivity().getWindowManager()
                    .getDefaultDisplay();
            DisplayMetrics d = new DisplayMetrics();
            display.getMetrics(d);
            v.setTag(0);
            moreTagsBtn.setImageResource(R.drawable.icon_up);
            skillsLayout.initData(getActivity(), 0, tagAdapter, true);
        } else if (tag == 0) {// 变为收起
            moreTagsText.setText("展开");
            v.setTag(1);
            moreTagsBtn.setImageResource(R.drawable.xiabian_50x50);
            skillsLayout.initData(getActivity(), 0, tagAdapter, false);
        }
    }

    // 公司其他职位
    @Click(R.id.moreBtnLl)
    void onMoreBtn() {
        ((DetailsFrameActivity_) getActivity()).goesPager(2);
    }

    /**
     * 匹配度详情
     */
    @Click(R.id.matchContainer)
    void onMatchDetail() {
        // Toast.makeText(getActivity(), "match details",
        // Toast.LENGTH_SHORT).show();
        MatchDetailsActivity_.intent(getActivity())
                .postId(detailDTO.getEntPostDTO().getPostId())
                .matchRate(detailDTO.getEntPostDTO().getMatchRate()).start();
    }

	/* =================== 发起后端服务调用 ================= */

    public void loadData() {
        if (detailDTO == null) {
            long postId = ((DetailsFrameActivity_) getActivity()).getPostId();
            loadData(postId);
        }
    }

    // 发起查询详情信息
    @Background
    public void loadData(long postId) {
        // 职位详情
        jobPostService.findExtJobEntPostDetailDTO(
                getActivity().getIntent().getExtras().getLong("fairID"),
                ZcdhApplication.getInstance().getZcdh_uid(), postId)
                .identify(kREQ_ID_findExtJobEntPostDetailDTO = RequestChannel.getChannelUniqueID(),
                        this);
    }

    // 查询出详情信息后显示到页面
    void showData() {

        if (detailDTO != null) {
            // 将职位相关的entId 回传给 父Activity，共享到 公司主页和更多职位 页面使用
            // ((DetailsFrameActivity_) getActivity()).entId =
            // detailDTO.getEntPostDTO().getEntId();
            MyEvents.post(Constants.kMSG_ENT_CHANGED, detailDTO
                    .getEntPostDTO().getEntId());
            // 职位信息
            JobEntPostDTO post = detailDTO.getEntPostDTO();
            if (post == null) {
                return;
            }

            // 其他
            List<JobEntOtherPostDTO> otherPosts = detailDTO
                    .getEntOtherPostDTO();
            // 相关职位
            List<JobEntRelationPostDTO> relationPosts = detailDTO
                    .getEntRelationPostDTO();
            // 技能要求
            List<JobEntTechniclRequireDTO> skills = detailDTO
                    .getTechniclRquire();

            // 设置标题：以企业名称为title
            SystemServicesUtils.setActionBarCustomTitle(getActivity(),
                    ((AppCompatActivity) getActivity()).getSupportActionBar(), post.getEntName());

            // 显示职位基本信息
            String property = post.getPostPropertyCode();
            if (Constants.POST_PROPERTY_JIANZHI.equals(property) // 显示假期工/兼职信息
                    || Constants.POST_PROPERTY_JIAQI.equals(property)) {
                postInfos.setVisibility(View.GONE);
                postInfos_1.setVisibility(View.VISIBLE);

                postNameText_1.setText(post.getPostAliases());
                if (post.getIsLegalize() != null && post.getIsLegalize() == 1) {
                    renzhengImg1.setVisibility(View.VISIBLE);
                } else {
                    renzhengImg1.setVisibility(View.GONE);
                }

                companyTxt.setText(post.getFullEntName());
                addressTxt.setText(post.getPostAddress());
                durationText_1.setText(TextUtils.isEmpty(detailDTO
                        .getEntPostDTO().getWorkDate()) ? "不限" : detailDTO
                        .getEntPostDTO().getWorkDate());

                zhuanyeTxt
                        .setText(TextUtils.isEmpty(post.getMajorName()) ? "专业不限"
                                : post.getMajorName());

                zhaopinTxt.setText(post.getEmployCount() + "");

                daiyuTxt.setText(post.getSalary());
            } else { // 显示全职
                postInfos.setVisibility(View.VISIBLE);
                postInfos_1.setVisibility(View.GONE);
                postNameText.setText(post.getPostAliases());
                if (post.getEmployCount() <= 0) {
                    employCountText.setVisibility(View.GONE);
                } else {
                    employCountText.setVisibility(View.VISIBLE);

                }
                // 工作类型
                if (!StringUtils.isBlank(post.getJobCategory())) {
                    jobCategoryText
                            .setText(Html
                                    .fromHtml("<b>类型</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                            + post.getJobCategory()));
                }

                // 工作经验
                if (!StringUtils.isBlank(post.getWorkExperience())) {
                    jingyangText
                            .setText(Html
                                    .fromHtml("<b>经验</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                            + post.getWorkExperience()));
                }

                employCountText.setText(Html.fromHtml("<b>"
                        + getString(R.string.employ_count)
                        + "</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + post.getEmployCountStr()));
                entNameText
                        .setText(Html
                                .fromHtml(post.getFullEntName()));
                if (post.getIsLegalize() != null && post.getIsLegalize() == 1) {
                    renzhengImg.setVisibility(View.VISIBLE);
                } else {
                    renzhengImg.setVisibility(View.GONE);
                }
                if (post.getIsLegalize() != null && post.getIsLegalize() == 1) {

                }
                if (!StringUtils.isBlank(post.getDegree())) {
                    degreeText
                            .setText(Html
                                    .fromHtml("<b>学历</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                            + post.getDegree()));
                } else {
                    degreeText
                            .setText(Html
                                    .fromHtml("<b>学历</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                            + getString(R.string.no_degree)));
                }
                paymentText
                        .setText(Html
                                .fromHtml(
                                        "<b>薪资</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='red'>"
                                                + post.getSalary() + "</font>"));
                areaText.setText(Html
                        .fromHtml("<b>地点</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + post.getAreaName()));
                publishDateTxt.setVisibility(View.VISIBLE);
                publishDateTxt.setText(new SimpleDateFormat("yyy-MM-dd")
                        .format(post.getPublishDate()));
                accessText.setText(Html.fromHtml("<b>"
                        + getString(R.string.access_count)
                        + "</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + detailDTO.getVisitCount()));
                if (detailDTO.getVisitCount() <= 0) {
                    accessText.setVisibility(View.GONE);
                } else {
                    accessText.setVisibility(View.VISIBLE);

                }
                appliedCountTxt.setText(Html.fromHtml("<b>"
                        + getString(R.string.apply_count)
                        + "</b>&nbsp;&nbsp;&nbsp;" + post.getApplyCount()));
            }

            // 显示匹配度
            if (post.getMatchRate() > 0) {
                matchContainer.setVisibility(View.VISIBLE);
                showMatchProgressAnimation();
            }

            // Toast.makeText(getActivity(), "post tags:" + post.getTagNames(),
            // Toast.LENGTH_SHORT).show();
            // 显示职位标签
            if (post.getTagNames() != null && post.getTagNames().size() > 0) {
                tagsContainer.removeAllViews();
                tagsContainer.initData(post.getTagNames(), null, Gravity.LEFT);

            }

            // 岗位描述(岗位要求)
            postDescription.setText(detailDTO.getPostRequire());
            Log.i("岗位描述", detailDTO.getPostRequire());

            // 技能要求
            if (skills != null) {
                // skillsLayout.setAdapter(new SkillsTagAdapter(skills));
                skillsLayout.removeAllViews();
                tagAdapter = new SkillsTagAdapter(skills);
                skillsLayout.initData(getActivity(), 0, tagAdapter, false);
            }

            // 其他职位
            if (otherPosts != null && otherPosts.size() > 0) {
                otherPostsGridView
                        .setAdapter(new OtherPostsAdapter(otherPosts));
                if (otherPosts.size() >= 3) {
                    moreBtnLl.setVisibility(View.VISIBLE);
                } else {
                    moreBtnLl.setVisibility(View.GONE);
                }
            }

            // 相关职位推荐
            if (relationPosts != null && relationPosts.size() > 0) {
                for (int i = 0; i < relationPosts.size(); i++) {
                    JobEntRelationPostDTO relationPost = relationPosts.get(i);
                    if (i == 0) {
                        TextView post1 = (TextView) (relaPostRow1
                                .findViewById(R.id.relaPostName1));
                        TextView ent1 = (TextView) (relaPostRow1
                                .findViewById(R.id.relaEnt1));
                        post1.setText(relationPost.getPostName());
                        ent1.setText(relationPost.getEntName());
                        relaPostRow1.setVisibility(View.VISIBLE);
                        relaPostRow1.setOnClickListener(this);
                        relaPostRow1.setTag(relationPost);
                    }

                    if (i == 1) {
                        TextView post2 = (TextView) (relaPostRow2
                                .findViewById(R.id.relaPostName2));
                        TextView ent2 = (TextView) (relaPostRow2
                                .findViewById(R.id.relaEnt2));
                        post2.setText(relationPost.getPostName());
                        ent2.setText(relationPost.getEntName());
                        relaPostRow2.setVisibility(View.VISIBLE);
                        relaPostRow2.setOnClickListener(this);
                        relaPostRow2.setTag(relationPost);
                    }
                    if (i == 2) {
                        TextView post3 = (TextView) (relaPostRow3
                                .findViewById(R.id.relaPostName3));
                        TextView ent3 = (TextView) (relaPostRow3
                                .findViewById(R.id.relaEnt3));
                        post3.setText(relationPost.getPostName());
                        ent3.setText(relationPost.getEntName());
                        relaPostRow3.setVisibility(View.VISIBLE);
                        relaPostRow3.setOnClickListener(this);
                        relaPostRow3.setTag(relationPost);
                    }
                }
            } else {
                // hidden
            }
        }

        // 收藏与未收藏
        if (detailDTO.getIsFavorite() == 0) {
            favriteBtn.setText(getString(R.string.favorites));
        } else if (detailDTO.getIsFavorite() == 1) {
            favriteBtn.setText("取消收藏");

        }

        // 已申请与未申请
        if (detailDTO.getIsApplied() == 0) {
            applyBtn.setText(getString(R.string.post_apply));
        } else if (detailDTO.getIsApplied() == 1) {
            applyBtn.setText(getString(R.string.post_applied));

        }

        if (detailDTO.getPostStatus() != 2) {
            tr_duration.setVisibility(View.VISIBLE);
            duration.setText(String.format("%s~%s", DateUtils
                    .getDateByFormatYMD(detailDTO.getEntPostDTO()
                            .getStartTime()), DateUtils
                    .getDateByFormatYMD(detailDTO.getEntPostDTO().getEndTime())));
        } else {
            tr_duration.setVisibility(View.GONE);
        }

        // 让ScrollView 滚到顶部
        contentScroll.fullScroll(View.FOCUS_UP);
        switch_container.getRefreshableView().fullScroll(View.FOCUS_UP);
    }

    /**
     * 动画显示匹配度
     */
    void showMatchProgressAnimation() {
        final int matchRate = detailDTO.getEntPostDTO().getMatchRate();
        matchDescText.setText(matchRate + "%");
        if (matchRate > 0) {
            Runnable updateProgressRunable = new Runnable() {

                @Override
                public void run() {
                    for (int i = 1; i <= matchRate; i++) {
                        updateProgress(i);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            new Thread(updateProgressRunable).start();
        }
    }

    @UiThread
    void updateProgress(int progress) {
        matchRatePb.setProgress(progress);
    }

    /* =================== 后端服务回调 ================= */
    @Override
    public void onRequestStart(String reqId) {
        Toast.makeText(getActivity(), "start", Toast.LENGTH_SHORT).show();
        if (reqId.equals(kREQ_ID_findExtJobEntPostDetailDTO)) {
            emptyTipView.startLoadingAnim();
            contentScroll.setVisibility(View.GONE);
            SystemServicesUtils.setActionBarCustomTitle(getActivity(),
                    ((AppCompatActivity) getActivity()).getSupportActionBar(), "正在加载...");
        }
    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        // 职位详情
        if (reqId.equals(kREQ_ID_findExtJobEntPostDetailDTO)) {
            if (result != null) {
                detailDTO = (JobEntPostDetailDTO) result;
                showData();
                contentScroll.setVisibility(View.VISIBLE);
                emptyTipView.isEmpty(false);
            } else {
                ToastUtil.show(R.string.str_no_related_post_details);
            }
        }

        // 收藏
        if (reqId.equals(kREQ_ID_updateFavorite)) {
            int favorite = -1;
            if (result != null) {
                favorite = (int) result;
            }
            String msg = "";
            if (favorite == 0) { // 收藏成功
                if (detailDTO.getIsFavorite() == 1) {
                    msg = "取消收藏成功";
                    detailDTO.setIsFavorite(0);
                    favriteBtn.setText(getString(R.string.favorites));
                    ((DetailsFrameActivity) getActivity()).cancelFavorite = true;
                } else if (detailDTO.getIsFavorite() == 0) {
                    msg = "收藏成功";
                    detailDTO.setIsFavorite(1);
                    favriteBtn.setText("取消收藏");
                    ((DetailsFrameActivity) getActivity()).cancelFavorite = false;
                }
            }
            if (favorite == 1) {// 收藏失败
                msg = "收藏失败";
            }
            ToastUtil.show(msg);
        }

        if (reqId.equals(KREQ_ID_FINDPOSTCONTENT)) {
            shareContents = (List<JobEntShareDTO>) result;
            Share.showShare(getActivity(), shareContents, false, null);
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

        // loading.dismiss();
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        emptyTipView.showException((ZcdhException) error, this);
        if (error != null) {
            Log.e(TAG, error.getStackTrace() + "");
        }
        SystemServicesUtils.setActionBarCustomTitle(getActivity(),
                ((AppCompatActivity) getActivity()).getSupportActionBar(), "加载失败");
    }

	/* =================== 适配器 ================= */

    /**
     * 技能要求
     *
     * @author yangjiannan
     */
    class SkillsTagAdapter extends PredicateAdapter {

        private List<JobEntTechniclRequireDTO> skills;

        public SkillsTagAdapter(List<JobEntTechniclRequireDTO> skills) {
            this.skills = skills;
        }

        @Override
        public View getView(int position, ViewGroup parentView) {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.skill_tags_item, null);
            TextView skillLevelText = (TextView) view
                    .findViewById(R.id.skillLevelText);
            TextView skView = (TextView) view.findViewById(R.id.skillNameText);

            JobEntTechniclRequireDTO skill = skills.get(position);
            skView.setText(skill.getTechniclName());

            skillLevelText.setText(skill.getParamName());

            if ("精".equals(skill.getParamName())) {// 精通
                skillLevelText.setText("精");
                skillLevelText.setTextColor(getResources().getColor(
                        R.color.tag_jingtong1));
                skillLevelText
                        .setBackgroundResource(R.drawable.skill_jingtong_background);
            }
            if ("熟".equals(skill.getParamName())) {// 熟悉
                skillLevelText.setText("熟");
                skillLevelText.setTextColor(getResources().getColor(
                        R.color.tag_shu));

                skillLevelText
                        .setBackgroundResource(R.drawable.skill_shuxi_background);
            }
            if ("会".equals(skill.getParamName())) {// 了解
                skillLevelText.setText("会");
                skillLevelText.setTextColor(getResources().getColor(
                        R.color.tag_know));

                skillLevelText
                        .setBackgroundResource(R.drawable.skill_know_background);
            }
            if ("知".equals(skill.getParamName())) { // /知道
                skillLevelText.setText("知");
                skillLevelText.setTextColor(getResources().getColor(
                        R.color.tag_zhidao));

                skillLevelText
                        .setBackgroundResource(R.drawable.skill_zhidao_background);
            }
            if ("良".equals(skill.getParamCode())) {
                skillLevelText.setText("良");
                skillLevelText.setTextColor(getResources().getColor(
                        R.color.tag_jingtong1));
                skillLevelText
                        .setBackgroundResource(R.drawable.skill_jingtong_background);
            }

            return view;
        }

        @Override
        public int getCount() {
            return skills.size();
        }

        @Override
        public void setLayout(ViewGroup parentView) {
            int unit = 40; // UnitTransfer.dip2px(getActivity(), 40);
            int height = skills.size() * unit;

            parentView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, height));
        }

        @Override
        public int getMarginOffset() {
            return 0;
        }

    }

    /**
     * 其他职位
     *
     * @author yangjiannan
     */
    class OtherPostsAdapter extends BaseAdapter {

        List<JobEntOtherPostDTO> others;

        public OtherPostsAdapter(List<JobEntOtherPostDTO> others) {
            this.others = others;
        }

        @Override
        public int getCount() {
            if (others != null && others.size() <= 3) {
                return others.size();
            }
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return others.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.post_other_item, parent, false);
            }
            TextView item = (TextView) convertView.findViewById(R.id.itemNameText);
            item.setText(others.get(position).getPostName());
            return convertView;
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            postNameText.setText("");
            JobEntRelationPostDTO relationPost = (JobEntRelationPostDTO) v.getTag();
            loadData(relationPost.getPostId());
            //根据职位所在的企业刷新企业详情页面
            DetailsFrameActivity host = (DetailsFrameActivity) getActivity();
            host.refresh(relationPost.getEntId());
        }
    }

    // 收到申请成功的消息
    @Override
    public void receive(String key, Object msg) {

        if (PostApplyActivity.kMSG_APPLY_STATUS.equals(key)) {
            if ((Integer) msg == 1) {
                detailDTO.setIsApplied(1);
            }
            applyBtn.setText(getString(R.string.post_applied));
        }

        // 更多职位列表传递过来的post_id
        if (Constants.kMSG_POST_ID.equals(key)) {
            if (msg != null) {
                long postId = (Long) msg;
                loadData(postId);
            }
        }
    }

    @Click(R.id.shareClickRl)
    void onShare() {
        SystemServicesUtils.initShareSDK(getActivity());
        if (shareContents == null) {
            findSharecontent();
        } else {
            Share.showShare(getActivity(), shareContents, false, null);
        }
    }

    @Background
    void findSharecontent() {
        jobPostService.findPostShareContent(getUserId(),
                detailDTO.getEntPostDTO().getPostId()).identify(
                KREQ_ID_FINDPOSTCONTENT = RequestChannel.getChannelUniqueID(),
                this);
    }


    @Override
    public void onDestroyView() {
        SystemServicesUtils.stopShareSDK(getActivity());
        super.onDestroyView();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        // 上一条岗位数据
        // TODO
        switch_container.onRefreshComplete();
        DetailsFrameActivity host = (DetailsFrameActivity) getActivity();
        if (host.switchable()) {
            if (host.getCurrentIndex() <= 0) {
                ToastUtil.show(R.string.str_latest_item);
            } else {
                contentScroll.setVisibility(View.GONE);
                emptyTipView.startLoadingAnim();
                host.setCurrentIndex(host.getCurrentIndex() - 1);
                loadData(host.getPosts().get(host.getCurrentIndex())
                        .getPostId());
                //根据职位所在的企业刷新企业详情页面
                host.refresh(host.getPosts().get(host.getCurrentIndex()).getEntId());
            }

        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        // 下一条数据
        // TODO
        switch_container.onRefreshComplete();
        DetailsFrameActivity host = (DetailsFrameActivity) getActivity();
        if (host.switchable()) {
            if (host.getPosts() == null || host.getCurrentIndex() >= host.getPosts().size() - 1) {
                ToastUtil.show(R.string.str_last_item);
            } else {
                contentScroll.setVisibility(View.GONE);
                emptyTipView.startLoadingAnim();
                host.setCurrentIndex(host.getCurrentIndex() + 1);
                loadData(host.getPosts().get(host.getCurrentIndex())
                        .getPostId());
                //根据职位所在的企业刷新企业详情页面
                List<JobEntPostDTO> posts = host.getPosts();
                JobEntPostDTO post = posts.get(host.getCurrentIndex());
                host.refresh(post.getEntId());

            }

        }
    }

    @Override
    public String getText(Mode mode) {
        DetailsFrameActivity activity = (DetailsFrameActivity) getActivity();

        switch (mode) {
            case PULL_FROM_START:
                if (activity.switchable()) {
                    if (activity.getCurrentIndex() > 0) {
                        return activity.getPosts().get(activity.getCurrentIndex() - 1)
                                .getPostAliases();
                    }
                }
                break;
            case PULL_FROM_END:
                if (activity.switchable()) {
                    if (activity.getPosts() != null) {
                        if (activity.getCurrentIndex() < activity.getPosts().size() - 1) {
                            return activity.getPosts().get(activity.getCurrentIndex() + 1)
                                    .getPostAliases();
                        }
                    }
                }
                break;
            default:
                break;
        }
        return "";
    }

}
