package com.blackeye.worth.core.sql.bean;


import com.alibaba.fastjson.JSONObject;

import java.util.Set;


public class BeanJSONObject extends BeanBase {

	JSONObject json;

	public BeanJSONObject(JSONObject json) {
		this.json = json;		
	}

	@Override
	public Object get(String name) {
		Object r = json.get(name);
		return r;
	}
	
	@Override
	public boolean contains(String name) {
		return json.containsKey(name);
	}

	@Override
	public void set(String name, Object object) {
		json.put(name, object);
	}


	@Override
	public Set<String> keySet() {
		return json.keySet();
	}

	@Override
	public Object get() {
		return json;
	}


}
