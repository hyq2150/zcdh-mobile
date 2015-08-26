package com.zcdh.mobile.app.extension;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;
import com.zcdh.mobile.R;

@SuppressLint("DrawAllocation")
public class ExtensionInputView extends EditText {

	private int textLength;

	private int borderColor;
	private float borderWidth;
	private float borderRadius;

	private int extensionLength;
	private int extensionColor;
	private float extensionWidth;
	private float extensionRadius;
	private CharSequence extension;

	private Paint extensionPaint = new Paint(ANTI_ALIAS_FLAG);
	private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

	private final int defaultContMargin = 4;
	private final int defaultSplitLineWidth = 3;
	private onTextChangedListener listener;

	public ExtensionInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final Resources res = getResources();

		final int defaultBorderColor = res
				.getColor(R.color.default_ev_border_color);
		final float defaultBorderWidth = res
				.getDimension(R.dimen.default_ev_border_width);
		final float defaultBorderRadius = res
				.getDimension(R.dimen.default_ev_border_radius);

		final int defaultExtensionLength = res
				.getInteger(R.integer.default_ev_extension_length);
		final int defaultExtensionColor = res
				.getColor(R.color.default_ev_extension_color);
		final float defaultExtensionWidth = res
				.getDimension(R.dimen.default_ev_extension_width);
		final float defaultExtensionRadius = res
				.getDimension(R.dimen.default_ev_extension_radius);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ExtensionView, 0, 0);
		try {
			borderColor = a.getColor(R.styleable.ExtensionView_borderColor,
					defaultBorderColor);
			borderWidth = a.getDimension(R.styleable.ExtensionView_borderWidth,
					defaultBorderWidth);
			borderRadius = a
					.getDimension(R.styleable.ExtensionView_borderRadius,
							defaultBorderRadius);
			extensionLength = a.getInt(
					R.styleable.ExtensionView_extensionLength,
					defaultExtensionLength);
			extensionColor = a.getColor(
					R.styleable.ExtensionView_extensionColor,
					defaultExtensionColor);
			extensionWidth = a.getDimension(
					R.styleable.ExtensionView_extensionWidth,
					defaultExtensionWidth);
			extensionRadius = a.getDimension(
					R.styleable.ExtensionView_extensionRadius,
					defaultExtensionRadius);
		} finally {
			a.recycle();
		}

		borderPaint.setStrokeWidth(borderWidth);
		borderPaint.setColor(borderColor);
		extensionPaint.setStrokeWidth(extensionWidth);
		extensionPaint.setTextSize(100);
		extensionPaint.setStyle(Paint.Style.FILL);
		extensionPaint.setColor(extensionColor);
	}

	public onTextChangedListener getListener() {
		return listener;
	}

	public void setListener(onTextChangedListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();

		// 外边框
		RectF rect = new RectF(0, 0, width, height);
		borderPaint.setColor(borderColor);
		canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

		// 内容区
		RectF rectIn = new RectF(rect.left + defaultContMargin, rect.top
				+ defaultContMargin, rect.right - defaultContMargin,
				rect.bottom - defaultContMargin);
		borderPaint.setColor(Color.WHITE);
		canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

		// 分割线
		borderPaint.setColor(borderColor);
		borderPaint.setStrokeWidth(defaultSplitLineWidth);
		for (int i = 1; i < extensionLength; i++) {
			float x = width * i / extensionLength;
			canvas.drawLine(x, 0, x, height, borderPaint);
		}

		// 邀请码
		float cx, cy = (float) (height / 1.5);
		float half = width / extensionLength / 3;
		for (int i = 0; i < textLength; i++) {
			cx = width * i / extensionLength + half;
			canvas.drawText(extension, i, i + 1, cx, cy, extensionPaint);
		}
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		this.textLength = text.toString().length();
		this.extension = text;
		invalidate();
		if (listener != null) {
			listener.onTextChanged(text);
		} else {
			listener = getListener();
		}
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		borderPaint.setColor(borderColor);
		invalidate();
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		borderPaint.setStrokeWidth(borderWidth);
		invalidate();
	}

	public float getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(float borderRadius) {
		this.borderRadius = borderRadius;
		invalidate();
	}

	public int getExtensionLength() {
		return extensionLength;
	}

	public void setExtensionLength(int extensionLength) {
		this.extensionLength = extensionLength;
		invalidate();
	}

	public int getExtensionColor() {
		return extensionColor;
	}

	public void setExtensionColor(int extensionColor) {
		this.extensionColor = extensionColor;
		extensionPaint.setColor(extensionColor);
		invalidate();
	}

	public float getExtensionWidth() {
		return extensionWidth;
	}

	public void setExtensionWidth(float extensionWidth) {
		this.extensionWidth = extensionWidth;
		extensionPaint.setStrokeWidth(extensionWidth);
		invalidate();
	}

	public float getExtensionRadius() {
		return extensionRadius;
	}

	public void setExtensionRadius(float extensionRadius) {
		this.extensionRadius = extensionRadius;
		invalidate();
	}

	public CharSequence getExtension() {
		return extension;
	}

	public interface onTextChangedListener {
		void onTextChanged(CharSequence text);
	}
}
