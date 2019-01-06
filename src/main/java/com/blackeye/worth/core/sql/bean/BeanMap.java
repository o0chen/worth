package com.blackeye.worth.core.sql.bean;

import java.util.Map;
import java.util.Set;

public class BeanMap extends BeanBase {

	Map map;

	public BeanMap(Map map) {
		this.map = map;
	}

	@Override
	public Object get(String name) {
		if(name == null){
			return null;
		}
		Object r = map.get(name);
//		if(r == null){
//			String table = (String) map.get("table");
//			if(table == null){
//				return null;
//			}
//
//			int beginIndex = name.indexOf('.');
//			if(beginIndex == -1){
//				return null;
//			}
//
//			String curkey = name.substring(0, beginIndex) + "_id";
//			String nextkey = name.substring(beginIndex + 1);
//
//			Object id = map.get(curkey);
//			if(id == null){
//				return null;
//			}
//
//			Linker type = Linker.get(table, curkey);
//
//			if(type == null){
//				return null;
//			}
//
//			String sql = String.format("select %s from %s where id=?", nextkey, type.referer_table_name);
//			Map first = SQLExecutor.first(sql, id);
//			Bean nb = BeanFactory.newBean(first);
//			return nb.get(nextkey);
			
//		}
		return r;
	}
	
	@Override
	public boolean contains(String name) {
		return map.containsKey(name);
	}

	@Override
	public void set(String name, Object object) {
		map.put(name, object);
	}

	@Override
	public Set keySet() {
		return map.keySet();
	}

	@Override
	public Object get() {
		return map;
	}


}
