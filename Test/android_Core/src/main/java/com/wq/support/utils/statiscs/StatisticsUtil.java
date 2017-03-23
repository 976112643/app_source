package com.wq.support.utils.statiscs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.baidu.mobstat.StatService;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计工具
 * Created by WQ on 2016/12/20.
 */

public class StatisticsUtil {
    static Map<String,String >PageNames=new HashMap<String,String>();
    public static void start(Context context){
        StatService.start(context);
    }
    public static void put(String pageClassName,String pageName){
        PageNames.put(pageClassName,pageName);
    }
    public static void put(Class<?> pageClassName,String pageName){
        PageNames.put(pageClassName.getName(),pageName);
    }
    public static String getPageName(String pageClassName){
        if(PageNames.containsKey(pageClassName)){
            return PageNames.get(pageClassName);
        }
       return pageClassName;
    }
    public static void pageStart(Fragment fragment){
        checkContains(fragment.getClass());
        StatService.onPageStart(fragment.getActivity(), StatisticsUtil.getPageName(fragment.getClass().getName()));
    }

    private static void checkContains(Class<?> aClass) {
        if(!PageNames.containsKey(aClass.getName())){
            put(aClass,aClass.getName());
        }
    }

    public static void pageStart(Activity activity){
        StatService.onPageStart(activity, StatisticsUtil.getPageName(activity.getClass().getName()));
    }
    public static void pageEnd(Fragment fragment){
        StatService.onPageEnd(fragment.getActivity(), StatisticsUtil.getPageName(fragment.getClass().getName()));
    }
    public static void pageEnd(Activity activity){
        StatService.onPageEnd(activity, StatisticsUtil.getPageName(activity.getClass().getName()));
    }
}
