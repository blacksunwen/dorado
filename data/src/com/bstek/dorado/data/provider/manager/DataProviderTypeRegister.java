package com.bstek.dorado.data.provider.manager;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.bstek.dorado.config.Parser;
import com.bstek.dorado.data.config.xml.GenericObjectParser;

/**
 * 用于利用外部的Spring配置文件完成DataProvider类型注册功能的辅助类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 16, 2007
 * @see com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry
 */
public class DataProviderTypeRegister implements BeanFactoryPostProcessor {
	private static final Log logger = LogFactory
			.getLog(DataProviderTypeRegister.class);

	private static final String DATA_PROVIDER_PARSER = "dorado.prototype.dataProviderParser";

	private DataProviderTypeRegistry dataProviderTypeRegistry;
	private String type;
	private String classType;
	private Parser parser;

	/**
	 * 设置DataProvider的类型注册管理器。
	 */
	public void setDataProviderTypeRegistry(
			DataProviderTypeRegistry dataProviderTypeRegistry) {
		this.dataProviderTypeRegistry = dataProviderTypeRegistry;
	}

	/**
	 * 返回类型名。
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型名。
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 返回DataProvider的Class类型。
	 */
	public String getClassType() {
		return classType;
	}

	/**
	 * 设置DataProvider的Class类型。
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	/**
	 * 返回对应的配置解析器。
	 */
	public Parser getParser() {
		return parser;
	}

	/**
	 * 设置对应的配置解析器。
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Class<?> cl = ClassUtils.getClass(classType);
			DataProviderTypeRegisterInfo registerInfo = new DataProviderTypeRegisterInfo(
					type, cl);

			if (parser == null) {
				parser = (Parser) beanFactory.getBean(DATA_PROVIDER_PARSER);
			}
			if (parser instanceof GenericObjectParser) {
				((GenericObjectParser) parser).setDefaultImpl(classType);
			}

			registerInfo.setParser(parser);
			dataProviderTypeRegistry.registerType(registerInfo);
		} catch (ClassNotFoundException e) {
			logger.error(e, e);
		}
	}
}
