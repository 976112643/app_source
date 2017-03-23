package com.wq.support.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wq.support.utils.EmptyDeal;
import com.widget.SpanTextView;

public class TextViewUtil {
	/**
	 * 设置EditText内容并移动光标到尾部
	 * @param edittext
	 * @param text
     */
	public static  void setTextAndSelectEnd(EditText edittext,CharSequence text){
		edittext.setText(text);
		edittext.setSelection(edittext.getText().length());
	}
	/**
	 * 关键字高亮
	 * @param textView 文本控件
	 * @param souceTxt 设置的文字内容
	 * @param keywords 关键字
	 * @param color    关键字高亮颜色
	 */
	public static void keywordsHeightlight(View textView, String souceTxt, String keywords, int color) {
	    if(keywords==null|| souceTxt==null)return;
	    if(textView instanceof  TextView) {
	        SpanTextView.SpanEditable span = new SpanTextView.SpanEditable(souceTxt);
	        span.setColorSpanAll(EmptyDeal.dealNull(keywords),color);
	        ((TextView)textView).setText(span.commit());
	    }
	}
	 /**
     * 设置textview 图标,接收drawable对象
     *
     * @param tv
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setCompoundDrawables(TextView tv, Drawable left, Drawable top, Drawable right, Drawable bottom) {

        tv.setCompoundDrawables(getVaildDrawable(left), getVaildDrawable(top), getVaildDrawable(right), getVaildDrawable(bottom));
    }

    /**
     * 设置textview 图标,接收drawable资源id
     *
     * @param tv
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setCompoundDrawables(TextView tv, int left, int top, int right, int bottom) {
        if(tv==null)return ;
        Context context = tv.getContext();
        tv.setCompoundDrawables(getVaildDrawable(context, left), getVaildDrawable(context, top), getVaildDrawable(context, right), getVaildDrawable(context, bottom));
    }

    /**
     * 获取有效的drawable对象,初始化绘制范围
     *
     * @param drawable 原始drawable对象
     * @return
     */
    public static Drawable getVaildDrawable(Drawable drawable) {
        if (drawable == null) return drawable;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 获取有效的drawable对象,初始化绘制范围
     *
     * @param context
     * @param drawableId drawable 资源id
     * @return
     */
    public static Drawable getVaildDrawable(Context context, int drawableId) {
        if (drawableId == -1 || drawableId == 0) return null;
        return getVaildDrawable(context.getResources().getDrawable(drawableId));
    }

}

