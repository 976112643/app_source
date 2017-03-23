package com.wq.base;

import java.util.HashMap;

public class GlobalConfig {
	static	GlobalConfigurable global;

	public static String get(String key) {
		checkConfig();
		return global.get(key);
	}

	public static HashMap<String, Object> getConfigs() {
		// TODO Auto-generated method stub
		return global.getConfigs();
	}
	
	public static void checkConfig(){
		if(global==null)
			throw new RuntimeException("请在Application 中对全局配置进行初始化");
	}
	public static void initConfig(GlobalConfigurable config){
		
	}
}
