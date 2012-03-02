package com.bstek.dorado.jdbc.mssql.v2000;

import junit.framework.Assert;

import com.bstek.dorado.jdbc.ide.resolver.ListSpaceResolver;

public class ResolverTest extends Mssql2000JdbcTestCase{

	public void testListSpaces() throws Exception {
		ListSpaceResolver r = new ListSpaceResolver();
		String spaces = r.toContent("mssql2000");
		
		System.out.println("Spaces: " + spaces);
		Assert.assertTrue(spaces.indexOf("dbo") >= 0);
	}
}
