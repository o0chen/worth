package com.blackeye.worth.core.sql.bean;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
	
	public static Bean newBean(Object obj){
		
		if (obj instanceof Bean){
			return (Bean)obj;
		}

		if(obj instanceof Map){
			return new BeanMap((Map) obj);
		}
		
		if(obj instanceof ResultSet){
			return new BeanResultSet((ResultSet) obj);
		}

		if(obj instanceof MultipartHttpServletRequest){
			return new BeanRequest((MultipartHttpServletRequest) obj);
		}

		if(obj instanceof HttpSession){
			return new SessionBean((HttpSession) obj);
		}

		if (obj instanceof JSONObject){
			return new BeanJSONObject((JSONObject)obj);
		}
		
		return new BeanObject(obj);
	}
	

	public static Map toMap(Object obj){
		
		Map r = new HashMap();
		Bean b = newBean(r);
		b.setAll(obj);
		return r;

	}
}
