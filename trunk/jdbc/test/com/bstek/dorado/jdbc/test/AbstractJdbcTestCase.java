package com.bstek.dorado.jdbc.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

public abstract class AbstractJdbcTestCase extends TestCase {

	private final String[] dependencyConfigLocation = new String[]{
			"com/bstek/dorado/core/context.xml",
			"com/bstek/dorado/core/test-context.xml",
			"com/bstek/dorado/config/context.xml",
			"com/bstek/dorado/common/context.xml",
			"com/bstek/dorado/data/context.xml",
			"com/bstek/dorado/idesupport/context.xml",
			"com/bstek/dorado/view/context.xml",
			"com/bstek/dorado/view/test-context.xml",
			"com/bstek/dorado/web/context.xml",
			"com/bstek/dorado/jdbc/context.xml",
			"com/bstek/dorado/jdbc/test/context.xml"
		};
	
	private List<TestTable> testTables = new ArrayList<TestTable>();
	private List<TestTrigger> testTriggers = new ArrayList<TestTrigger>();
	private List<TestSequence> testSequences = new ArrayList<TestSequence>();
	
	private JdbcTestContext currentContext = null;
	
	@Override
	protected void setUp() throws Exception {
		System.out.println(">>--------------------------------------------------->");
		super.setUp();
		TestJdbcUtils.setCurrentTestClass(this.getClass());

		String locationStr = StringUtils.join(dependencyConfigLocation, ';');
		String exLocation = getExConfigLocation();
		if (exLocation != null) {
			locationStr += ";" + exLocation;
		}
		Configure.getStore().set("core.contextConfigLocation", locationStr);
		currentContext = new JdbcTestContext();
		
		for (TestSequence seq: testSequences) {
			seq.create();
		}
		for (TestTable t: testTables) {
			t.create();
		}
		for (TestTrigger t: testTriggers) {
			t.create();
		}
		TestJdbcUtils.setCurrentTestClass(null);
	}

	@Override
	protected void tearDown() throws Exception {
		for (TestTrigger t: testTriggers) {
			t.drop();
		}
		for (TestTable t: testTables) {
			t.drop();
		}
		for (TestSequence seq: testSequences) {
			seq.drop();
		}

		currentContext.close();
		super.tearDown();
		
		System.out.println("<---------------------------------------------------<<");
		System.gc();
		Thread.sleep(2*1000);
	}
	
	protected void register(TestTable...tables) {
		for(TestTable t: tables) {
			testTables.add(t);
		}
	}
	
	protected void register(TestTrigger... triggers) {
		for (TestTrigger t: triggers) {
			testTriggers.add(t);
		}
	}
	
	protected void register(TestSequence... seqs) {
		for (TestSequence seq: seqs) {
			testSequences.add(seq);
		}
	}
	
	protected TestTable getTestTable(String name) {
		for (TestTable t: testTables) {
			if (name.equals(t.getName())) {
				return t;
			}
		}
		throw new IllegalArgumentException("no test table named [" + name + "]");
	}
	
	protected JdbcEnviroment getJdbcEnviroment() throws Exception {
		JdbcEnviromentManager manager = (JdbcEnviromentManager)Context.getCurrent().getServiceBean("jdbc.enviromentManager");
		return manager.getDefault();
	}
	
	protected JdbcDao getDao() {
		try {
			return (JdbcDao)Context.getCurrent().getServiceBean("jdbcDao");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() throws Exception {
		return this.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
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
	
	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	protected DataResolverManager getDataResolverManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataResolverManager dataResolverManager = (DataResolverManager) conetxt
				.getServiceBean("dataResolverManager");
		return dataResolverManager;
	}
	
	String getExConfigLocation() {
		String className = this.getClass().getName();
		String [] tokens = StringUtils.split(className, '.');
		tokens[tokens.length-1] = "context.xml";
		String location = StringUtils.join(tokens, '/');
		
		InputStream in = this.getClass().getResourceAsStream(location);
		if (in != null) {
			try {
				return location;
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} else {
			return null;
		}
	}

}
