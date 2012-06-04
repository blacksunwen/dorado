package com.bstek.dorado.jdbc.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;
import com.bstek.dorado.junit.AbstractDoradoTestCase;

public abstract class AbstractJdbcTestCase extends AbstractDoradoTestCase {

	private List<TestTable> testTables = new ArrayList<TestTable>();
	private List<TestTrigger> testTriggers = new ArrayList<TestTrigger>();
	private List<TestSequence> testSequences = new ArrayList<TestSequence>();
	
	@Override
	protected String[] getAddonsConfigLocation() {
		return new String[]{"com/bstek/dorado/jdbc/context.xml",
				"com/bstek/dorado/jdbc/test/context.xml"};
	}

	@Override
	protected void beforeCaseRun() throws Exception {
		for (TestSequence seq: testSequences) {
			seq.create();
		}
		for (TestTable t: testTables) {
			t.create();
		}
		for (TestTrigger t: testTriggers) {
			t.create();
		}
	}

	@Override
	protected void afterCaseRun() throws Exception {
		for (TestTrigger t: testTriggers) {
			t.drop();
		}
		for (TestTable t: testTables) {
			t.drop();
		}
		for (TestSequence seq: testSequences) {
			seq.drop();
		}
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
		JdbcEnviromentManager manager = getServiceBean("jdbc.enviromentManager");
		return manager.getDefault();
	}
	
	protected JdbcDao getDao() {
		try {
			return (JdbcDao)getServiceBean("jdbcDao");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() throws Exception {
		return this.getJdbcEnviroment().getSpringNamedDao().getNamedParameterJdbcTemplate();
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
	
}
