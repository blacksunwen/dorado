package com.bstek.dorado.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.type.StringRepresentableType;
import org.hibernate.type.Type;

import com.bstek.dorado.hibernate.provider.UserCriteria;

public abstract class BaseUserCriteriaProcessor<T> {

	protected T createWhereToken(String associationPath, String expr, 
			Type type, UserCriteria.Parameter parameter) {
		String expr2 = expr.trim();
		if (StringUtils.isNotEmpty(expr2)) {
			if (expr2.length() > 2) {
				if (expr2.startsWith(">=") && expr2.indexOf('%') < 0) {
					//对于">=12%3"是like还是>=呢？当然是like，因为>=中是不会出现%的
					Object value = toValue(expr.substring(2), type);
					if (value != null) {
						return toGE(associationPath, value, parameter);
					} else {
						return null;
					}
				} else if (expr2.startsWith("<=") && expr2.indexOf('%') < 0) { 
					//对于"<=12%3"是like还是<=呢？当然是like，因为<=中是不会出现%的
					Object value = toValue(expr.substring(2), type);
					if (value != null) {
						return toLE(associationPath, value, parameter);
					} else {
						return null;
					}
				} else if (expr2.startsWith("<>") && expr2.indexOf('%') < 0) {
					//对于"<>12%3"是like还是<>呢？当然是like，因为<>中是不会出现%的
					Object value = toValue(expr.substring(2), type);
					if (value != null) {
						return toNE(associationPath, value, parameter);
					} else {
						return null;
					}
				} else if (expr2.startsWith("[") && expr2.endsWith("]")
						&& expr2.indexOf('%') < 0) {   
					//对于"[12%3,333]"是like还是between呢？当然是like，因为between中是不会出现%的
					Object[] value = toBetweenValue(expr, type);
					if (value != null && value.length == 2) {
						return toBetween(associationPath, value[0], value[1], parameter);
					} else {
						return null;
					}
				} else if (expr2.startsWith("(") && expr2.endsWith(")")
						&& expr2.indexOf("%%") < 0) {
					//1.需要转义逗号","，因为in中可能会出现逗号，为了更好的支持这种情况，运行自定义分隔符
					//例如："(, 123,234)"的分隔符是','，"(| 123|,sdfd|k,k.k)"的分隔符是'|'
					//2.对于"(12%3,123)"是like还是in呢？是in，如果希望是like，
					//那么使用"%%"，即"(12%%3,123)"
					Object[] objects = toInValue(expr, type);
					if (objects != null && objects.length > 0) {
						return toIn(associationPath, objects, parameter);
					} else {
						return null;
					}
				}
			}
			
			if (expr2.length() > 1) {
				String op_ = expr2.substring(0, 1);
				if ("=".equals(op_)) {
					Object value = toEQValue(expr, type);
					return toEQ(associationPath, value, parameter);
				} else if (">".equals(op_) && expr2.indexOf('%') < 0) {
					Object value = toValue(expr.substring(1), type);
					return toGT(associationPath, value, parameter);
				} else if ("<".equals(op_) && expr2.indexOf('%') < 0) {
					Object value = toValue(expr.substring(1), type);
					return toLT(associationPath, value, parameter);
				} else if (expr2.indexOf('%') >= 0) {
					//不需要转义，如果%不是用来表示like，那么可以在前面添加=表示相等
					//也就是说"=55%"是=而不是like
					String value = toLikeValue(expr);
					return toLike(associationPath, value, parameter);
				} 
			}
			
			return toDefault(associationPath, expr2, type, parameter);
		}
		return null;
	}
	
	protected abstract T toDefault(String propertyName, String expr, Type type, UserCriteria.Parameter parameter);
	
	protected abstract T toBetween(String propertyName, Object value1, Object value2, UserCriteria.Parameter parameter);
	protected Object[] toBetweenValue(String expr, Type type) {
		String value = expr.substring(1, expr.length()-1);
		String[] values = StringUtils.split((String)value, ',');
		if (values.length == 2) {
			Object value1 = values[0].trim();
			Object value2 = values[1].trim();
			if (type != null && type instanceof StringRepresentableType) {
				StringRepresentableType<?> strType = (StringRepresentableType<?>) type;
				value1 = strType.fromStringValue((String)value1);
				value2 = strType.fromStringValue((String)value2);
			}
			return new Object[]{value1, value2};
		}
		return null;
	}
	
	protected abstract T toIn(String propertyName, Object[] objects, UserCriteria.Parameter parameter);
	protected Object[] toInValue(String expr, Type type) {
		String value = expr.substring(1, expr.length() - 1);
		char spliter = ','; 
		if (value.length() > 2 && value.charAt(1)==' ') {
			spliter = value.charAt(0);
		}
		
		String[] values = StringUtils.split((String)value, spliter);
		Object[] objects = new Object[values.length];
		for (int i=0; i<values.length; i++) {
			String v= values[i];
			if (type != null && type instanceof StringRepresentableType) {
				StringRepresentableType<?> strType = (StringRepresentableType<?>) type;
				Object obj = strType.fromStringValue(v);
				objects[i] = obj;
			}
		}
		return objects;
	}

	protected abstract T toEQ(String propertyName, Object value, UserCriteria.Parameter parameter);
	protected Object toEQValue(String expr, Type type) {
		Object value = toValue((expr.charAt(0)!='=') ? expr : expr.substring(1), type);
		return value;
	}

	protected abstract T toLike(String propertyName, String value, UserCriteria.Parameter parameter);
	protected String toLikeValue(String expr) {
		return expr.indexOf('%') >= 0 ? expr: "%" + expr + "%";
	}

	protected abstract T toLT(String propertyName, Object value, UserCriteria.Parameter parameter);
	protected Object toValue(String expr, Type type) {
		Object value = expr;
		if (StringUtils.isEmpty(expr)) {
			return null;
		} 
		if (type != null && type instanceof StringRepresentableType) {
			StringRepresentableType<?> strType = (StringRepresentableType<?>)type;
			value = strType.fromStringValue((String)value);
		}
		return value;
	}

	protected abstract T toGT(String propertyName, Object value, UserCriteria.Parameter parameter);

	protected abstract T toNE(String propertyName, Object value, UserCriteria.Parameter parameter);

	protected abstract T toLE(String propertyName, Object value, UserCriteria.Parameter parameter);

	protected abstract T toGE(String propertyName, Object value, UserCriteria.Parameter parameter);
}
