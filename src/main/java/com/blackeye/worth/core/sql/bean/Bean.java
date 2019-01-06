package com.blackeye.worth.core.sql.bean;

import java.util.Map;
import java.util.Set;

public interface Bean {

	public boolean contains(String name);
	public Object get(String name);
	public void set(String name, Object value);
	public void setAll(Object object, String... name);
	public Set<String> keySet();
	
	public Object get();
	public Map toMap();
}
