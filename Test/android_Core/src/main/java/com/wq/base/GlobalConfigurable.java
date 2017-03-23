package com.wq.base;

import java.util.HashMap;

public interface GlobalConfigurable {
	public String get(String key);
	public HashMap<String,Object> getConfigs();
	
}
