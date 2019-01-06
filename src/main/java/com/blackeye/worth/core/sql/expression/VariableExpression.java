package com.blackeye.worth.core.sql.expression;

public class VariableExpression extends ConstantExpression {

	@Override
	public void eval(ExpressionContext context) {
		
		value = context.get(expr);
		
		if(value == null || (context.isEmptyNull && value.equals(""))){
			parent.result = false;
			result = false;
			return;
		}
		
		
		parent.valueList.add(value);
		text = "?";
		parent.text += text;
		
	}
	@Override
	public VariableExpression clone(){
		VariableExpression newone = new VariableExpression();
		newone.expr = expr;
		return newone;
	}
}
