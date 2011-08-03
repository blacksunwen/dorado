package com.bstek.dorado.hibernate.hql;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultHqlParameterResolver implements HqlParameterResolver {

	public Object filterValue(AutoFilterVar filter, HqlParameter hqlParameter) 
		throws Exception {
		String expr = hqlParameter.getExpr();
		Object obj = AutoFilterVar.getValue(filter, expr);
		return obj;
	}
	
	public Object parameterValue(final Object parameter, HqlParameter hqlParameter) throws Exception {
		Object value = hqlParameter.getValue();
		if (value != null) {
			return value;
		} else {
			if (parameter == null) {
				return null;
			} else {
				String expr = hqlParameter.getExpr();
				if ("$".equals(expr)) {
					return parameter;
				} else {
					String[] fields = StringUtils.split(expr, '.');
					if (fields != null) {
						Object obj = parameter;
						for (String field : fields) {
							obj = value(obj, field);
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
		}
	}

	@SuppressWarnings({"rawtypes"})
	protected Object value(final Object obj, String field) throws Exception {
		if (obj instanceof Map) {
			Map m = (Map) obj;
			return m.get(field);
		} else {
			return PropertyUtils.getProperty(obj, field);
		}
	}
}
