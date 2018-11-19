package com.blackeye.worth.utils;

import com.blackeye.worth.config.SysDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ObjectMapUtils {

	public final static Integer UNDERLINEF_FORMATER=1;

	public static Map objectToMap(Object thisObj){
		return objectToMap(thisObj,0);
	}
	/***
	 * 将对象转换为map对象
	 * @param keyFomater map中key值的格式 0-保持驼峰 1-转成下划线
	 * @param thisObj 对象
	 * @return
	 */
	public static Map objectToMap(Object thisObj,Integer keyFomater)
	{
		Map map = new HashMap();
		Class c;
		try
		{
			c = Class.forName(thisObj.getClass().getName());
			//获取所有的方法
			Method[] m = c.getMethods();
			for (int i = 0; i < m.length; i++)
			{   //获取方法名
				String method = m[i].getName();
				//获取get开始的方法名
				if (method.startsWith("get")&&!method.contains("getClass"))
				{
					try{
						//获取对应对应get方法的value值
						Object value = m[i].invoke(thisObj);
						if (value != null)
						{
							//截取get方法除get意外的字符 如getUserName-->UserName
							String key=method.substring(3);
							//将属性的第一个值转为小写
							key=key.substring(0,1).toLowerCase()+key.substring(1);
							//将属性key,value放入对象
							if(keyFomater==1){
								key=camelToUnderline(key);
							}
							map.put(key, value);
						}
					}catch (Exception e) {
						// TODO: handle exception
						System.out.println("error:"+method);
					}
				}
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}


	/**
	 * 将Map对象通过反射机制转换成Bean对象
	 *
	 * @param map 存放数据的map对象
	 * @param clazz 待转换的class
	 * @return 转换后的Bean对象
	 * @throws Exception 异常
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> clazz) throws Exception {
		Object obj = clazz.newInstance();
		if(map != null && map.size() > 0) {
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey();       //属性名
				Object value = entry.getValue();
				String setMethodName = "set"
						+ propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1);
				//获取属性对应的对象字段
				Field field = getClassField(clazz, propertyName);
				if(field==null)
					continue;
				//获取字段类型
				Class<?> fieldTypeClass = field.getType();
				//根据字段类型进行值的转换
				value = convertValType(value, fieldTypeClass);
				try{
					//调用对象对应的set方法
					clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
				}catch(NoSuchMethodException e){
					e.printStackTrace();
				}
			}
		}
		return obj;
	}


	/**
	 * 将Map对象通过反射机制转换成Bean对象
	 * (下划线转成驼峰)，如果存在相同会发生覆盖
	 * @param map 存放数据的map对象
	 * @param clazz 待转换的class
	 * @return 转换后的Bean对象
	 * @throws Exception 异常
	 */
	public static Object mapToObject2(Map<String, Object> map, Class<?> clazz) throws Exception {
		Object obj = clazz.newInstance();
		if(map != null && map.size() > 0) {
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey();       //属性名
				if(propertyName.indexOf("_")!=-1){
					propertyName=underlineToCamel(propertyName);
				}
				Object value = entry.getValue();
				String setMethodName = "set"
						+ propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1);
				//获取属性对应的对象字段
				Field field = getClassField(clazz, propertyName);
				if(field==null)
					continue;
				//获取字段类型
				Class<?> fieldTypeClass = field.getType();
				//根据字段类型进行值的转换
				value = convertValType(value, fieldTypeClass);
				try{
					//调用对象对应的set方法
					clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
				}catch(NoSuchMethodException e){
					System.err.println(setMethodName);
					e.printStackTrace();
				}
			}
		}
		return obj;
	}




	/**
	 * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
	 *
	 * @param clazz 指定的class
	 * @param fieldName 字段名称
	 * @return Field对象
	 */
	private static Field getClassField(Class<?> clazz, String fieldName) {
		if( Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field []declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null) {// 简单的递归一下
			return getClassField(superClass, fieldName);
		}
		return null;
	}

	/**
	 * 将Object类型的值，转换成bean对象属性里对应的类型值
	 *
	 * @param value Object对象值
	 * @param fieldTypeClass 属性的类型
	 * @return 转换后的值
	 */
	private static Object convertValType(Object value, Class<?> fieldTypeClass) {
		Object retVal = null;
		if(Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = value==null?null:Long.parseLong(value.toString());
		} else if(Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = value==null?null:Integer.parseInt(value.toString());
		} else if(Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = value==null?null:Float.parseFloat(value.toString());
		} else if(Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else if (String.class.getName().equals(fieldTypeClass.getName())){
			retVal = String.valueOf(value);
		} else if (Date.class.getName().equals(fieldTypeClass.getName())){
			if(value instanceof Date){
				retVal=(Date)value;
			}else{
				try {
					retVal=DateX.getDateTime(String.valueOf(value));
				}catch (Exception e){
					return null;
				}
			}
			
		} else {
			retVal = value;
		}
		return retVal;
	}



	public static final char UNDERLINE='_';
	public static String camelToUnderline(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		int len=param.length();
		StringBuilder sb=new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c=param.charAt(i);
			if (Character.isUpperCase(c)){
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static String underlineToCamel(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		int len=param.length();
		StringBuilder sb=new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c=param.charAt(i);
			if (c==UNDERLINE){
				if (++i<len){
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static String underlineToCamel2(String param){
		if (param==null||"".equals(param.trim())){
			return "";
		}
		StringBuilder sb=new StringBuilder(param);
		Matcher mc= Pattern.compile("_").matcher(param);
		int i=0;
		while (mc.find()){
			int position=mc.end()-(i++);
			//String.valueOf(Character.toUpperCase(sb.charAt(position)));
			sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
		}
		return sb.toString();
	}

	public static void main1(String... args) throws Exception{
		//        User user = new User();
		//        user.setId(1);
		//        user.setUserName("hhh");
		//        user.setAge(12);
		//        user.setPassword("password");
		//
		//        //对象转map
		//        Map map = objectToMap(user);
		//        //map转对象
		//        User newUser = (User) mapToObject(map,User.class);

	}

	//新增通过ObjectMapper转换含有复杂属性的mapper--上面的方法仅处理简单属性

	static ObjectMapper mapper=new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

//		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
//      mapper.getDeserializationConfig().with(formatter1).with(formatter2);
//		mapper.setDateFormat(formatter1);//这种方式只能设置一个全局的
		mapper.setDateFormat(new SysDateFormat());//这种方式只能设置一个全局的

	}


	public static Object mapToObject3(Map<String, Object> map, Class<?> clazz) throws Exception {
		String jsonStr=mapper.writeValueAsString(map);
		return mapper.readValue(jsonStr,clazz);
	}

}