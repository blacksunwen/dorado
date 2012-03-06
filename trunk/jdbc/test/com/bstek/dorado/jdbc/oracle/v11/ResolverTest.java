package com.bstek.dorado.jdbc.oracle.v11;

import junit.framework.Assert;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.ide.resolver.ListJdbcTypeResolver;
import com.bstek.dorado.jdbc.ide.resolver.ListSpaceResolver;
import com.bstek.dorado.jdbc.ide.resolver.ListTableResolver;

public class ResolverTest extends AbstractOracle11JdbcTestCase {

	public void testListSpace() {
		ListSpaceResolver resolver = new ListSpaceResolver();
		String xml = resolver.toContent("ora11");
		
		System.out.println(xml);
		Assert.assertTrue(xml.indexOf("DORADO") >=0);
	}
	
	public void testListJdbcType() throws Exception {
		Context context = Context.getCurrent();
		JdbcTypeManager jdbcTypeManager = (JdbcTypeManager)context.getServiceBean("jdbc.jdbcTypeManager");
		ListJdbcTypeResolver resolver = new ListJdbcTypeResolver();
		resolver.setJdbcTypeManager(jdbcTypeManager);
		String str = resolver.toContent();
		
		System.out.println(str);
	}
	
	public void testListTable1() {
		ListTableResolver resolver = new ListTableResolver();
		
		String envName = "ora11";
		String namespace = "DORADO";
		String[] tableTypes = null;
		String tableNamePattern = null;
		
		String xml = resolver.toContent(envName, namespace, tableTypes, tableNamePattern);
		System.out.println("Tables:" + xml);
		
		Assert.assertTrue(xml.indexOf("EMPLOYEE") >=0);
	}
	
	public void testListTable2() {
		String envName = "ora11";
		String namespace = "DORADO";
		String[] tableTypes = null;
		String tableNamePattern = "emp";
		
		ListTableResolver resolver = new ListTableResolver();
		
		String xml = resolver.toContent(envName, namespace, tableTypes, tableNamePattern);
		System.out.println("Tables:" + xml);
		
		Assert.assertTrue(xml.indexOf("EMPLOYEE") >=0);
	}
	
}
