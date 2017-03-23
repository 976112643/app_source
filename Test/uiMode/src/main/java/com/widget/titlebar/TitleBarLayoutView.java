package com.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wq.uicore.R;

/**
 * @作者: Wang'sr
 * @时间: 2016/11/2
 * @功能描述:顶部菜单的公共类
 */

public class TitleBarLayoutView extends RelativeLayout {
	private View rootView;
	private Context mContext;
	private TextView tvLeft;
	private TextView tvRight;
	private ImageView ivLeftIcon;
	private ImageView ivRightIcon;
	private TextView tvTitle;

	public RelativeLayout getRelLeftArea() {
		return relLeftArea;
	}

	public void setRelLeftArea(RelativeLayout relLeftArea) {
		this.relLeftArea = relLeftArea;
	}

	public RelativeLayout getRelRightArea() {
		return relRightArea;
	}

	public void setRelRightArea(RelativeLayout relRightArea) {
		this.relRightArea = relRightArea;
	}

	private RelativeLayout relLeftArea;
	private RelativeLayout relRightArea;
	private Drawable leftIconDrawable;
	private Drawable rightIconDrawable;
	private String leftText;
	private String rightText;
	private String titleText;
	private boolean isShowRightImg;

	public TitleBarLayoutView(Context context) {
		this(context, null);
	}

	public TitleBarLayoutView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public TitleBarLayoutView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		/** 获取资源属性 */
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.TitleBarLayoutView);
		leftIconDrawable = ta
				.getDrawable(R.styleable.TitleBarLayoutView_TitlesLeftIcon);
		rightIconDrawable = ta
				.getDrawable(R.styleable.TitleBarLayoutView_TitlesRightIcon);
		leftText = ta
				.getString(R.styleable.TitleBarLayoutView_TitlesLeftTVText);
		rightText = ta
				.getString(R.styleable.TitleBarLayoutView_TitlesRightTVText);
		titleText = ta.getString(R.styleable.TitleBarLayoutView_TitlesText);
		isShowRightImg = ta.getBoolean(
				R.styleable.TitleBarLayoutView_TitlesRightIconIsShow, false);
		ta.recycle();
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.include_title_view, this, false);
		if (rootView != null) {
			tvLeft = (TextView) rootView.findViewById(R.id.tv_left_text);
			tvRight = (TextView) rootView.findViewById(R.id.tv_right_text);
			ivLeftIcon = (ImageView) rootView.findViewById(R.id.iv_left_icon);
			ivRightIcon = (ImageView) rootView.findViewById(R.id.iv_right_icon);
			tvTitle = (TextView) rootView.findViewById(R.id.tv_title_text);
			relLeftArea = (RelativeLayout) rootView
					.findViewById(R.id.left_back_area);
			relRightArea = (RelativeLayout) rootView
					.findViewById(R.id.right_back_area);
			addView(rootView);
		}

		setView();

	}

	/**
	 * 设置值和参数
	 */
	private void setView() {
		setLeftIconDrawable(leftIconDrawable);
		setRightIconDrawable(rightIconDrawable);
		setRightText(rightText);
		setLeftText(leftText);
		setTitleText(titleText);

		/** 扩展设置公共监听 */
		getRelLeftArea().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("TitleView", "OnClickListener");
				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
				((Activity) mContext).finish();
				// ((Activity) mContext).overridePendingTransition(
				// R.anim.slide_in_situ, R.anim.push_right_out);
			}
		});

	}

	/**
	 * -------------------- Get View 方便扩展 ----------------
	 */

	public TextView getTvLeft() {
		return tvLeft;
	}

	public TextView getTvRight() {
		return tvRight;
	}

	public ImageView getIvLeftIcon() {
		return ivLeftIcon;
	}

	public ImageView getIvRightIcon() {
		return ivRightIcon;
	}

	public TextView getTvTitle() {
		return tvTitle;
	}

	/**
	 * -------------------- set View 方便扩展 ----------------
	 */

	public void setLeftIconDrawable(Drawable leftIconDrawable) {
		this.leftIconDrawable = leftIconDrawable;
		getIvLeftIcon().setImageDrawable(leftIconDrawable);
	}

	public void setRightIconDrawable(Drawable rightIconDrawable) {
		this.rightIconDrawable = rightIconDrawable;
		getIvRightIcon().setImageDrawable(rightIconDrawable);
		getIvRightIcon().setVisibility(VISIBLE);
	}

	public void setLeftText(String leftText) {
		this.leftText = leftText;
		getTvLeft().setText(leftText);
	}

	public void setRightText(String rightText) {
		this.rightText = rightText;
		getTvRight().setText(rightText);
		getTvRight().setVisibility(VISIBLE);
		setIsShowRightImg(false);
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
		getTvTitle().setText(titleText);
	}

	public void setIsShowRightImg(boolean isShowRightImg) {
		this.isShowRightImg = isShowRightImg;
		getIvRightIcon().setVisibility(isShowRightImg ? VISIBLE : GONE);
	}
}
