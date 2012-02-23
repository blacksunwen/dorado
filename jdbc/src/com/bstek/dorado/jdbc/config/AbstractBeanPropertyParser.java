package com.bstek.dorado.jdbc.config;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.core.bean.BeanFactoryUtils;

/**
 * 抽象的获取Bean的ParpertyParser
 * 
 * @author mark.li@bstek.com
 * @param <T>
 * @see com.bstek.dorado.core.bean.BeanFactoryUtils
 */
public abstract class AbstractBeanPropertyParser<T> extends PropertyParser {

	@SuppressWarnings("unchecked")
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String beanName = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(beanName)) {
			return (T)BeanFactoryUtils.getBean(beanName);
		} else {
			return null;	
		}
	}
}
