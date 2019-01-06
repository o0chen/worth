package com.blackeye.worth.core.sql.expression;


public class LineExpression extends Expression {
	
	@Override
	public void eval(ExpressionContext context) {

		for(Expression expr : exprList){
			context.expr = expr;
			expr.eval(context);
			if(!result){
				break;
			}
		}
		if(result){
			parent.text += text;
			parent.valueList.addAll(valueList);
		}
		
	}

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append("\tLineExpression:{\r\n");
		for(Expression expr : exprList){
			s.append(expr.toString());
		}
		s.append("\t}\r\n");
		return s.toString();
	}
	
	@Override
	public LineExpression clone(){
		
		LineExpression newone = new LineExpression();
		for(Expression expr : exprList){
			expr = expr.clone();
			expr.parent = newone;
			newone.exprList.add(expr);
		}
		return newone;
	}

}
