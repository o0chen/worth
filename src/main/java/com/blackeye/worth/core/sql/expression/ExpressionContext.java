package com.blackeye.worth.core.sql.expression;


import com.blackeye.worth.core.sql.bean.Bean;
import com.blackeye.worth.core.sql.bean.BeanFactory;

import java.util.HashMap;
import java.util.LinkedList;

public class ExpressionContext {
	
	HashMap local = new HashMap();
	LinkedList values = new LinkedList();

	public Object root;
	Object result;

	ParagraphExpression paragraph;
	LineExpression line;
	Expression expr;
	
	public void set(Object root){
		this.root = root;
	}
	
	public ExpressionContext(Object root){
		this.root = root;
	}
	
	public void set(String name, Object value){
		local.put(name, value);
	}

	public Object get(String expr) {
		result = local.get(expr);
		if(result == null){
			Bean bean = BeanFactory.newBean(root);
			result = bean.get(expr);
		}
		return result;
	}
	
	public boolean isEmptyNull = true;

}
