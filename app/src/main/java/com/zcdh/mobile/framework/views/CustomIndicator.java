package com.zcdh.mobile.framework.views;

import com.zcdh.mobile.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class CustomIndicator extends View {
	// 空心圆半径
	private int RADIUS = 6;
	// 空心圆画笔
	private Paint mBgPaint;
	// 实心圆画笔（当前页）
	private Paint mPaint;
	// 圆点个数,默认为5,设计布局时可以预览
	private int mCount = 0;
	// 当前实心圆的位置
	private int mPosition;
	// 偏移量（百分比）
	private float mOffset;
	// 第一个空心圆的圆心坐标
	private int startY;
	private int startX;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 画出空心的小圆点
		for (int i = 0; i < mCount; i++) {
			canvas.drawCircle(startX + i * 3 * RADIUS, startY, RADIUS, mBgPaint);
			
		}

		// 画出指示当前位置的原点,由于高度固定，所以只计算了X坐标
		float x = startX + (mPosition + mOffset) * 3 * RADIUS;
		canvas.drawCircle(x, startY, RADIUS - 1, mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 获取第一个空心圆的圆心坐标
		startX = (w - 3 * RADIUS * mCount)/2+RADIUS;
		startY = h / 2;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	// 设置圆个数
	public void setCount(int count) {
		mCount = count;
		invalidate();
	}

	// 获取偏移量并重绘indicator
	public void onPageScrolled(int position, float offset) {
		mPosition = position;
		mOffset = offset;
		if(offset==-1){
			offset = startX + position*RADIUS*3;
		}
		invalidate();
	}

	// 创建带AttributeSet参数的构造方法使控件可以直接拖动到布局中并预览
	public CustomIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	// 初始化画笔
	private void initPaint() {
		// 空心圆画笔
		mBgPaint = new Paint();
		mBgPaint.setColor(Color.GRAY);
		mBgPaint.setAntiAlias(true);
		mBgPaint.setStyle(Paint.Style.STROKE);
		mBgPaint.setStrokeWidth(2);
		// 实心圆画笔
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.bright_blue));
		mPaint.setAntiAlias(true);

	}

}