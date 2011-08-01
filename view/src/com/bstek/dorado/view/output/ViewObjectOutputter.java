package com.bstek.dorado.view.output;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;
import com.bstek.dorado.view.el.OutputableExpressionUtils;

/**
 * 用于将一个Java对象输出成为View对象的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 1, 2008
 */
public class ViewObjectOutputter extends ObjectOutputter implements
		BeanFactoryAware {
	private final static String[] EMPTY_PROPERTYNAMES = new String[0];
	private final static HashMap<Class<?>, String[]> PROPERTYNAMES_CACHE = new HashMap<Class<?>, String[]>();

	private BeanFactory beanFactory;
	private boolean escapeable = true;
	private boolean useTypedJson = true;
	private String prototype;
	private String configType;
	private String defaultConfigType;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public boolean isEscapeable() {
		return escapeable;
	}

	public void setEscapeable(boolean escapeable) {
		this.escapeable = escapeable;
	}

	public boolean isUseTypedJson() {
		return useTypedJson;
	}

	public void setUseTypedJson(boolean useTypedJson) {
		this.useTypedJson = useTypedJson;
	}

	/**
	 * 返回该对象在客户端中的类型名。
	 */
	public String getPrototype() {
		return prototype;
	}

	/**
	 * 设置该对象在客户端中的类型名。
	 */
	public void setPrototype(String prototype) {
		this.prototype = prototype;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getDefaultConfigType() {
		return defaultConfigType;
	}

	public void setDefaultConfigType(String defaultConfigType) {
		this.defaultConfigType = defaultConfigType;
	}

	public void output(Object object, OutputContext context) throws Exception {
		if (object != null) {
			JsonBuilder json = context.getJsonBuilder();
			if (object instanceof Collection<?>) {
				if (escapeable) {
					json.escapeableArray();
				} else {
					json.array();
				}
				for (Object element : (Collection<?>) object) {
					if (element instanceof Ignorable
							&& ((Ignorable) element).isIgnored()) {
						continue;
					}
					outputObject(element, context);
				}
				json.endArray();
			} else {
				outputObject(object, context);
			}
		} else {
			if (!escapeable)
				context.getWriter().append("null");
		}
	}

	/**
	 * 将一个Java的POJO对象输出成为JSON对象。
	 */
	protected void outputObject(Object object, OutputContext context)
			throws IOException, Exception {
		Writer writer = context.getWriter();
		if (object != null) {
			// 此段逻辑的本意是根据对象中Annotation的定义将输出任务转发给其他的输出器。
			// 但会造成SubViewHolder中的subView使用错误的TopViewOutputter导致问题，且目前认为此段逻辑事实可以避免。
			// 11/03/04
			/*
			 * Outputter outputter; TypeAnnotationInfo typeAnnotationInfo =
			 * ViewOutputUtils .getTypeAnnotationInfo(beanFactory, object); if
			 * (typeAnnotationInfo != null) { outputter =
			 * typeAnnotationInfo.getOutputter(); if (outputter != null &&
			 * outputter != this) { outputter.output(object, context); return; }
			 * }
			 */

			String prototype = this.prototype;
			String configType = this.configType;

			ViewObject viewObject = object.getClass().getAnnotation(
					ViewObject.class);
			if (viewObject != null) {
				if (StringUtils.isEmpty(prototype)) {
					prototype = viewObject.prototype();
				}
				if (StringUtils.isEmpty(configType)) {
					configType = viewObject.shortTypeName();
				}
			}
			configType = StringUtils.defaultIfEmpty(configType, null);
			if (defaultConfigType != null
					&& defaultConfigType.equals(configType)) {
				configType = null;
			}

			JsonBuilder json = context.getJsonBuilder();
			if (!useTypedJson && StringUtils.isNotEmpty(prototype)) {
				writer.write("new ");
				writer.write(prototype);
				writer.write("(");
			}

			if (escapeable) {
				json.escapeableObject();
			} else {
				json.object();
			}
			if (useTypedJson && StringUtils.isNotEmpty(configType)) {
				json.key("$type").value(configType);
			}

			outputObjectProperties(object, context);
			json.endObject();

			if (!useTypedJson && StringUtils.isNotEmpty(prototype)) {
				writer.write(")");
			}
		} else {
			writer.append("null");
		}
	}

	protected String[] getOutputProperties(Object object) {
		String[] propertyNames;
		if (object instanceof Map<?, ?>) {
			propertyNames = ((Map<?, ?>) object).keySet().toArray(
					EMPTY_PROPERTYNAMES);
		} else {
			Class<?> type = object.getClass();
			if (ProxyBeanUtils.isProxy(type)) {
				type = type.getSuperclass();
			}

			propertyNames = PROPERTYNAMES_CACHE.get(type);
			if (propertyNames == null) {
				List<String> propertyNameList = new ArrayList<String>();
				for (PropertyDescriptor propertyDescriptor : PropertyUtils
						.getPropertyDescriptors(type)) {
					String name = propertyDescriptor.getName();
					if (!"class".equals(name)) {
						propertyNameList.add(name);
					}
				}
				if (propertyNameList.isEmpty()) {
					propertyNames = EMPTY_PROPERTYNAMES;
				} else {
					propertyNames = propertyNameList
							.toArray(EMPTY_PROPERTYNAMES);
				}
				PROPERTYNAMES_CACHE.put(type, propertyNames);
			}
		}
		return propertyNames;
	}

	/**
	 * 输出一个Java的POJO对象中在{@link #getConfigProperties()}配置过的各个POJO属性。
	 */
	protected void outputObjectProperties(Object object, OutputContext context)
			throws Exception {
		Map<?, ?> beanMap;
		TypeAnnotationInfo typeAnnotationInfo = null;
		if (object instanceof Map<?, ?>) {
			beanMap = (Map<?, ?>) object;
		} else {
			beanMap = BeanMap.create(object);
			typeAnnotationInfo = ViewOutputUtils.getTypeAnnotationInfo(
					beanFactory, object);
		}

		Map<String, Object> configProperties = getConfigProperties();
		PropertyOutputter defaultPropertyOutputter = (PropertyOutputter) configProperties
				.get("*");
		Assert.notNull(defaultPropertyOutputter,
				"[defaultPropertyOutputter] undefined.");

		JsonBuilder json = context.getJsonBuilder();
		for (String property : getOutputProperties(object)) {
			Object escapeValue = configProperties.get(property);
			if (escapeValue == null && typeAnnotationInfo != null) {
				escapeValue = typeAnnotationInfo.getPropertyConfig(property);
			}

			if (escapeValue != null) {
				if (escapeValue instanceof VirtualPropertyOutputter) {
					continue;
				} else if (escapeValue instanceof PropertyOutputter) {
					if (escapeValue instanceof DataPropertyOutputter) {
						DataPropertyOutputter propertyOutputter = ((DataPropertyOutputter) escapeValue);
						if (!propertyOutputter.isEvaluateExpression()) {
							OutputableExpressionUtils
									.disableOutputableExpression();
						}

						Object value = null;
						try {
							value = beanMap.get(property);
							if (OutputableExpressionUtils.getSkipedExpression() != null) {
								value = OutputableExpressionUtils
										.getSkipedExpression();
							}
						} finally {
							if (!propertyOutputter.isEvaluateExpression()) {
								OutputableExpressionUtils
										.enableOutputableExpression();
							}
						}

						if (!propertyOutputter.isEscapeValue(value)) {
							json.escapeableKey(property);
							propertyOutputter.output(value, context);
							json.endKey();
						}
					} else {
						PropertyOutputter propertyOutputter = ((PropertyOutputter) escapeValue);
						Object value = beanMap.get(property);
						if (!propertyOutputter.isEscapeValue(value)) {
							json.escapeableKey(property);
							propertyOutputter.output(value, context);
							json.endKey();
						}
					}
				} else {
					Object value = beanMap.get(property);
					if (!OutputUtils.isEscapeValue(value, escapeValue)) {
						json.escapeableKey(property);
						defaultPropertyOutputter.output(value, context);
						json.endKey();
					}
				}
			} else {
				Object value = beanMap.get(property);
				if (value != null && EntityUtils.isSimpleValue(value)) {
					if (!OutputUtils.isEscapeValue(value)) {
						json.escapeableKey(property);
						defaultPropertyOutputter.output(value, context);
						json.endKey();
					}
				}
			}
		}

		for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
			Object escapeValue = entry.getValue();
			if (escapeValue instanceof VirtualPropertyOutputter) {
				String property = entry.getKey();
				VirtualPropertyOutputter propertyOutputter = (VirtualPropertyOutputter) escapeValue;
				propertyOutputter.output(object, property, context);
			}
		}
	}
}