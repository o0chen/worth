package com.blackeye.worth.core.sql.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Set;

public class BeanResultSet extends BeanBase {

	HashMap result = new HashMap();

	public BeanResultSet(ResultSet rs) {

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int count = rsmd.getColumnCount();
			for (int i = 0; i < count; i++) {
				String name = rsmd.getColumnLabel(i + 1).toLowerCase();
				result.put(name, rs.getObject(i + 1));
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public Object get(String name) {
		return result.get(name);
	}

	@Override
	public boolean contains(String key) {
		try {
			return result.containsKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void set(String name, Object object) {
		result.put(name, object);
	}

	@Override
	public void setAll(Object object, String... name) {
	}


	@Override
	public Set keySet() {
		return result.keySet();
	}

	@Override
	public Object get() {
		return result;
	}

}
