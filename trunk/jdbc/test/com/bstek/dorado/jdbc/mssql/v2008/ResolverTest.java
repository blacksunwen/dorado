package com.bstek.dorado.jdbc.mssql.v2008;

import junit.framework.Assert;

import com.bstek.dorado.jdbc.ide.resolver.ListSpaceResolver;

public class ResolverTest extends Mssql2008JdbcTestCase{

	public void testListSpaces() throws Exception {
		ListSpaceResolver r = new ListSpaceResolver();
		String spaces = r.toContent("mssql2008");
		
		System.out.println("Spaces: " + spaces);
		Assert.assertTrue(spaces.indexOf("dbo") >= 0);
	}
}
