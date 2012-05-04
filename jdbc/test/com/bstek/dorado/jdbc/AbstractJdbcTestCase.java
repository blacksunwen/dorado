package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

public abstract class AbstractJdbcTestCase extends ConfigManagerTestSupport {

	protected AbstractJdbcTestCase () {
		super();
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/jdbc/context.xml");
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/idesupport/context.xml");
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/view/context.xml");
		for (String location: getExtConfigLocations()) {
			this.addExtensionContextConfigLocation(location);
		}
	}
	
	@Override
	protected void setUp() throws Exception {
		TestJdbcUtils.setCurrentTestClass(this.getClass());
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		TestJdbcUtils.setCurrentTestClass(null);
		super.tearDown();
	}

	protected JdbcEnviroment getJdbcEnviroment() throws Exception {
		JdbcEnviromentManager manager = (JdbcEnviromentManager)Context.getCurrent().getServiceBean("jdbc.enviromentManager");
		return manager.getDefault();
	}
	
	public JdbcDao getDao() {
		try {
			return (JdbcDao)Context.getCurrent().getServiceBean("jdbcDao");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected JdbcDataProvider getProvider(String name) throws Exception {
		JdbcDataProvider provider = (JdbcDataProvider)this.getDataProviderManager().getDataProvider(name);
		Assert.assertNotNull("no provider named [" + name + "] defined.", provider);
		
		return provider;
	}
	
	protected JdbcDataProvider newProvider(String tableName) throws Exception {
		JdbcDataProvider provider = new JdbcDataProvider();
		provider.setTableName(tableName);
		return provider;
	}
	
	protected JdbcDataResolver newResolver(JdbcDataResolverItem... items) throws Exception {
		JdbcDataResolver resolver = new JdbcDataResolver();
		List<JdbcDataResolverItem> newItems = new ArrayList<JdbcDataResolverItem>();
		for (JdbcDataResolverItem item: items) {
			newItems.add(item);
		}
		resolver.setResolverItems(newItems);
		return resolver;
	}
	
	protected JdbcDataResolver getResolver(String name) throws Exception {
		JdbcDataResolver resolver = (JdbcDataResolver)this.getDataResolverManager().getDataResolver(name);
		Assert.assertNotNull("no resolver named [" + name + "] defined.", resolver);
		
		return resolver;
	}
	
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add(this.getDefaultContextFilePath());
		return locations;
	}
	
	private String getDefaultContextFilePath() {
		String className = this.getClass().getName();
		String [] tokens = StringUtils.split(className, '.');
		tokens[tokens.length-1] = "context.xml";
		
		return StringUtils.join(tokens, '/');
	}
}
