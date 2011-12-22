package com.bstek.dorado.data.resolver.manager;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.data.resolver.DataResolver;

/**
 * 用于利用外部的Spring配置文件完成DataResolver类型注册功能的辅助类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 16, 2007
 * @see com.bstek.dorado.data.Resolver.manager.DataResolverTypeRegistry
 */
public class DataResolverTypeRegister implements InitializingBean {
	private static final Log logger = LogFactory
			.getLog(DataResolverTypeRegister.class);

	private DataResolverTypeRegistry dataResolverTypeRegistry;

	private String classType;
	private String type;

	/**
	 * 设置DataResolver的类型注册管理器。
	 */
	public void setDataResolverTypeRegistry(
			DataResolverTypeRegistry dataResolverTypeRegistry) {
		this.dataResolverTypeRegistry = dataResolverTypeRegistry;
	}

	/**
	 * 设置DataResolver的Class类型。
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	/**
	 * 设置类型名。
	 */
	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		try {
			Class<? extends DataResolver> cl = ClassUtils.getClass(classType);
			DataResolverTypeRegisterInfo registerInfo = new DataResolverTypeRegisterInfo(
					type, cl);
			dataResolverTypeRegistry.registerType(registerInfo);
		} catch (ClassNotFoundException e) {
			logger.error(e, e);
		}
	}
}
