package com.bstek.dorado.jdbc.model.autotable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlConstants.Operator;
import com.bstek.dorado.jdbc.type.JdbcType;

public abstract class AbstractSingleColumnMatchRule extends BaseMatchRule {

	private String fromTableName;
	private String columnName;
	
	public void setFromTable(String tableAlias) {
		this.fromTableName = tableAlias;
	}
	
	public String getFromTable() {
		return this.fromTableName;
	}
	
	public void setColumn(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumn() {
		return this.columnName;
	}
	
	protected Value parseValue(JdbcParameterSource parameterSource, Object value) {
		if (value instanceof String) {
			String valueExpr = (String) value;
			if (!StringUtils.isBlank(valueExpr)) {
				if (valueExpr.length() > 1) {
					char opChar = valueExpr.charAt(0);
					if (opChar == ':') {//占位符，从parameterSource中获取值
						return new ParameterValue(parameterSource, valueExpr.substring(1));
					} else if (opChar == '=') {//静态值
						String valueStr = valueExpr.substring(1);
						return new StaticValue(parameterSource, valueStr);
					} 
				}
				
				//SQL片段
				return new SqlTokenValue(parameterSource, valueExpr);
			}
		} else if (value != null) {
			return new StaticValue(parameterSource, value);
		}
		
		return NullValue.INSTANCE;
	}
	
	public abstract static class Value {
		private JdbcParameterSource parameterSource;
		Value(JdbcParameterSource parameterSource) {
			this.parameterSource = parameterSource;
		}
		
		public JdbcParameterSource getParameterSource() {
			return parameterSource;
		}

		public abstract String token(JdbcType jdbcType, Operator operator);
	}
	
	private static class NullValue extends Value {

		NullValue() {
			super(null);
		}

		static final NullValue INSTANCE = new NullValue();

		@Override
		public String token(JdbcType jdbcType, Operator operator) {
			return null;
		}
		
	}
	
	private static class ParameterValue extends Value {
		private String parameterName;
		
		ParameterValue(JdbcParameterSource parameterSource, String parameterName) {
			super(parameterSource);
			this.parameterName = parameterName;
		}

		@Override
		public String token(JdbcType jdbcType, Operator operator) {
			JdbcParameterSource parameterSource = this.getParameterSource();
			if (parameterSource.hasValue(parameterName)) {
				Object parameterValue = parameterSource.getValue(parameterName);
				if (parameterValue != null) {
					return ":" + parameterName;
				}
			}
			
			return null;
		}
		
	}
	
	private static class StaticValue extends Value {
		private Object value;
		
		StaticValue(JdbcParameterSource parameterSource, Object value) {
			super(parameterSource);
			this.value = value;
		}
		
		@Override
		public String token(JdbcType jdbcType, Operator operator) {
			JdbcParameterSource parameterSource = this.getParameterSource();
			Object parameterValue = value;
			if (jdbcType != null) {
				parameterValue = jdbcType.toDB(value);
			}
			if (operator != null) {
				parameterValue = operator.parameterValue(value);
			}
			
			String parameterName = parameterSource.addValue(parameterValue);
			return ":" + parameterName;
		}
	}
	
	private static class SqlTokenValue extends Value {
		private String token;

		SqlTokenValue(JdbcParameterSource parameterSource, String token) {
			super(parameterSource);
			this.token = token;
		}
		
		@Override
		public String token(JdbcType jdbcType, Operator operator) {
			return token;
		}
	}
}
