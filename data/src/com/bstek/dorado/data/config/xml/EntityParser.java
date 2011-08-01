package com.bstek.dorado.data.config.xml;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.config.EntityExpressionMethodInterceptor;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * 实体数据的解析器。该解析器声明的解析结果为单个的Map或Bean。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 31, 2007
 */
public class EntityParser extends DataElementParserSupport {
	private static final Class<?> DEFAULT_MAP_TYPE = HashMap.class;

	@Override
	protected Object internalParse(Node node, DataParseContext context)
			throws Exception {
		Element element = (Element) node;

		DefinitionReference<DataTypeDefinition> dataTypeRef = dataObjectParseHelper
				.getReferencedDataType(DataXmlConstants.ATTRIBUTE_DATA_TYPE,
						null, element, context);
		if (dataTypeRef == null) {
			dataTypeRef = context.getCurrentDataType();
		}
		context.setCurrentDataType(null);

		Class<?> cl = null;
		if (dataTypeRef != null) {
			DataTypeDefinition dataTypeDefinition = dataTypeRef.getDefinition();
			cl = dataTypeDefinition.getMatchType();
		}
		if (cl != null) {
			if ((cl.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
				if (Map.class.isAssignableFrom(cl)) {
					cl = DEFAULT_MAP_TYPE;
				}
			}
		} else {
			cl = DEFAULT_MAP_TYPE;
		}

		Map<String, Expression> expressionProperties = new HashMap<String, Expression>();
		Map<String, Object> valueProperties = new HashMap<String, Object>();
		Map<String, Object> properties = parseProperties(element, context);
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String property = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Expression) {
				expressionProperties.put(property, (Expression) value);
			} else {
				valueProperties.put(property, value);
			}
		}

		Object entity;
		if (expressionProperties.isEmpty()) {
			entity = cl.newInstance();
		} else {
			EntityExpressionMethodInterceptor entityExpressionMethodInterceptor = new EntityExpressionMethodInterceptor(
					expressionProperties);
			entity = BeanFactoryUtils.getBean(cl.getName(),
					entityExpressionMethodInterceptor);

			if (entity instanceof Map<?, ?>) {
				entityExpressionMethodInterceptor.setDisabled(true);
				for (String property : expressionProperties.keySet()) {
					PropertyUtils.setProperty(entity, property, null);
				}
				entityExpressionMethodInterceptor.setDisabled(false);
			}
		}

		for (Map.Entry<String, Object> entry : valueProperties.entrySet()) {
			String property = entry.getKey();
			Object value = entry.getValue();
			PropertyUtils.setProperty(entity, property, value);
		}

		context.restoreCurrentDataType();
		return entity;
	}

}
