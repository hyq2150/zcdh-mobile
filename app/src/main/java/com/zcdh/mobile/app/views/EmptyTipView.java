package com.zcdh.mobile.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.framework.nio.RequestException;

/**
 * 空列表提示View
 * @author jeason, 2014-7-25 下午2:57:01
 */
public class EmptyTipView extends LinearLayout implements OnClickListener {

	/**
	 * 
	 */
	private TextView tvInfo;
	/**
	 * 标识加载数据
	 */
	private FrameLayout loadingFl;
	/**
	 * 加载数据动画
	 */
	private ImageView loadingImg;
	
	/**
	 * 无内容图标
	 */
	private  ImageView exceptionImg;
	
	/**
	 *点击加载数据
	 */
	private DataLoadInterface dataLoadInterface;
	
	ProgressBar loadingPB;
	
	public EmptyTipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bindViews();
	}

	public EmptyTipView(Context context, String tips) {
		this(context);
		tvInfo.setText(tips);
	}

	public EmptyTipView(Context context, int tipRes) {
		this(context);
		tvInfo.setText(tipRes);
	}

	public EmptyTipView(Context context) {
		super(context);
		
		//初始化views
		bindViews();
		
	}
	
	private void bindViews(){
		
		LayoutInflater.from(getContext()).inflate(R.layout.empty_view, this,
				true);
		
		loadingFl = (FrameLayout) findViewById(R.id.loadingFl);
		loadingImg = (ImageView) findViewById(R.id.loadingImg2); 
		tvInfo = (TextView) findViewById(R.id.tips);
		exceptionImg = (ImageView) findViewById(R.id.exceptionImg);
		loadingPB = (ProgressBar) findViewById(R.id.loadingPB);
		exceptionImg.setOnClickListener(this);
		
		//startLoadingAnim();
	}
	
	public void startLoadingAnim(){
		setVisibility(View.VISIBLE);
		loadingFl.setVisibility(View.GONE);
		loadingImg.setVisibility(View.GONE);
		exceptionImg.setVisibility(View.GONE);

		tvInfo.setText(getResources().getString(R.string.loading));
		tvInfo.setVisibility(View.VISIBLE);
		loadingPB.setVisibility(View.VISIBLE);
		//开始加载动画
		/*Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
		loadingImg.startAnimation(anim);*/
	}

	public void setTip(CharSequence tip) {
		tvInfo.setText(tip);
		loadingPB.setVisibility(View.GONE);
	}

	public void setTip(int tipRes) {
		setTip(getResources().getString(tipRes));
	}
	/**
	 * 
	 * @param isEmpty 标识是否无内容
	 */
	public void isEmpty(boolean isEmpty){
		if(isEmpty){
			exceptionImg.setImageResource(R.drawable.nothing);
			exceptionImg.setVisibility(View.VISIBLE);
			tvInfo.setVisibility(View.GONE);
			
		}else{
			exceptionImg.setVisibility(View.GONE);
			setVisibility(View.GONE);
		}
		
		loadingImg.clearAnimation();
		loadingFl.setVisibility(View.GONE);
		loadingPB.setVisibility(View.GONE);
	}
	
	public void showException(ZcdhException exc, DataLoadInterface retryDataLoader){
		showException(exc.getErrCode(), retryDataLoader);
	}

	public void showException(String errCode, DataLoadInterface retryDataLoader) {
		this.dataLoadInterface = retryDataLoader;
		if(errCode!=null){
			//  显示网络连接异常图标
			if(errCode.equals(RequestException.EXC_CODE_NETWORK+"")){
				exceptionImg.setImageResource(R.drawable.lineoff);
			}
			// 显示连接服务异常图标
			if(errCode.equals(RequestException.EXC_CODE_SESSION+"")){
				exceptionImg.setImageResource(R.drawable.wrong);
			}
		}
		exceptionImg.setVisibility(View.VISIBLE);
		loadingImg.clearAnimation();
		loadingFl.setVisibility(View.GONE);
		tvInfo.setVisibility(View.GONE);
		loadingPB.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.exceptionImg){
			if(dataLoadInterface!=null){
				startLoadingAnim();
				dataLoadInterface.loadData();
			}
		}
	}

}
