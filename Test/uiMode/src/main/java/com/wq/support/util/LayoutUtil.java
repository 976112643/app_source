package com.wq.support.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * 布局工具,简化一些边距,
 * Created by cnsunrun on 2016/12/1.
 */

public class LayoutUtil {
    public  static void setLayout(View view ,int width,int height){
        if (view == null ||view.getLayoutParams()==null) {
            return ;
        }
        if(view.getLayoutParams() instanceof ViewGroup.LayoutParams){
            ViewGroup.LayoutParams layoutParams= (ViewGroup.LayoutParams) view.getLayoutParams();
            layoutParams.width=width;
            layoutParams.height=height;
            view.setLayoutParams(layoutParams);
        }
    }
    /**
     * 设置控件外边距 -1为不改变原始值
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     *
     */
  public static  void  setMargin(View view, int left, int top, int right, int bottom) {
      if (view == null) {
          return ;
      }
      if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
          ViewGroup.MarginLayoutParams marginLayoutParams= (ViewGroup.MarginLayoutParams) view.getLayoutParams();
          marginLayoutParams.leftMargin=getVaildVal(left,marginLayoutParams.leftMargin);
          marginLayoutParams.rightMargin=getVaildVal(right,marginLayoutParams.rightMargin);
          marginLayoutParams.topMargin=getVaildVal(top,marginLayoutParams.topMargin);
          marginLayoutParams.bottomMargin=getVaildVal(bottom,marginLayoutParams.bottomMargin);
          view.setLayoutParams(marginLayoutParams);
      }
    }


    /**
     * 判断值是否有效
     * @param setVal
     * @param defVal
     * @return
     */
    static int getVaildVal(int setVal,int defVal){
        return setVal==-1?defVal:setVal;
    }
}
