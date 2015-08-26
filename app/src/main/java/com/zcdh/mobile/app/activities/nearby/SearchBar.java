package com.zcdh.mobile.app.activities.nearby;

import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcCUCCSpecialService;
import com.zcdh.mobile.api.IRpcJobSearchService;
import com.zcdh.mobile.api.model.JobSearchTagDTO;
import com.zcdh.mobile.api.model.JobTagTypeDTO;
import com.zcdh.mobile.api.model.MoreToolsDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.framework.activities.FWSpeechRecognizerActivity;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.CustomIndicator;
import com.zcdh.mobile.utils.StringUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.BackgroundExecutor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * @author yangjiannan
 * 
 *         搜索 bar，支持一下输入 1)语音识别，语义分析搜索职位 2)输入关键字搜索职位 3)选择标签搜索职位
 */
@EBean
public class SearchBar implements RequestListener, Subscriber, OnGestureListener, OnClickListener, OnItemClickListener,
		OnLongClickListener, VoiceSearchGestureListnner, VoiceSearchDialogListnner {

	protected static final String TAG = SearchBar.class.getSimpleName();

	// 查找标签分类服务标识
	private String kREQ_ID_FINDJOBTAGTYPEDTO; // findJobTagTypeDTO

	// 分页查询标签
	private String kREQ_ID_findJobSearchTagDTOByPage;

	/**
	 * 更多功能
	 */
	private String kREQ_ID_findMoreTools;

	private IRpcJobSearchService jobSearchService;

	private IRpcCUCCSpecialService cuccSpecialService;

	// 操作回调监听
	private SearchBarListener barListener;

	// 宿主Activity
	private Activity context;

	/**
	 * bar 容器
	 */
	private View barContainer;

	/**
	 * 语音搜索框
	 */
	private VoiceSearchDialog voiceSearchDialog;

	/**
	 * 关键字搜索输入框
	 */
	private EditText keyWorkEditText;

	private FrameLayout keywordContainer;

	/**
	 * 切换到职位列表
	 */
	private ImageButton switchModeBtn;

	/**
	 * 搜索按钮
	 */
	private Button searchBtn;

	/**
	 * 标签按钮
	 */
	private ImageButton tagToggleBtn;

	/**
	 * 键盘按钮
	 */
	private ImageButton keybordBtn;

	/**
	 * 语音按钮
	 */
	private ImageButton micBtn;

	/**
	 * 长按喊工作
	 */
	private Button voiceSearchBtn;

	/**
	 * 处理语音手势事件
	 */
	private VoiceSearchGestureProccessor voiceSearchGestureProccessor;

	/**
	 * 更多功能按钮
	 */
	private ImageButton moreBtn;

	/**
	 * 更多功能
	 */
	private GridView moreFuncGridView;

	private MoreFuncAdapter moreFuncAdapter;

	/**
	 * 切换标签的手势识别
	 */
	private GestureDetector gd;

	/**
	 * 标签容器
	 */
	private RelativeLayout tagsContainerRl;

	/**
	 * 显示标签的GridView
	 * 
	 */
	private GridView tagsGrideview;
	private TagsGrideViewAdapter tagsAdapter;

	/**
	 *  
	 */
	private CustomIndicator indicator;

	/**
	 * 标签类别1
	 */
	private Button tagCateogeryBtn1;
	/**
	 * 标签类别2
	 */
	private Button tagCateogeryBtn2;
	/**
	 * 标签类别3
	 */
	private Button tagCateogeryBtn3;

	/**
	 * 显示标签和更多功能的容器
	 */
	private RelativeLayout relContentRl;

	/**
	 * 更多功能面板
	 */
	private RelativeLayout moreFuncRl;

	/**
	 * 标签当前页
	 */
	private Integer currentTagstPage = 1;

	/**
	 * 标签每页大小
	 */
	private Integer tagPageSize = 8;

	/**
	 * 当前标签类别的标签最大页数
	 */
	private int maxTagsPage;

	/**
	 * 当前选择的标签类别
	 */
	private String selectedTagCategory;

	// 标签类别
	private List<JobTagTypeDTO> jobTagTypes;

	// 标签数据
	private HashMap<String, List<JobSearchTagDTO>> tags = new HashMap<String, List<JobSearchTagDTO>>();

	/**
	 * 更多功能
	 */
	private List<MoreToolsDTO> moreFuncs = new ArrayList<MoreToolsDTO>();

	// 是否显示标签
	private boolean tagsShow;
	// 是否显示更多功能
	private boolean funsShow;

	// /**
	// * 计算更多功能，每项的宽度
	// */
	// private int funcItemWidth;

	// 是否加载标签
	private boolean isLoadTags = false;
	// 是否加载更多功能数据
	private boolean isLoadFuns = false;

	public void initBar(SearchBarListener barListener, Activity context, View barContainer) {
		this.barContainer = barContainer;
		this.context = context;
		this.barListener = barListener;
		jobSearchService = RemoteServiceManager.getRemoteService(IRpcJobSearchService.class);

		cuccSpecialService = RemoteServiceManager.getRemoteService(IRpcCUCCSpecialService.class);

		// 初始化Views
		bindViews();
	}

	public ImageButton getSwitchModeBtn() {
		return switchModeBtn;
	}

	/**
	 * 初始化绑定View
	 */
	private void bindViews() {

		barContainer = (LinearLayout) barContainer.findViewById(R.id.barContainer);
		keywordContainer = (FrameLayout) barContainer.findViewById(R.id.keywordContainer);
		keyWorkEditText = (EditText) barContainer.findViewById(R.id.keyWorkEditText);
		searchBtn = (Button) barContainer.findViewById(R.id.searchBtn);
		tagToggleBtn = (ImageButton) barContainer.findViewById(R.id.tagToggleBtn);
		micBtn = (ImageButton) barContainer.findViewById(R.id.micBtn);
		keybordBtn = (ImageButton) barContainer.findViewById(R.id.keybordBtn);
		voiceSearchBtn = (Button) barContainer.findViewById(R.id.voiceSearchBtn);
		switchModeBtn = (ImageButton) barContainer.findViewById(R.id.switchModeBtn);

		moreBtn = (ImageButton) barContainer.findViewById(R.id.moreBtn);
		moreFuncGridView = (GridView) barContainer.findViewById(R.id.moreFuncGridView);
		moreFuncAdapter = new MoreFuncAdapter();
		moreFuncGridView.setAdapter(moreFuncAdapter);
		moreFuncGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= moreFuncs.size()) {
					if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
						Toast.makeText(context, context.getResources().getString(R.string.login_first),
								Toast.LENGTH_SHORT).show();
						ActivityDispatcher.to_login(context);
					} else {
						HashMap<Long, MoreToolsDTO> selectedFuncs = new HashMap<Long, MoreToolsDTO>();
						if (moreFuncs != null && moreFuncs.size() > 0) {
							for (int i = 0; i < moreFuncs.size(); i++) {
								selectedFuncs.put(moreFuncs.get(i).getId(), moreFuncs.get(i));
							}
						}
						AddMoreFuncActivity_.intent(context).selectedFuncs(selectedFuncs)
								.startForResult(AddMoreFuncActivity.kREQUEST_ADD_FUNCS);
					}
				} else {
					MoreToolsDTO dto = moreFuncs.get(position);
					if (dto.getOpenType() == 2) {
						if (!StringUtils.isBlank(dto.getAndroidURL())) {
							try {

								Class activityClazz = Class.forName(dto.getAndroidURL());
								Intent intent = new Intent(context, activityClazz);
								if (!TextUtils.isEmpty(dto.getCustomParam())) {
									for (Entry<String, String> param : StringUtils.getParams(dto.getCustomParam())
											.entrySet()) {
										intent.putExtra(param.getKey(), param.getValue());
									}
								}
								Log.i(TAG, dto.getCustomParam() + "");
								intent.putExtra("index", position);
								context.startActivity(intent);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} finally {
								showOrHiddenSearchBar();
							}
						} else {
							Toast.makeText(context, "敬请期待...", Toast.LENGTH_SHORT).show();
						}
					}
					if (dto.getOpenType() == 1) {
						Bundle data = new Bundle();
						data.putString("isShowTitle", dto.getIsShowTitle() + "");
						NewsBrowserActivity_.IntentBuilder_ ib = NewsBrowserActivity_
							.intent(context);
						ib.get().putExtras(data);
						ib.title(dto.getToolName());
						ib.url(dto.getAndroidURL()).start();
					}
				}
			}
		});

		tagsContainerRl = (RelativeLayout) barContainer.findViewById(R.id.tagsContainerRl);
		tagsGrideview = (GridView) barContainer.findViewById(R.id.tagsGrideview);
		indicator = (CustomIndicator) barContainer.findViewById(R.id.indicator);
		tagCateogeryBtn1 = (Button) barContainer.findViewById(R.id.tagCategoryBtn1);
		tagCateogeryBtn2 = (Button) barContainer.findViewById(R.id.tagCategoryBtn2);
		tagCateogeryBtn3 = (Button) barContainer.findViewById(R.id.tagCategoryBtn3);
		relContentRl = (RelativeLayout) barContainer.findViewById(R.id.relContentRl);
		moreFuncRl = (RelativeLayout) barContainer.findViewById(R.id.moreFunContainer);

		// 设置事件
		keyWorkEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				onFocusChangedKeywordEditText(v, hasFocus);
			}
		});
		keyWorkEditText.setOnClickListener(this);
		keyWorkEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				onKeyWordChange(text, before, start, count);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		switchModeBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		tagToggleBtn.setOnClickListener(this);
		micBtn.setOnClickListener(this);
		keybordBtn.setOnClickListener(this);
		moreBtn.setOnClickListener(this);
		tagCateogeryBtn1.setOnClickListener(this);
		tagCateogeryBtn2.setOnClickListener(this);
		tagCateogeryBtn3.setOnClickListener(this);
		tagsGrideview.setOnItemClickListener(this);

		voiceSearchBtn.setOnLongClickListener(this);

		// 初始好手势识别
		// 5_ 手势识别
		gd = new GestureDetector(context, this);
		// 设置标签GridView 的手势事件监听
		tagsGrideview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return gd.onTouchEvent(event);
			}
		});

		barContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return gd.onTouchEvent(event);
			}
		});

		// 6_ 设置标签
		tagsAdapter = new TagsGrideViewAdapter();
		tagsGrideview.setAdapter(tagsAdapter);

		// 7 _ 预加载数据（标签、..）
		// loadTagTypes();

		// 8 _ 语音搜索（喊工作）
		voiceSearchDialog = new VoiceSearchDialog(context, R.style.Theme_VoiceSearchDialog, this);

		// 9_ 更多功能
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		// funcItemWidth = (dm.widthPixels - 5 * 8) / 4;
		// Toast.makeText(context, funcItemWidth + "",
		// Toast.LENGTH_SHORT).show();
		// loadMoreFuncs();
	}

	/* ================== 加载后端数据 ================ */
	/**
	 * 加载标签
	 */
	void loadTagTypes() {

		BackgroundExecutor.execute(new BackgroundExecutor.Task(null, 0, null) {

			@Override
			public void execute() {

				jobSearchService.findJobTagTypeDTO()
						.identify(kREQ_ID_FINDJOBTAGTYPEDTO = RequestChannel.getChannelUniqueID(), SearchBar.this);
			}
		});

	}

	/**
	 * 默认先加载第一个标签分类
	 * 
	 */
	void loadTags(final String typeCode) {
		selectedTagCategory = typeCode;
		BackgroundExecutor.execute(new BackgroundExecutor.Task(null, 0, null) {

			@Override
			public void execute() {

				jobSearchService.findJobSearchTagDTOByPage(typeCode, currentTagstPage, tagPageSize).identify(
						kREQ_ID_findJobSearchTagDTOByPage = RequestChannel.getChannelUniqueID(), SearchBar.this);

			}
		});

	}

	/**
	 * 加载更多功能数据
	 */
	@Background
	void loadMoreFuncs() {
		LatLng center = ZcdhApplication.getInstance().getMyLocation();
		cuccSpecialService
				.findMoreTools1(ZcdhApplication.getInstance().getZcdh_uid(), center.longitude, center.latitude, 1, 100)
				.identify(kREQ_ID_findMoreTools = RequestChannel.getChannelUniqueID(), this);
	}

	/* ================== 接收订阅的消息 ============= */
	@Override
	public void receive(String key, Object msg) {
		/*
		 * 接收语音消息
		 */
		if (key.equals(FWSpeechRecognizerActivity.kEVENT_SPEECH_RESULT) && msg != null) {
			// Toast.makeText(getActivity(), msg + "",
			// Toast.LENGTH_SHORT).show();
			if (!StringUtils.isBlank(msg + "")) {
				this.keyWorkEditText.setText(msg + "");
			} else {

			}
		}
	}

	/* ============= 语音识别（喊工作）手势操作 =================== */

	@Override
	public void onWillVoice(MotionEvent event) {
		voiceSearchDialog.beginVoiceTheme();
		// Log.i("onWillVoice ........", "yes !!!!");
	}

	@Override
	public void onWillCancel(MotionEvent event) {
		voiceSearchDialog.endVoiceTheme();
		// Log.i("onWillCancel ........", "yes !!!!");

	}

	@Override
	public void onCancel(MotionEvent event) {
		voiceSearchDialog.cancel();
		voiceSearchBtn.setOnTouchListener(null);
		Log.i("onCancel ........", "yes !!!!");
	}

	public void onVoiceEnd(MotionEvent event) {
		// Toast.makeText(context, "onVoiceEnd", Toast.LENGTH_SHORT).show();
		String sayContent = voiceSearchDialog.getSayContent();
		if (!StringUtils.isBlank(sayContent)) {

			// 松手时，如果已经识别出了内容，取消继续监听识别
			voiceSearchDialog.cancel();
			//
			barListener.onVoiceResult(sayContent);
		}
		voiceSearchDialog.endVoice();
		barListener.onVoiceEnd();
		voiceSearchBtn.setOnTouchListener(null);
	}

	/**
	 * 语音识别结束，返回结果
	 */
	public void onVoiceResult(String sayContent) {
		barListener.onVoiceResult(sayContent);
		voiceSearchBtn.setOnTouchListener(null);
	}

	/* ===================== 手势识别 ====================== */

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		final int SWIPE_THRESHOLD = 100;
		final int SWIPE_VELOCITY_THRESHOLD = 100;
		boolean result = false;
		try {
			float diffY = e2.getY() - e1.getY();
			float diffX = e2.getX() - e1.getX();
			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffX > 0) {
						onSwipeRigth();
					} else {
						onSwipeLeft();
					}
				}
			} else {
				if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						// onSwipeBottom();
					} else {
						// onSwipeTop();
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		onItemClickForTags(position);
	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.voiceSearchBtn) {
			voiceSearchBtn.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					if (voiceSearchGestureProccessor == null) {
						voiceSearchGestureProccessor = new VoiceSearchGestureProccessor(SearchBar.this);
					}
					voiceSearchGestureProccessor.proccess(event);
					return false;
				}
			});
			voiceSearchDialog.show();
			MobclickAgent.onEvent(context, Constants.UMENG_VOICE_BUTTON_ACTION);
		}
		return false;
	}

	public void onClick(View view) {
		if (view.getId() == R.id.tagToggleBtn) {
			onTagToggleBtn();
		}
		if (view.getId() == R.id.moreBtn) {
			onMoreBtn();
		}
		if (view.getId() == R.id.keybordBtn) {
			onKeyboardBtn();
		}
		if (view.getId() == R.id.micBtn) {
			onMicBtn();
		}
		if (view.getId() == R.id.switchModeBtn) {
			onSwitchListViewBtn();
		}
		if (view.getId() == R.id.searchBtn) {
			onSearchBtn();
		}
		if (view.getId() == R.id.tagCategoryBtn1) {
			onTagCategoryBtn1();
		}
		if (view.getId() == R.id.tagCategoryBtn2) {
			onTagCategoryBtn2();
		}
		if (view.getId() == R.id.tagCategoryBtn3) {
			onTagCategoryBtn3();
		}
	}

	/***
	 * 展开标签按钮
	 */
	private void onTagToggleBtn() {
		// 使EditText 失去焦点
		showOrhiddenPannelByTagBtn();
		hideSoftKeyboard(context);
	}

	/**
	 * 展开更多功能
	 */
	private void onMoreBtn() {
		// 使EditText 失去焦点
		showOrhiddenPannelByMoreBtn();
		hideSoftKeyboard(context);
	}

	/**
	 * 
	 */
	private void onKeyboardBtn() {
		keybordBtn.setVisibility(View.GONE);
		micBtn.setVisibility(View.VISIBLE);
		keywordContainer.setVisibility(View.VISIBLE);
		voiceSearchBtn.setVisibility(View.GONE);

		showSoftKeyboard(keyWorkEditText);
		showOrHiddenSearchBar();
	}

	/**
	 * 语音
	 */
	private void onMicBtn() {
		// Intent intent = new Intent(context,
		// FWSpeechRecognizerActivity.class);
		// context.startActivityForResult(intent,
		// FWSpeechRecognizerActivity.kREQUEST_SPEECH_RECONGIZER);

		hiddenEditTextKeyboard();
		showOrHiddenSearchBar();

		keybordBtn.setVisibility(View.VISIBLE);
		micBtn.setVisibility(View.GONE);
		keywordContainer.setVisibility(View.GONE);
		voiceSearchBtn.setVisibility(View.VISIBLE);
		searchBtn.setVisibility(View.GONE);
		moreBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * 输入框内容改变
	 */
	private void onKeyWordChange(CharSequence text, int before, int start, int count) {
		String keyWord = keyWorkEditText.getText().toString();
		if (!StringUtils.isBlank(keyWord)) {
			searchBtn.setVisibility(View.VISIBLE);
			moreBtn.setVisibility(View.GONE);
		} else {
			searchBtn.setVisibility(View.GONE);
			moreBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 
	 */
	private void onFocusChangedKeywordEditText(View hello, boolean hasFocus) {
		if (hasFocus) {
			if (funsShow)
				showOrhiddenPannelByMoreBtn();
			if (tagsShow)
				showOrhiddenPannelByTagBtn();
			/*
			 * int screenHeight =
			 * context.getResources().getDisplayMetrics().heightPixels;
			 * TranslateAnimation animation = new TranslateAnimation(0, 0,
			 * screenHeight, screenHeight-
			 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 200,
			 * context.getResources().getDisplayMetrics()));
			 * animation.setDuration(300);
			 * barContainer.startAnimation(animation);
			 */

		}
	}

	/*
	 * private void onKeywordTextClick() { if (funsShow)
	 * showOrhiddenPannelByMoreBtn(); if (tagsShow)
	 * showOrhiddenPannelByTagBtn(); }
	 */

	/**
	 * 选择标签类别1
	 */
	private void onTagCategoryBtn1() {

		String code = jobTagTypes.get(0).getTagTypeCode();
		if (code.equals(selectedTagCategory))
			return;
		tags.put(selectedTagCategory, null);
		selectedTagCategory = code;
		currentTagstPage = 1;
		loadTags(selectedTagCategory);
		showTagCateogery();
		indicator.onPageScrolled(1, -1);
		tagCateogeryBtn1.setTextColor(context.getResources().getColor(R.color.font_white));
		tagCateogeryBtn2.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn3.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn1.setBackground(context.getResources().getDrawable(R.drawable.btn_style2));
		tagCateogeryBtn2.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
		tagCateogeryBtn3.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
	}

	/**
	 * 选择标签类别2
	 */
	private void onTagCategoryBtn2() {
		String code = jobTagTypes.get(1).getTagTypeCode();
		if (code.equals(selectedTagCategory))
			return;
		tags.put(selectedTagCategory, null);
		selectedTagCategory = code;
		currentTagstPage = 1;
		loadTags(selectedTagCategory);
		showTagCateogery();
		indicator.onPageScrolled(1, -1);
		tagCateogeryBtn2.setTextColor(context.getResources().getColor(R.color.font_white));
		tagCateogeryBtn1.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn3.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn2.setBackground(context.getResources().getDrawable(R.drawable.btn_style2));
		tagCateogeryBtn3.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
		tagCateogeryBtn1.setBackground(context.getResources().getDrawable(R.drawable.btn_style1));
	}

	/**
	 * 选择标签类别3
	 */
	private void onTagCategoryBtn3() {
		String code = jobTagTypes.get(2).getTagTypeCode();
		if (code.equals(selectedTagCategory))
			return;
		selectedTagCategory = code;
		currentTagstPage = 1;
		loadTags(selectedTagCategory);
		showTagCateogery();
		tagCateogeryBtn3.setTextColor(context.getResources().getColor(R.color.font_white));
		tagCateogeryBtn2.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn1.setTextColor(context.getResources().getColor(R.color.font_grey));
		tagCateogeryBtn3.setBackground(context.getResources().getDrawable(R.drawable.btn_style2));
		tagCateogeryBtn2.setBackground(context.getResources().getDrawable(R.drawable.btn_style1));
		tagCateogeryBtn1.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
	}

	private void onItemClickForTags(int position) {
		// Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
		onTagToggleBtn();
		List<JobSearchTagDTO> tags = this.tags.get(selectedTagCategory);

		int idnex = (currentTagstPage - 1) * tagPageSize + position;
		if (idnex < 0)
			idnex = 0;

		// Toast.makeText(context,tags.size()+"="+ idnex + ":"
		// +currentTagstPage, Toast.LENGTH_SHORT).show();
		// JobSearchTagDTO tag = tags.get(idnex);
		// Toast.makeText(context, tag.getTagName()
		// +":"+tag.getTagCode()+"->"+idnex, Toast.LENGTH_SHORT).show();
		barListener.onTagClick(tags.get(idnex));
	}

	/**
	 * 关键字搜索
	 */
	private void onSearchBtn() {
		hiddenEditTextKeyboard();
		barListener.onKeywordSearch(keyWorkEditText.getText().toString());
	}

	/**
	 * 切换职位列表
	 */
	private void onSwitchListViewBtn() {
		barListener.onSwitchMode(switchModeBtn);
	}

	/* ================ ================ */

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findJobSearchTagDTOByPage)) {
			onFinishLoadTags(result);
			isLoadTags = true;
		}
		if (reqId.equals(kREQ_ID_FINDJOBTAGTYPEDTO)) {
			onFinishLoadlTagTypes(result);
		}
		if (reqId.equals(kREQ_ID_findMoreTools)) {
			if (result != null) {
				Page<MoreToolsDTO> page = (Page<MoreToolsDTO>) result;
				moreFuncs = page.getElements();
				moreFuncAdapter.notifyDataSetChanged();
				// Toast.makeText(context, "yes!!", Toast.LENGTH_SHORT).show();
				isLoadFuns = true;
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	// 标签种类
	@UiThread
	void onFinishLoadlTagTypes(Object result) {
		if (result != null) {
			jobTagTypes = (List<JobTagTypeDTO>) result;
			// 默认加载第一个标签的类别的标签数据
			loadTags(jobTagTypes.get(0).getTagTypeCode());
			showTagCateogery();
		}
	}

	// 标签
	@UiThread
	void onFinishLoadTags(Object result) {
		if (result != null) {
			Page<JobSearchTagDTO> page = (Page<JobSearchTagDTO>) result;
			maxTagsPage = page.getMaxpage();
			List<JobSearchTagDTO> tags_ = tags.get(selectedTagCategory);
			if (tags_ == null)
				tags_ = new ArrayList<JobSearchTagDTO>();
			tags_.addAll(page.getElements());
			tags.put(selectedTagCategory, tags_);
			tagsAdapter.notifyDataSetChanged();
			indicator.setCount(maxTagsPage);
		}
	}

	private void onSwipeLeft() {
		currentTagstPage = currentTagstPage + 1;
		if (currentTagstPage >= maxTagsPage)
			currentTagstPage = maxTagsPage;

		if (!hasTags()) {// 判断当前页是否已存在
			loadTags(selectedTagCategory);
		} else {
			tagsAdapter.notifyDataSetChanged();
		}
		indicator.onPageScrolled(currentTagstPage, -1);
	}

	private void onSwipeRigth() {
		currentTagstPage = currentTagstPage - 1;
		if (currentTagstPage <= 0)
			currentTagstPage = 1;
		if (!hasTags()) {
			loadTags(selectedTagCategory);
		} else {
			tagsAdapter.notifyDataSetChanged();
		}
		// Toast.makeText(getActivity(), currentTagstPage + "",
		// Toast.LENGTH_SHORT).show();
		indicator.onPageScrolled(currentTagstPage, -1);
	}

	/**
	 * 显示更多功能
	 */
	void showMoreFuncs() {

		moreFuncAdapter.notifyDataSetChanged();
	}

	/**
	 * 当前页的标签是否已从后端加载
	 * 
	 * @return
	 */
	boolean hasTags() {
		List<JobSearchTagDTO> tags_ = tags.get(selectedTagCategory);
		if (tags_ != null && tags_.size() > (currentTagstPage - 1) * tagPageSize) {
			return true;
		}
		return false;
	}

	/**
	 * 显示标签类别
	 */
	public void showTagCateogery() {

		tagCateogeryBtn1.setBackgroundColor(context.getResources().getColor(R.color.white));
		tagCateogeryBtn2.setBackgroundColor(context.getResources().getColor(R.color.white));
		tagCateogeryBtn3.setBackgroundColor(context.getResources().getColor(R.color.white));
		
		if (jobTagTypes.size() > 0) {
			for (int i = 0; i < jobTagTypes.size(); i++) {
				JobTagTypeDTO type = jobTagTypes.get(i);
				if (i == 0) {
					tagCateogeryBtn1.setText(type.getTagTypeName());
					tagCateogeryBtn1.setVisibility(View.VISIBLE);
					Log.i("selectedTagCategory", selectedTagCategory + "");
					if (selectedTagCategory.equals(type.getTagTypeCode()))
						tagCateogeryBtn1.setBackgroundColor(context.getResources().getColor(R.color.blues));
					tagCateogeryBtn2.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
					tagCateogeryBtn3.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
				}
				if (i == 1) {
					tagCateogeryBtn2.setText(type.getTagTypeName());
					tagCateogeryBtn2.setVisibility(View.VISIBLE);
					if (selectedTagCategory.equals(type.getTagTypeCode()))
						tagCateogeryBtn2.setBackgroundColor(context.getResources().getColor(R.color.blues));
				}
				if (i == 2) {
					tagCateogeryBtn3.setText(type.getTagTypeName());
					tagCateogeryBtn3.setVisibility(View.VISIBLE);
					if (selectedTagCategory.equals(type.getTagTypeCode()))
						tagCateogeryBtn3.setBackgroundColor(context.getResources().getColor(R.color.blues));
				}
//				 if(i==0){
//				 tagCateogeryBtn1.setTextColor(context.getResources().getColor(R.color.font_white));
//				}else if(i==1){
//				 tagCateogeryBtn2.setTextColor(context.getResources().getColor(R.color.font_white));
//				}else if(i==2){
//				 tagCateogeryBtn3.setTextColor(context.getResources().getColor(R.color.font_white));
//				 }
			}
		}
	}

	/**
	 * 点击标签时候隐藏面板
	 */
	public void showOrhiddenPannelByTagBtn() {
		tagsShow = !tagsShow;
		funsShow = false;
		if (tagsShow) {
			tagToggleBtn.setBackgroundResource(R.drawable.tag_toggle_active);
			tagsContainerRl.setVisibility(View.VISIBLE);

			moreBtn.setBackgroundResource(R.drawable.more_btn_selector);
			moreFuncRl.setVisibility(View.GONE);

			relContentRl.setVisibility(View.VISIBLE);

			if (barListener != null) {
				barListener.onShow(true);
				if (!isLoadTags)
					loadTagTypes();
			}
		} else {
			tagToggleBtn.setBackgroundResource(R.drawable.tag_toggle_selector);
			tagsContainerRl.setVisibility(View.GONE);

			moreBtn.setBackgroundResource(R.drawable.more_btn_selector);
			moreFuncRl.setVisibility(View.GONE);

			relContentRl.setVisibility(View.GONE);

			if (barListener != null) {
				barListener.onShow(false);
			}
		}
	}

	/**
	 * 点击更多功能时候隐藏面板
	 */
	public void showOrhiddenPannelByMoreBtn() {
		funsShow = !funsShow;
		tagsShow = false;

		if (funsShow) {
			MobclickAgent.onEvent(context, Constants.UMENG_NEARBY_MORE_ACTION);
			tagToggleBtn.setBackgroundResource(R.drawable.tag_toggle_selector);
			tagsContainerRl.setVisibility(View.GONE);
			moreBtn.setBackgroundResource(R.drawable.more_active);
			moreFuncRl.setVisibility(View.VISIBLE);

			relContentRl.setVisibility(View.VISIBLE);

			if (barListener != null) {
				barListener.onShow(true);
				if (!isLoadFuns)
					loadMoreFuncs();
			}
		} else {
			tagToggleBtn.setBackgroundResource(R.drawable.tag_toggle_selector);
			tagsContainerRl.setVisibility(View.GONE);
			moreBtn.setBackgroundResource(R.drawable.more_btn_selector);
			moreFuncRl.setVisibility(View.GONE);

			relContentRl.setVisibility(View.GONE);
			if (barListener != null) {
				barListener.onShow(false);
			}
		}

	}

	/**
	 * 是
	 */
	public void hiddenEditTextKeyboard() {
		barContainer.requestFocus();
		hideSoftKeyboard(context);
	}

	/**
	 * 隐藏EditText 键盘
	 * 
	 * @param activity
	 */

	public void hideSoftKeyboard(Activity activity) {
		try {
			if (activity.getCurrentFocus() != null) {
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
			keyWorkEditText.clearFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showSoftKeyboard(EditText editText) {
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		final InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public void showOrHiddenSearchBar() {
		if (tagsShow) {
			onTagToggleBtn();
		} else if (funsShow) {
			onMoreBtn();
		} else {

		}
	}

	public void clear() {
		keyWorkEditText.setText("");
	}

	/**
	 * 标签
	 * 
	 * @author yangjiannan
	 * 
	 */
	class TagsGrideViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			int count = 0;
			Log.i("tags.size=currentTagstPage*tagPageSize", tags.size() + "=" + currentTagstPage + "*" + tagPageSize);
			List<JobSearchTagDTO> tags_ = tags.get(selectedTagCategory);
			if (tags_ == null)
				return 0;
			if (tags_.size() >= currentTagstPage * tagPageSize) {
				count = tagPageSize;
			} else {
				count = tags_.size() - (currentTagstPage - 1) * tagPageSize;
				if (count < 0)
					count = 0;
			}
			return count;
		}

		@Override
		public Object getItem(int p) {
			return p;
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View view, ViewGroup viewGroup) {

			View itemView;
			TextView tagNameText;
			tagNameText = null;

			itemView = LayoutInflater.from(context).inflate(R.layout.tags_item, null);

			tagNameText = (TextView) itemView.findViewById(R.id.tagNameText);
			// 取得当前页标签的开始索引
			int startIndex = (currentTagstPage - 1) * tagPageSize;
			if (startIndex < 0)
				startIndex = 0;
			// Log.i("startIndex ", startIndex + "");
			List<JobSearchTagDTO> tags_ = tags.get(selectedTagCategory);
			if (tags_ != null && tags_.size() > 0) {
				JobSearchTagDTO tagDto = tags_.get(startIndex + p);
				String tagName = tagDto.getTagName();
				tagNameText.setText(tagName);
			}
			return itemView;
		}

	}

	/**
	 * 更多功能
	 * 
	 * @author yangjiannan
	 * 
	 */
	class MoreFuncAdapter extends BaseAdapter {
		private DisplayImageOptions options;

		public MoreFuncAdapter() {
			options = new DisplayImageOptions.Builder().cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
					.cacheOnDisk(true).considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			if (moreFuncs != null) {
				return moreFuncs.size() + 1;
			}
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FuncItem item = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.funs_item_view, null);
				item = new FuncItem();
				item.funcItemIcon = (ImageView) convertView.findViewById(R.id.iv_app_icons);
				item.funcItemNameText = (TextView) convertView.findViewById(R.id.tv_app_title);
				item.ll_app_item = (RelativeLayout) convertView.findViewById(R.id.ll_app_item);
				convertView.findViewById(R.id.hotImg).setVisibility(View.GONE);
				convertView.setTag(item);

			} else {
				item = (FuncItem) convertView.getTag();
			}
			if (position >= moreFuncs.size()) {
				item.ll_app_item.setBackgroundResource(R.drawable.add_func_selector);
				item.funcItemIcon.setImageResource(R.drawable.ic_plus);
				item.funcItemNameText.setVisibility(View.GONE);
			} else {
				item.ll_app_item.setBackgroundResource(R.drawable.white_corner_rect);
				item.funcItemNameText.setVisibility(View.VISIBLE);
				MoreToolsDTO moreToolsDTO = moreFuncs.get(position);
				if (moreToolsDTO != null) {
					if (moreToolsDTO.getIosImg() != null) {
						ImageLoader.getInstance().displayImage(moreToolsDTO.getIosImg().getMedium(), item.funcItemIcon,
								options);
					}
					item.funcItemNameText.setText(moreToolsDTO.getToolName());
				}
			}

			return convertView;
		}

		class FuncItem {
			RelativeLayout ll_app_item;
			ImageView funcItemIcon;
			TextView funcItemNameText;
			ImageView hotImg;
		}

	}

	/**
	 * 刷新更多功能
	 */
	public void refreshMoreFuncs() {
		loadMoreFuncs();
	}

}
