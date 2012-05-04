package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;

public class VarExpr {
	private String expr;
	private DataType dataType;
	private String varName;
	private boolean percentStart = false;
	private boolean percentEnd = false;
	
	static boolean acceptByExpr(char c) {
		return Character.isJavaIdentifierPart(c) || (c == '.') || (c == '(') || (c == ')') || (c == '%');
	}
	
	public static VarExpr create(String expr, JdbcParameterSource parameterSource) throws Exception {
		VarExpr varExpr = new VarExpr(expr);
		varExpr.parse(parameterSource);
		return varExpr;
	}
	
	private void parse(JdbcParameterSource parameterSource) throws Exception {
		String toDataTypeExpr = expr;
		String toOperatorExpr = null;
		String toVarNameExpr = null;
		
		//解析DataType
		if (toDataTypeExpr.charAt(0) == '(') {
			int endIndex = toDataTypeExpr.indexOf(')');
			String dataTypeName = toDataTypeExpr.substring(1, endIndex);
			dataType = DataUtils.getDataType(dataTypeName);
			toOperatorExpr = toDataTypeExpr.substring(endIndex + 1);
		}
		
		//解析[操作符]和[变量名]
		if (toOperatorExpr == null) {
			toOperatorExpr = expr;
		}
		toVarNameExpr = toOperatorExpr;
		if (toOperatorExpr.charAt(0) == '%') {
			percentStart = true;
			toVarNameExpr = toVarNameExpr.substring(1);
		}
		if (toOperatorExpr.charAt(toOperatorExpr.length() - 1) == '%') {
			percentEnd = true;
			toVarNameExpr = toVarNameExpr.substring(0, toVarNameExpr.length()-1);
		}
		
		if (!percentStart && !percentEnd) {
			varName = toVarNameExpr;
		} else {
			Object varValue = parameterSource.getValue(toVarNameExpr);
			varName = parameterSource.addValue(varValue);
		}
	}
	
	public String getExpr() {
		return expr;
	}

	public DataType getDataType() {
		return dataType;
	}

	public boolean isPercentStart() {
		return percentStart;
	}

	public boolean isPercentEnd() {
		return percentEnd;
	}

	private VarExpr(String expr) {
		this.expr = expr;
	}
	
	public String getVarName() {
		return varName;
	}

	public Object translatValue(Object value) {
		if (dataType != null) {
			value = dataType.fromObject(value);
		}
		
		if (!percentStart && !percentEnd) {
			return value;
		}
			
		if (value == null) {
			return "%";
		}
		
		if (percentStart && percentEnd) {
			return "%" + value + "%";
		} 
		
		if (percentStart) {
			return "%" + value;
		} 
		
		if (percentEnd) {
			return value + "%";
		} 
		
		throw new IllegalArgumentException(expr);
	}
	
}
