package com.bstek.dorado.jdbc.oracle.v11;

import junit.framework.Assert;

import com.bstek.dorado.jdbc.ide.resolver.ListSpaceResolver;
import com.bstek.dorado.jdbc.ide.resolver.ListTableResolver;

public class ResolverTest extends AbstractOracle11JdbcTestCase {

	public void testListSpace() {
		ListSpaceResolver resolver = new ListSpaceResolver();
		String xml = resolver.toContent("ora11");
		
		System.out.println(xml);
		Assert.assertTrue(xml.indexOf("DORADO") >=0);
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
