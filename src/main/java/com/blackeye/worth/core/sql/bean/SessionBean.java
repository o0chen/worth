package com.blackeye.worth.core.sql.bean;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class SessionBean extends BeanBase {

	HttpSession session;

	public SessionBean(HttpSession session) {
		this.session = session;
	}

	@Override
	public Object get(String name) {
		return session.getAttribute(name);
	}

	@Override
	public boolean contains(String name) {
		return session.getAttribute(name) != null;
	}

	@Override
	public void set(String name, Object object) {
		session.setAttribute(name, object);
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet();
		
		Enumeration<String> enu = session.getAttributeNames();
		while(enu.hasMoreElements()){
			set.add(enu.nextElement());
		}
		
		return set;
	}

	@Override
	public Object get() {
		return session;
	}

}
