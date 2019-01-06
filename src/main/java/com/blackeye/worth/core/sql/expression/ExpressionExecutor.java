package com.blackeye.worth.core.sql.expression;

public class ExpressionExecutor {

//	public ExpressionExecutor(String expressionName){
//
//		bql = SQLText.get(expressionName);
//
//		if(bql==null){
//			return;
//		}
//
//		expression = Expression.newExpression(bql);
//	}
//
//	Expression expression;
//	String bql;
//	public void execute(Object parameter, Object result){
//
//		if(bql==null){
//			return;
//		}
//
//		ExpressionContext ctx = new ExpressionContext(parameter);
//
//		expression.eval(ctx);
//
//		String sql = expression.getText();
//		List values = expression.getValues();
//
//		SQLExecutor.fetch(result, sql, values);
//
//	}
}
