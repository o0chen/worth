package com.blackeye.worth.core.sql.expression;

import java.util.LinkedList;
import java.util.List;

public abstract class Expression {
	
	
	public static Expression newExpression(String sql){
		
		Parser parser = new Parser();
		Expression expr = parser.parse(sql);
		return expr;
		
	}
	
	protected LinkedList<Expression> exprList;
	protected LinkedList valueList;
	
	protected String text;
	protected String expr;
	protected Object value;
	
	protected Expression parent;
	protected boolean result;

	public abstract void eval(ExpressionContext context);
	@Override
	public abstract Expression clone();
	
	protected Expression(){
		init();
	}
	protected void init(){
		exprList = new LinkedList();
		valueList = new LinkedList();
		text = "";
		expr = null;
		value = null;
		result = true;
	}
	protected void parent(Expression parent){
		this.parent = parent;
		parent.exprList.add(this);
	}
	protected void expr(String expr){
		this.expr = expr;
	}
	public String getText(){
		return text;
	}
	public List getValues(){
		return valueList;
	}
	
}
