package com.blackeye.worth.core.sql.expression;

public class InjectOrVariableExpression extends ConstantExpression {

	@Override
	public void eval(ExpressionContext context) {
		value = context.get(expr);
		if(value == null || (context.isEmptyNull && value.equals(""))){
			parent.result = false;
			result = false;
			return;
		}

		text = value.toString();
		parent.text += text;

	}
	@Override
	public InjectOrVariableExpression clone(){
		InjectOrVariableExpression newone = new InjectOrVariableExpression();
		newone.expr = expr;
		return newone;
	}
}
