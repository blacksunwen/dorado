package com.bstek.dorado.config.xml;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-26
 */
public class XmlParserAnnotationHelper implements BeanFactoryAware {
	private static final String PROPERTY_PARSER = "dorado.propertyParser";
	private static final String OBJECT_PARSER = "dorado.prototype.objectParser";

	private BeanFactory beanFactory;
	private Map<Class<?>, ObjectParser> objectParserCache = new HashMap<Class<?>, ObjectParser>();

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param object
	 * @return
	 */
	public TypeAnnotationInfo getTypeAnnotationInfo(Class<?> type)
			throws Exception {
		if (type.getName().startsWith("java.") || type.isArray()
				|| Map.class.isAssignableFrom(type)
				|| Collection.class.isAssignableFrom(type)) {
			return null;
		}

		XmlNode xmlNode = type.getAnnotation(XmlNode.class);
		if (xmlNode != null) {
			TypeAnnotationInfo typeAnnotationInfo = new TypeAnnotationInfo();
			String nodeName = xmlNode.nodeName();
			if (StringUtils.isEmpty(nodeName)) {
				nodeName = ClassUtils.getShortClassName(type);
			}
			typeAnnotationInfo.setNodeName(nodeName);

			String parserId = xmlNode.parser();
			XmlParser xmlParser;
			if (StringUtils.isNotEmpty(parserId)) {
				xmlParser = (XmlParser) beanFactory.getBean(parserId);
			} else {
				xmlParser = getDefaultSubParser(type);
			}
			typeAnnotationInfo.setParser(xmlParser);
			return typeAnnotationInfo;
		} else {
			return null;
		}
	}

	public XmlParser getDefaultPropertyParser(Class<?> cl) {
		return (XmlParser) beanFactory.getBean(PROPERTY_PARSER);
	}

	public XmlParser getDefaultSubParser(Class<?> cl) {
		if (Modifier.isAbstract(cl.getModifiers())) {
			return null;
		} else {
			ObjectParser parser = objectParserCache.get(cl);
			if (parser == null) {
				parser = (ObjectParser) beanFactory.getBean(OBJECT_PARSER);
				parser.setImpl(cl.getName());
				objectParserCache.put(cl, parser);
			}
			return parser;
		}
	}
}
