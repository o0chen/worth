package com.blackeye.worth.core.sql.expression;

public class ConstantExpression extends Expression {

	@Override
	public void eval(ExpressionContext context) {
		text = expr;
		value = expr;
		parent.text += expr;
	}
	
	@Override
	public String toString(){
		return "\t\t"+this.getClass().getSimpleName()+":{\r\n\t\t\t" + expr.replace("\r", "\\r").replace("\n", "\\n") + "\r\n\t\t}\r\n";
	}
	
	@Override
	public ConstantExpression clone(){
		ConstantExpression newone = new ConstantExpression();
		newone.expr = expr;
		return newone;
	}
}
