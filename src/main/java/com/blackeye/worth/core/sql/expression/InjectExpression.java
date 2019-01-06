package com.blackeye.worth.core.sql.expression;

public class InjectExpression extends ConstantExpression {

	@Override
	public void eval(ExpressionContext context) {
		value = context.get(expr);
		if(value == null){
			text = "";
			return;
		}
		text = value.toString();
		parent.text += text;
	}
	@Override
	public InjectExpression clone(){
		InjectExpression newone = new InjectExpression();
		newone.expr = expr;
		return newone;
	}
}
