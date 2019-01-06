package com.blackeye.worth.core.sql.bean;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanObject extends BeanBase {

	Object object;

	Map<String, Field> fieldMap = new HashMap();

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public BeanObject(Object obj) {
		this.object = obj;
		Field[] fields = obj.getClass().getFields();
		for (Field f : fields) {
			String name = getFieldName(f);
			fieldMap.put(name, f);
		}
	}

	public static String getFieldName(Field f) {

//		SessionAttribute a = f.getAnnotation(SessionAttribute.class);
//		if (a != null && a.Name() != null && a.Name().length() > 0) {
//			return a.Name();
//		}
//
//		Parameter p = f.getAnnotation(Parameter.class);
//		if (p != null && p.Name() != null && p.Name().length() > 0) {
//			return p.Name();
//		}

		return f.getName();
	}

	@Override
	public Object get(String name) {
		
		if(name == null)return null;
		
		String[] names = name.split("\\.", 2);
		if(names.length==2){
			Object object = get(names[0]);
			if(object == null){
				return null;
			}
			return BeanFactory.newBean(object).get(names[1]);
		}
		
		
		Field field = fieldMap.get(name);
		if (field == null)
			return null;
		try {
			return field.get(object);
		} catch (IllegalArgumentException e) {
			System.err.println("exception: object " + object + " field " + name
					+ " illegal argument");
		} catch (IllegalAccessException e) {
			System.err.println("exception object " + object + " field " + name
					+ " not public");
		}
		return null;
	}

	@Override
	public boolean contains(String key) {
		return fieldMap.containsKey(key);
	}

	@Override
	public void set(String name, Object value) {

		if (value == null)
			return;

		String[] names = name.split("\\.", 2);
		if(names.length==2){
			Object object = get(names[0]);
			if(object == null){
				return;
			}
			BeanFactory.newBean(object).set(names[1], value);
			return;
		}
		
		try {
			Field field = fieldMap.get(name);
			
			if(field==null){
				return;
			}

			if (field.getType() != value.getClass()) {
				if (value.getClass() != String.class) {
					value = value.toString();
				}
				
				if (field.getType().isArray()) {

					Class cls = field.getType().getComponentType();

				
					if (cls == Integer.class) {
						
						String[] ss = value.toString().split(",");
						
						Integer[] t = new Integer[ss.length];
						
						for(int i=0;i<ss.length;i++){
							t[i] = Integer.valueOf(ss[i], 10);
						}
						field.set(object, t);
						return;
					}

					if (cls == Float.class) {
						
						String[] ss = value.toString().split(",");
						
						Float[] t = new Float[ss.length];
						
						for(int i=0;i<ss.length;i++){
							t[i] = Float.valueOf(ss[i]);
						}
						field.set(object, t);
						return;
					}

					if (cls == String.class) {
						
						String[] ss = value.toString().split(",");
						
						field.set(object, ss);
						return;
					}
					
				}

				value = Caster.valueOf(field.getType(), (String) value);
			}

			field.set(object, value);
		} catch (Exception e) {
			System.out.println("exception: "+name);
			e.printStackTrace();
		}
	}


	@Override
	public Set keySet() {
		return fieldMap.keySet();
	}

	@Override
	public Object get() {
		return object;
	}

}
