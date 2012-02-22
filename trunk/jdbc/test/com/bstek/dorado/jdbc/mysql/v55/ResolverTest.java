package com.bstek.dorado.jdbc.mysql.v55;

import junit.framework.Assert;

import com.bstek.dorado.jdbc.ide.resolver.ListSpaceResolver;
import com.bstek.dorado.jdbc.ide.resolver.ListTableResolver;

public class ResolverTest extends Mysql55JdbcTestCase{

	public void testListSpace() {
		ListSpaceResolver resolver = new ListSpaceResolver();
		String xml = resolver.toContent("mysql");
		
		System.out.println(xml);
		Assert.assertTrue(xml.indexOf("dorado") >=0);
	}
	
	public void testListTable() {
		ListTableResolver resolver = new ListTableResolver();
		
		String envName = "mysql";
		String namespace = "dorado";
		String[] tableTypes = null;
		String tableNamePattern = null;
		
		String xml = resolver.toContent(envName, namespace, tableTypes, tableNamePattern);
		System.out.println("Tables:" + xml);
		
		Assert.assertTrue(xml.indexOf("employee") >=0);
	}
	
	public void testListTable2() {
		ListTableResolver resolver = new ListTableResolver();
		
		String envName = "mysql";
		String namespace = "dorado";
		String[] tableTypes = null;
		String tableNamePattern = null;
		
		String xml = resolver.toContent(envName, namespace, tableTypes, tableNamePattern);
		System.out.println("Tables:" + xml);
		
		Assert.assertTrue(xml.indexOf("employee") >=0);
	}
}
