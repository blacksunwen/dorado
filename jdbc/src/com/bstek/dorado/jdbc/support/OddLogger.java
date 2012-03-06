package com.bstek.dorado.jdbc.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

public class OddLogger implements InitializingBean, ApplicationContextAware{

	private static Log logger = LogFactory.getLog(OddLogger.class);

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isInfoEnabled()) {
			String msg = "Registered TransactionManagers: [";
			String [] names = applicationContext.getBeanNamesForType(PlatformTransactionManager.class);
			msg += StringUtils.join(names, ',');
			msg += "]";
			logger.info(msg);
		}
		
		if (logger.isInfoEnabled()) {
			String msg = "Registered TransactionDefinitions: [";
			String [] names = applicationContext.getBeanNamesForType(TransactionDefinition.class);
			msg += StringUtils.join(names, ',');
			msg += "]";
			logger.info(msg);
		}
	}
	
	
}
