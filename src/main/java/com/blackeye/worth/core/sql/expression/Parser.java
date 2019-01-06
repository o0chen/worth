package com.blackeye.worth.core.sql.expression;


public class Parser {
	private char[] chs;
	private int len;
	private int pos = 0;
	private char chr;
	private int variableStart;
	public String sql;

	private final static char prefix = '{';
	private final static char suffix = '}';
	
	private final static char nullable_flag = ':';
	private final static char static_flag = '$';
	private final static char staticorvariable_flag = '#';

	public Expression parse(final String sql) {
		
		this.sql = sql;
		chs = sql.toLowerCase().toCharArray();
		len = chs.length;
		pos = 0;
		variableStart = 0;

		while (pos < len) {
			chr = chs[pos];
			pos++;
			if (chr == prefix) {
				beforeVariable();
				parseVariable();
			} else if (chr == '\r' || chr == '\n') {
				parseLine();
			}
		}
		parseLine();
		
		chs = null;
		len = 0;
		pos = 0;
		return paragraph;
	}

	private void parseLine() {
		
		int end = pos == len ? len : pos - 1;
		if(variableStart < end){
			String expr = substring(variableStart, end);
			Expression expr0 = new ConstantExpression();
			expr0.expr(expr);
			expr0.parent(line);
		}
		line = new LineExpression();
		line.parent(paragraph);
		variableStart = pos - 1;
	}

	private ParagraphExpression paragraph = new ParagraphExpression();
	private LineExpression line = new LineExpression();
	{
		line.parent(paragraph);
	}

	public Parser() {
	}

	private void parseVariable() {
		chr = chs[pos];
		int end = variableEnd();
		
		String expr;
		Expression expr0;
		
		switch (chr) {
		case static_flag:
			pos++;
			expr0 = new InjectExpression();
			break;
		case nullable_flag:
			pos++;
			expr0 = new RequiredExpression();
			break;
		case staticorvariable_flag:
			pos++;
			expr0 = new InjectOrVariableExpression();
			break;
		default:
			expr0 = new VariableExpression();
			break;
		}

		expr = substring(pos, end);
		expr0.expr(expr);
		expr0.parent(line);
		
		pos = end + 1;
		variableStart = pos;
	}

	private void beforeVariable() {
		
		if (variableStart < pos - 1) {

			String expr = substring(variableStart, pos - 1);
			Expression expr0 = new ConstantExpression();
			expr0.expr(expr);
			expr0.parent(line);
			
		}
		variableStart = pos;
	}

	private String substring(int start, int end) {
		String result = sql.substring(start, end);
		return result;
	}

	private int variableEnd() {
		return charEnd(suffix);
	}

	private int charEnd(char c) {
		int pos = this.pos;
		char chr;
		for (;;) {
			chr = chs[pos];
			if (chr == c) {
				break;
			}
			pos++;
		}
		return pos;
	}
}
