package com.blackeye.worth.core.sql.bean;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class MapToObject {

	public void set(Object obj, Map map) {

		if (obj == null) {
			return;
		}
		if (map == null || map.size() == 0) {
			return;
		}

		Field[] fs = obj.getClass().getFields();

		for (Field f : fs) {

			
			String name = f.getName();
			
			Class clz = f.getType();

			Object value = map.get(name);

			if (clz.isArray()) {

				Class cls = clz.getComponentType();

				if (cls == String.class) {
					if(value instanceof String){
						setStringArray(obj, f, Arrays.asList(((String) value).split(",")));
					}else{
						setStringArray(obj, f, (List) value);
					}
					
					continue;
				}
				if (cls == Integer.class) {
					setIntegerArray(obj, f, (List) value);
					continue;
				}
				if (cls == Float.class) {
					setFloatArray(obj, f, (List) value);
					continue;
				}
				List<Map> l = getMapArray(map, name);
				
				Object[] objs = null;
				try {
					objs = (Object[]) f.get(obj);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
				if(objs == null || objs.length ==0){
					
					int length = l.size();
					objs = (Object[]) Array.newInstance(cls, length);
					for(int i=0;i<length;i++){
						try {
							objs[i] = cls.newInstance();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					try {
						f.set(obj, objs);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				int i = 0;
				for (Map currentMap : l) {
					set(objs[i], currentMap);
					i++;
				}

				continue;
			}

			if (clz == String.class) {
				setString(obj, f, value);
				continue;
			}
			if (clz == Integer.class) {
				setInteger(obj, f, value);
				continue;
			}
			if (clz == Float.class) {
				setFloat(obj, f, value);
				continue;
			}

			Map objMap = getMap(map, name);
			if (objMap.size() > 0) {
				try {
					set(f.get(obj), objMap);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private List<Map> getMapArray(Map map, String name) {

		List<Map> l = null;

		String prefix = name + ".";
		for (Object eeee : map.entrySet()) {

			Map.Entry entry = (Map.Entry) eeee;

			if (entry.getKey().toString().startsWith(prefix)) {
				List values = (List) entry.getValue();

				int length = values.size();

				if (l == null) {
					l = new ArrayList(length);
					for (int i = 0; i < length; i++) {
						l.add(new HashMap());
					}
				}

				for (int i = 0; i < length; i++) {
					l.get(i).put(
							entry.getKey().toString()
									.substring(prefix.length()),
							values.get(i));
				}
			}

		}
		return l == null ? new ArrayList(0) : l;
	}
	
	

	private Map getMap(Map map, String name) {
		Map r = new HashMap();

		String prefix = name + ".";
		for (Object eeee : map.entrySet()) {

			Map.Entry entry = (Map.Entry) eeee;

			if (entry.getKey().toString().startsWith(prefix)) {
				r.put(entry.getKey().toString().substring(prefix.length()),
						entry.getValue());
			}

		}

		return r;
	}

	private void setInteger(Object obj, Field field, Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof Object[]) {
			value = ((Object[]) value)[0];
		}
		if (value instanceof List) {
			value = ((List) value).get(0);
		}
		if(value.toString().length() == 0){
			return;
		}
		value = Integer.valueOf(value.toString(), 10);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}

	}
	private void setFloat(Object obj, Field field, Object value) {
		if (value == null) {
			return;
		}
		if (value instanceof Object[]) {
			value = ((Object[]) value)[0];
		}
		if (value instanceof List) {
			value = ((List) value).get(0);
		}
		if(value.toString().length() == 0){
			return;
		}
		value = Float.valueOf(value.toString());
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}

	}
	private void setString(Object obj, Field field, Object value) {

		if (value == null) {
			return;
		}
		if (value instanceof Object[]) {
			value = join((Object[]) value);
		}

		if (value instanceof List) {
			value = join((List) value);
		}
		try {
			field.set(obj, value.toString());
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		}

	}

	private Object join(Object[] value) {
		StringBuilder s = new StringBuilder();
		boolean b = false;
		for(Object ss: value){
			if(b){
				s.append(",");
			}else{
				b = true;
			}
			s.append(ss);
		}
		return s.toString();
	}

	private Object join(List value) {
		StringBuilder s = new StringBuilder();
		boolean b = false;
		for(Object ss: value){
			if(b){
				s.append(",");
			}else{
				b = true;
			}
			s.append(ss);
		}
		return s.toString();
	}

	public static void setIntegerArray(Object obj, Field field, List value) {

		if(value==null){
			return;
		}
		Integer[] objs;
		try {
			objs = (Integer[]) field.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		int length = objs==null ? 0 : objs.length;

		if (length == 0) {
			length = value.size();
			objs = (Integer[]) Array.newInstance(Integer.class, length);
			try {
				field.set(obj, objs);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}

		for (int i = 0; i < length; i++) {
			objs[i] = Integer.valueOf(value.get(i).toString(), 10);
		}

	}
	public static void setFloatArray(Object obj, Field field, List value) {

		if(value==null){
			return;
		}
		Float[] objs;
		try {
			objs = (Float[]) field.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		int length = objs==null ? 0 : objs.length;

		if (length == 0) {
			length = value.size();
			objs = (Float[]) Array.newInstance(Float.class, length);
			try {
				field.set(obj, objs);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}

		for (int i = 0; i < length; i++) {
			objs[i] = Float.valueOf(value.get(i).toString());
		}

	}
	public static void setStringArray(Object obj, Field field, List value) {
		

		if(value==null){
			return;
		}
		String[] objs;
		try {
			objs = (String[]) field.get(obj);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		int length = objs== null ? 0 : objs.length;

		if (length == 0) {
			length = value.size();
			objs = (String[]) Array.newInstance(String.class, length);
			try {
				field.set(obj, objs);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}

		for (int i = 0; i < length; i++) {
			objs[i] = value.get(i).toString();
		}

	}

}