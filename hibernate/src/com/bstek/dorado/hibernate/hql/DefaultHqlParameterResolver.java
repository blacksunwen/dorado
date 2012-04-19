package com.bstek.dorado.hibernate.hql;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.entity.EnhanceableEntity;
import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.type.DataType;

public class DefaultHqlParameterResolver implements HqlParameterResolver {

	public Object parameterValue(final Object parameter, HqlParameter hqlParameter) throws Exception {
		Object value = hqlParameter.getValue();
		if (value != null) {
			return value;
		} else {
			if (parameter == null) {
				return null;
			} else {
				String expr = hqlParameter.getExpr();
				DataType dataType = hqlParameter.getDataType();
				Object returnValue = null;
				if ("$".equals(expr)) {
					returnValue = parameter;
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

						returnValue = obj;
					}
				}
				
				if (dataType != null) {
					returnValue = dataType.fromObject(returnValue);
				}
				return returnValue;
			}
		}
	}

	@SuppressWarnings({"rawtypes"})
	protected Object value(final Object obj, String field) throws Exception {
		if (obj instanceof EnhanceableEntity) {
			EnhanceableEntity entity = (EnhanceableEntity) obj;
			EntityEnhancer enhancer = entity.getEntityEnhancer();
			try {
				return enhancer.readProperty(obj, field, false);
			} catch (Throwable e) {
				throw new Exception(e);
			}
		} else if (obj instanceof Map) {
			Map m = (Map) obj;
			return m.get(field);
		} else {
			return PropertyUtils.getProperty(obj, field);
		}
	}
}
