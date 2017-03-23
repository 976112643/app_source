package com.wq.support.uibase;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

/**
 * 首选项操作扩展基类,繁重数据操作请使用 getSession 获取Session对象来进行操作,或者使用数据库
 * @author WQ 下午3:14:18
 */
public abstract class SharedPreferencesBaseActivity extends BaseActivity implements
		SharedPreferences , Editor,OnSharedPreferenceChangeListener{

	private Editor editor;//首选项编辑器
	private SharedPreferences prefrences;//全局首选项
	private String prefrencesName;//首选项储存名称
	public static final int NAME_MODE_ACTIVITY=0,NAME_MODE_GLOBAL=1;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(newBase);
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setPrefrenceNameMode(NAME_MODE_GLOBAL);
	}
	
	/**
	 * 设置首选项模式,全局/限于该类  
	 * @param NameMode
	 */
	public void setPrefrenceNameMode(int NameMode){
		if(NameMode==NAME_MODE_ACTIVITY){
			prefrencesName=getClass().getSimpleName();
		}else if(NameMode==NAME_MODE_GLOBAL){
			prefrencesName=	getPackageName();
		}
	}

	/**
	 * 设置首选项名称 ,全局以及类范围的首选项模式不适用时,可自己指定
	 * @param prefrencesName
	 */
	public void setPrefrenceName(String prefrencesName) {
		this.prefrencesName = prefrencesName;
	}
	public SharedPreferences getPrefrences(){
		if(prefrences==null ){
			prefrences=	getSharedPreferences(prefrencesName, Context.MODE_PRIVATE);}
		return prefrences;
	}
	public Editor getEditor() {
		if (editor == null) {
			editor = getPrefrences()
					.edit();
		}
		return editor;
	}

	@Override
	protected void onResume() {
		
		registerOnSharedPreferenceChangeListener(this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		commit();
		unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}
	@Override
	public Map<String, ?> getAll() {
		return getPrefrences().getAll();
	}

	@Override
	public String getString(String key, String defValue) {
		// TODO Auto-generated method stub
		return getPrefrences().getString(key, defValue);
	}

	@Override
	public Set<String> getStringSet(String key, Set<String> defValues) {
		// TODO Auto-generated method stub
		return getPrefrences().getStringSet(key, defValues);
	}

	@Override
	public int getInt(String key, int defValue) {
		// TODO Auto-generated method stub
		return  getPrefrences().getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		// TODO Auto-generated method stub
		return  getPrefrences().getLong(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		// TODO Auto-generated method stub
		return  getPrefrences().getFloat(key, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		// TODO Auto-generated method stub
		return  getPrefrences().getBoolean(key, defValue);
	}

	@Override
	public boolean contains(String key) {
		return  getPrefrences().contains(key)
				;
	}

	@Override
	public Editor edit() {
		return  getPrefrences().edit()
				;
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		 getPrefrences().registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		getPrefrences().unregisterOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public Editor putString(String key, String value) {
		return getEditor().putString(key, value);
	}

	@Override
	public Editor putStringSet(String key, Set<String> values) {
		return  getEditor().putStringSet(key, values);
	}

	@Override
	public Editor putInt(String key, int value) {
		return  getEditor().putInt(key, value);
	}

	@Override
	public Editor putLong(String key, long value) {
		return  getEditor().putLong(key, value);
	}

	@Override
	public Editor putFloat(String key, float value) {
		return  getEditor().putFloat(key, value);
	}

	@Override
	public Editor putBoolean(String key, boolean value) {
		return  getEditor().putBoolean(key, value);
	}

	@Override
	public Editor remove(String key) {
		return  getEditor().remove(key);
	}

	@Override
	public Editor clear() {
		return  getEditor().clear();
	}

	@Override
	public boolean commit() {
		return  getEditor().commit();
	}

	@Override
	public void apply() {
		 getEditor().apply();
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
	}
}
