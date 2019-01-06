package com.blackeye.worth.core.sql.expression;

public class RequiredExpression extends ConstantExpression {

	@Override
	public void eval(ExpressionContext context) {
		
		value = context.get(expr);
		parent.valueList.add(value);
		
		text = "?";
		parent.text += text;
		
	}
	@Override
	public RequiredExpression clone(){
		RequiredExpression newone = new RequiredExpression();
		newone.expr = expr;
		return newone;
	}
}
