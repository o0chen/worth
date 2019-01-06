package com.blackeye.worth.core.sql.expression;


public class ParagraphExpression extends Expression {

	@Override
	public void eval(ExpressionContext context) {
		
		context.paragraph = this;
		
		for(Expression expr : exprList){
			context.line = (LineExpression) expr;
			expr.eval(context);
		}
		
	}
	
	@Override
	public ParagraphExpression clone(){
		ParagraphExpression newone = new ParagraphExpression();
		for(Expression expr : exprList){
			expr = expr.clone();
			expr.parent = newone;
			newone.exprList.add(expr);
		}
		return newone;
	}

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append("ParagraphExpression:{\r\n");
		for(Expression expr : exprList){
			s.append(expr.toString());
		}
		s.append("}\r\n");
		return s.toString();
	}

}
