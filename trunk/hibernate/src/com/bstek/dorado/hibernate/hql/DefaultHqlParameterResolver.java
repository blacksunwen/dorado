package com.bstek.dorado.hibernate.hql;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.entity.EnhanceableEntity;
import com.bstek.dorado.data.entity.EntityEnhancer;

public class DefaultHqlParameterResolver implements HqlParameterResolver {

	public Object parameterValue(final Object parameter, HqlVarExpr hqlParameter) throws Exception {
		String varName = hqlParameter.getVarName();
		Object value = null;
		
		if ("$".equals(varName)) {
			value = parameter;
		} else {
			if (varName.indexOf('.') < 0) {
				value = value(parameter, varName);
			} else {
				String[] fields = StringUtils.split(varName, '.');
				if (fields != null) {
					Object obj = parameter;
					for (String field : fields) {
						obj = value(obj, field);
						if (obj == null) {
							break;
						}
					}

					value = obj;
				}
			}
		}
		
		value = hqlParameter.translatValue(value);
		return value;
	}

	@SuppressWarnings({"rawtypes"})
	protected Object value(final Object parameter, String field) throws Exception {
		if (parameter instanceof EnhanceableEntity) {
			EnhanceableEntity entity = (EnhanceableEntity) parameter;
			EntityEnhancer enhancer = entity.getEntityEnhancer();
			try {
				return enhancer.readProperty(parameter, field, false);
			} catch (Throwable e) {
				throw new Exception(e);
			}
		} else if (parameter instanceof Map) {
			Map m = (Map) parameter;
			return m.get(field);
		} else {
			return PropertyUtils.getProperty(parameter, field);
		}
	}
}
