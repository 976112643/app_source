package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wq.uicore.R;


/**
 * @author Administrator
 *
 * 自定义TiemView
 */
public class ItemView extends RelativeLayout  {
    Context mContext;
    private View view;
    private RelativeLayout rlItemView;

    private TextView tvItemLeftText;// 标题,右边文字按钮

    private TextView tvItemRightText;// 标题,右边文字按钮
    private ImageView ivItemRightImage;// 标题,右边文字按钮

    private Drawable leftIcon;
    private String leftText;
    private String rightText;
    private Boolean isShowRightImg;
    private Drawable RightImage;
    private int RightTextColor;
    private int leftColor;
    private Drawable RightTextIcom;

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        leftIcon = ta.getDrawable(R.styleable.ItemView_leftTVTextDrawLeftImg);
        leftText = ta.getString(R.styleable.ItemView_leftTVText);
        rightText = ta.getString(R.styleable.ItemView_RightTVText);
        isShowRightImg = ta.getBoolean(R.styleable.ItemView_RightImgShow, true);
        RightImage = ta.getDrawable(R.styleable.ItemView_SetRightImg);
        RightTextColor = ta.getColor(R.styleable.ItemView_SetRightTextColor, 0);
        RightTextIcom = ta.getDrawable(R.styleable.ItemView_SetRightTextIcom);
        leftColor=ta.getColor(R.styleable.ItemView_SetLeftTextColor,0);
        ta.recycle();
        initView();
    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.common_item_view, null);
        if (view != null) {

            rlItemView = (RelativeLayout) view.findViewById(R.id.item_layout);
            tvItemLeftText = (TextView) view.findViewById(R.id.item_left_text);
            ivItemRightImage = (ImageView) view.findViewById(R.id.item_right_img);
            tvItemRightText = (TextView) view.findViewById(R.id.item_right_text);
            addView(view);

        }

        setLeftTextDrawableLeft(leftIcon, leftText);
        setRightText(rightText);
        setRightImageVisibility(isShowRightImg);
        setRightImage(RightImage);
        setRightTextColor(RightTextColor);
        setRightTexticon(RightTextIcom);
        setLeftTextTextColor(leftColor);
    }


    public TextView getTvItemLeftText() {
        return tvItemLeftText;
    }

    public void setTvItemLeftText(TextView tvItemLeftText) {
        this.tvItemLeftText = tvItemLeftText;
    }

    public TextView getTvItemRightText() {
        return tvItemRightText;
    }

    public void setTvItemRightText(TextView tvItemRightText) {
        this.tvItemRightText = tvItemRightText;
    }

    public ImageView getIvItemRightImage() {
        return ivItemRightImage;
    }

    public void setIvItemRightImage(ImageView ivItemRightImage) {
        this.ivItemRightImage = ivItemRightImage;
    }


    /***************/


    /**
     * 设置左侧text图标 位置
     *
     * @param drawable
     * @param itemString
     */
    public void setLeftTextDrawableLeft(Drawable drawable, String itemString) {
        if (drawable != null) {
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            getTvItemLeftText().setCompoundDrawables(drawable, null, null, null);
        } else {
            getTvItemLeftText().setCompoundDrawables(null, null, null, null);
        }
        if (itemString != null) {
            getTvItemLeftText().setText(itemString);
        }

    }

    /**
     * 设置右侧text文字
     */
    public void setRightText(String itemString) {

        if (itemString != null) {
            getTvItemRightText().setVisibility(VISIBLE);
            getTvItemRightText().setText(itemString);
        } else {
            getTvItemRightText().setText("");
        }

    }

    /**
     * 设置右侧图标是否显示
     */
    public void setRightImageVisibility(Boolean isShow) {
        if (isShow) {
            getIvItemRightImage().setVisibility(View.VISIBLE);
        } else {
            getIvItemRightImage().setVisibility(View.GONE);
        }
    }




    /**
     * 设置右侧图片
     */
    public void setRightImage(Drawable drawable) {

        if (drawable != null) {
            getIvItemRightImage().setImageDrawable(drawable);
        }

    }

    /**
     * 设置右侧字体的颜色
     * @param RightTextColor
     */

    public void setRightTextColor(int RightTextColor) {
        if (RightTextColor != 0) {
            getTvItemRightText().setTextColor(RightTextColor);
        }

    }
    public void setLeftTextTextColor(int RightTextColor) {
        if (RightTextColor != 0) {
            getTvItemLeftText().setTextColor(RightTextColor);
        }

    }
    /**
     * 设置字体左侧图标
     */
    public void setRightTexticon(Drawable drawable){
        if (drawable != null) {
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            getTvItemRightText().setCompoundDrawables(drawable, null, null, null);
        } else {
            getTvItemRightText().setCompoundDrawables(null, null, null, null);
        }
    }

}

