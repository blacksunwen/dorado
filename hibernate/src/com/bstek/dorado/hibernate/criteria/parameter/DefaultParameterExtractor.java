package com.bstek.dorado.hibernate.criteria.parameter;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.util.Assert;

public class DefaultParameterExtractor implements ParameterExtractor {

	public Object expr(final Object parameter, String expr, String dataTypeName)
			throws Exception {
		if (parameter == null) {
			return null;
		} else {
			Object value = expr(parameter, expr);
			return value(value, dataTypeName);
		}
	}

	public Object expr(Object parameter, String expr) throws Exception {
		if ("$".equals(expr)) {
			return parameter;
		} else {
			String[] fields = StringUtils.split(expr, '.');
			if (fields != null) {
				Object obj = parameter;
				for (String field : fields) {
					obj = field(obj, field);
					if (obj == null) {
						break;
					}
				}

				return obj;
			} else {
				return null;
			}
		}
	}
		
	public String getExpr(Object expr) {
		if (expr instanceof String && ((String)expr).startsWith(":"))
			return ((String)expr).substring(1);
		else 
			return null; 
	}

	public Object value(Object value, String dataTypeName) throws Exception {
		if (StringUtils.isNotEmpty(dataTypeName)) {
			DataType dataType = DataUtils.getDataType(dataTypeName);
			Assert.notNull(dataType, "Unknown DataType '" + dataTypeName + "'.");
			return dataType.fromObject(value);
		} else {
			return value;
		}
	}

	@SuppressWarnings("rawtypes")
	protected Object field(final Object obj, String field) throws Exception{
		if (obj instanceof Map) {
			Map m = (Map) obj;
			return m.get(field);
		} else {
			return PropertyUtils.getProperty(obj, field);
		}
	}

}
