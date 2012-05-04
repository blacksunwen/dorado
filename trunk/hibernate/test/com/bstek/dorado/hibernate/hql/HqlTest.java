package com.bstek.dorado.hibernate.hql;

import java.util.List;

import junit.framework.Assert;

import com.bstek.dorado.data.util.DataUtils;
import com.bstek.dorado.hibernate.HibernateContextTestCase;

public class HqlTest extends HibernateContextTestCase {

	public void test_novar() throws Exception {
		String clause = "from Product";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product", hql.getClause());
		Assert.assertEquals(0, vars.size());
	}
	
	public void test_varname() throws Exception {
		String clause = "from Product where price > :price";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product where price > ?", hql.getClause());
		Assert.assertEquals(1, vars.size());
		{
			HqlVarExpr var = vars.get(0);
			Assert.assertEquals("price", var.getExpr());
			Assert.assertEquals(0, var.getIndex());
			Assert.assertEquals("price", var.getVarName());
		}
	}
	
	public void test_dataType() throws Exception {
		String clause = "from Product where price > :(Float)price";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product where price > ?", hql.getClause());
		Assert.assertEquals(1, vars.size());
		{
			HqlVarExpr var = vars.get(0);
			HqlVarExpr var2 = new HqlVarExpr();
			var2.index = 0;
			var2.expr = "(Float)price";
			var2.dataType = DataUtils.getDataType("Float");
			var2.percentEnd = false;
			var2.percentStart = false;
			var2.varName = "price";
			
			Assert.assertEquals(var2, var);
		}
	}
	
	public void test_percent1() throws Exception {
		String clause = "from Product where name like :%name%";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product where name like ?", hql.getClause());
		Assert.assertEquals(1, vars.size());
		HqlVarExpr var = vars.get(0);
		HqlVarExpr var2 = new HqlVarExpr();
		var2.index = 0;
		var2.expr = "%name%";
		var2.dataType = null;
		var2.percentEnd = true;
		var2.percentStart = true;
		var2.varName = "name";
		
		Assert.assertEquals(var2, var);
	}
	
	public void test_percent2() throws Exception {
		String clause = "from Product where name like :%name";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product where name like ?", hql.getClause());
		Assert.assertEquals(1, vars.size());
		HqlVarExpr var = vars.get(0);
		HqlVarExpr var2 = new HqlVarExpr();
		var2.index = 0;
		var2.expr = "%name";
		var2.dataType = null;
		var2.percentEnd = false;
		var2.percentStart = true;
		var2.varName = "name";
		
		Assert.assertEquals(var2, var);
	}
	
	public void test_percent3() throws Exception {
		String clause = "from Product where name like :name%";
		Hql hql = HqlUtil.build(clause, null);
		List<HqlVarExpr> vars = hql.getVarExprs();
		
		Assert.assertEquals("from Product where name like ?", hql.getClause());
		Assert.assertEquals(1, vars.size());
		HqlVarExpr var = vars.get(0);
		HqlVarExpr var2 = new HqlVarExpr();
		var2.index = 0;
		var2.expr = "name%";
		var2.dataType = null;
		var2.percentEnd = true;
		var2.percentStart = false;
		var2.varName = "name";
		
		Assert.assertEquals(var2, var);
	}
}
