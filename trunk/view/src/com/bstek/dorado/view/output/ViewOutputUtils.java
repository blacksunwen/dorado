package com.bstek.dorado.view.output;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-24
 */
public abstract class ViewOutputUtils {
	private static Map<Class<?>, TypeAnnotationInfo> typeAnnotationInfoCache = new HashMap<Class<?>, TypeAnnotationInfo>();

	private static final String SUB_COMPONENT_OUTPUTTER = "dorado.subComponentOutputter";

	/**
	 * 输出HTML中JavaScript标记的开始部分。
	 * 
	 * @throws IOException
	 */
	public static void outputScriptBeginTag(Writer writer) throws IOException {
		writer.write("<script language=\"javascript\" type=\"text/javascript\">\n");
	}

	/**
	 * 输出HTML中JavaScript标记的结束部分。
	 * 
	 * @throws IOException
	 */
	public static void outputScriptEndTag(Writer writer) throws IOException {
		writer.write("</script>\n");
	}

	/**
	 * 以安全的方式向HTML中输出一个段文本。
	 * 
	 * @throws IOException
	 */
	public static void outputString(Writer writer, String s) throws IOException {
		writer.write(StringEscapeUtils.escapeHtml(s));
	}

	/**
	 * 将Java对象的某属性输出为JavaScript属性。
	 * 
	 * @param writer
	 *            Writer
	 * @param owner
	 *            该属性在JavaScript种的宿主。
	 * @param object
	 *            Java对象。
	 * @param property
	 *            要输出的属性名。
	 * @param escapeValue
	 *            默认值。如果Java对象的实际属性值与默认值一致则忽略此次输出操作。
	 * @throws Exception
	 * @see #DEFAULT_VALUE
	 */
	public static void outputProperty(Writer writer, String owner,
			Object object, String property, Object escapeValue)
			throws Exception {
		Object value = PropertyUtils.getProperty(object, property);
		if (value == escapeValue
				|| (escapeValue != null && escapeValue.equals(value))) {
			return;
		}

		writer.write(owner);
		writer.write('.');
		writer.write(property);
		writer.write('=');

		if (value == null) {
			writer.write("null");
		} else if (value instanceof String) {
			writer.write("\"");
			writer.write((String) value);
			writer.write("\"");
		} else if (value instanceof Number || value instanceof Boolean) {
			writer.write(value.toString());
		} else if (value instanceof Date) {
			writer.write("new Date(");
			writer.write(String.valueOf(((Date) value).getTime()));
			writer.write(")");
		} else {
			writer.write("\"");
			writer.write(value.toString());
			writer.write("\"");
		}
		writer.write(";\n");
	}

	/**
	 * @param object
	 * @return
	 */
	public static TypeAnnotationInfo getTypeAnnotationInfo(
			BeanFactory beanFactory, Object object) throws Exception {
		Class<? extends Object> cl = object.getClass();
		if (cl.getName().startsWith("java.") || cl.isArray()
				|| Map.class.isAssignableFrom(cl)
				|| Collection.class.isAssignableFrom(cl)) {
			return null;
		}

		TypeAnnotationInfo typeAnnotationInfo = null;
		if (typeAnnotationInfoCache.containsKey(cl)) {
			typeAnnotationInfo = typeAnnotationInfoCache.get(cl);
		} else {
			Outputter outputter = null;
			Map<String, Object> properties = null;

			ViewObject viewObject = cl.getAnnotation(ViewObject.class);
			if (viewObject != null) {
				if (StringUtils.isNotEmpty(viewObject.outputter())) {
					outputter = (Outputter) beanFactory.getBean(viewObject
							.outputter());
				}
			}

			BeanInfo beanInfo = Introspector.getBeanInfo(cl);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				Method readMethod = propertyDescriptor.getReadMethod();
				if (readMethod != null) {
					if (readMethod.getDeclaringClass() != cl) {
						readMethod = cl.getMethod(readMethod.getName(),
								readMethod.getParameterTypes());
					}

					ViewAttribute viewAttribute = readMethod
							.getAnnotation(ViewAttribute.class);
					if (viewAttribute != null) {
						Object propertyConfig = null;
						if (!viewAttribute.output()) {
							propertyConfig = OutputUtils.IGNORE_VALUE;
						} else if (StringUtils.isNotEmpty(viewAttribute
								.outputter())) {
							String beanId = viewAttribute.outputter();
							if (OutputUtils.IGNORE_VALUE.equals(beanId)) {
								propertyConfig = OutputUtils.IGNORE_VALUE;
							} else {
								propertyConfig = beanFactory.getBean(beanId);
							}
						} else if (StringUtils.isNotEmpty(viewAttribute
								.referenceComponentName())) {
							propertyConfig = beanFactory
									.getBean("dorado.componentReferencePropertyOutputter");
						} else {
							Class<?> propertyType = propertyDescriptor
									.getPropertyType();
							boolean isCollection = Collection.class
									.isAssignableFrom(propertyType);
							if (isCollection) {
								propertyType = null;
								Type parameterType = readMethod
										.getGenericReturnType();
								if (parameterType instanceof ParameterizedType) {
									propertyType = (Class<?>) ((ParameterizedType) parameterType)
											.getActualTypeArguments()[0];
								}
							}
							if (propertyType != null
									&& Component.class
											.isAssignableFrom(propertyType)) {
								propertyConfig = beanFactory
										.getBean(SUB_COMPONENT_OUTPUTTER);
							}

							if (propertyConfig == null) {
								propertyConfig = StringUtils.defaultString(
										viewAttribute.defaultValue(),
										OutputUtils.ESCAPE_VALUE);
							}
						}

						if (properties == null) {
							properties = new HashMap<String, Object>();
						}
						properties.put(propertyDescriptor.getName(),
								propertyConfig);
					}
				}
			}

			if (outputter != null || properties != null) {
				typeAnnotationInfo = new TypeAnnotationInfo();
				typeAnnotationInfo.setOutputter(outputter);
				typeAnnotationInfo.setProperties(properties);
				typeAnnotationInfoCache.put(cl, typeAnnotationInfo);
			}
		}
		return typeAnnotationInfo;
	}

	/**
	 * 将Java对象的某属性输出为JavaScript属性。
	 * 
	 * @param writer
	 *            Writer
	 * @param owner
	 *            该属性在JavaScript种的宿主。
	 * @param object
	 *            Java对象。
	 * @param property
	 *            要输出的属性名。
	 */
	public static void outputProperty(Writer writer, String owner,
			Object object, String property) throws Exception {
		outputProperty(writer, owner, object, property, null);
	}
}
