package com.bstek.dorado.hibernate.hql;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;

public class HqlVarExpr {

	int index;
	String expr;
	DataType dataType;
	String varName;
	boolean percentStart = false;
	boolean percentEnd = false;
	
	static boolean acceptByExpr(char c) {
		return Character.isJavaIdentifierPart(c) || (c == '.') || (c == '(') || (c == ')') || (c == '%');
	}
	
	HqlVarExpr() {
		
	}
	HqlVarExpr(String expr, int index) {
		this.expr = expr;
		this.index = index;
		try {
			this.parse();
		} catch (Exception e) {
			throw new IllegalArgumentException("Error expression [" + expr + "]", e);
		}
	}
	
	private void parse() throws Exception {
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
		
		varName = toVarNameExpr;
	}
	
	public int getIndex() {
		return index;
	}

	public String getExpr() {
		return expr;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
		result = prime * result + index;
		result = prime * result + (percentEnd ? 1231 : 1237);
		result = prime * result + (percentStart ? 1231 : 1237);
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HqlVarExpr other = (HqlVarExpr) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (expr == null) {
			if (other.expr != null)
				return false;
		} else if (!expr.equals(other.expr))
			return false;
		if (index != other.index)
			return false;
		if (percentEnd != other.percentEnd)
			return false;
		if (percentStart != other.percentStart)
			return false;
		if (varName == null) {
			if (other.varName != null)
				return false;
		} else if (!varName.equals(other.varName))
			return false;
		return true;
	}
	
}
