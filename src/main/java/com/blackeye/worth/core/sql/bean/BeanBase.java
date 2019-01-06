package com.blackeye.worth.core.sql.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class BeanBase implements Bean {

	
	@Override
	public abstract Object get();
	
	@Override
	public Map toMap(){
		HashMap r = new HashMap();
		
		Object value;
		for(String key : keySet()){
			
			value = get(key);
			if(value == null)continue;
			value = value.toString();
			r.put(key, value);
		}
		return r;
	}
	
	@Override
	public void setAll(Object object, String... name) {
		Bean bean = BeanFactory.newBean(object);
		
		if(name.length == 0){
			for(String key : bean.keySet()){
				set(key, bean.get(key));
			}
			return;
		}
		
		if(name.length == 1){
			name = name[0].split("\\s+");
		}
		
		for(String n : name){
			this.set(n, bean.get(n));
		}
	}

	@Override
	public boolean contains(String name) {
		return false;
	}

	@Override
	public Object get(String name) {
		return null;
	}

	@Override
	public void set(String name, Object value) {
		
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

}
