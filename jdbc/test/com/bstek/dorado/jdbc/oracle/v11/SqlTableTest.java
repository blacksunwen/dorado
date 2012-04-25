package com.bstek.dorado.jdbc.oracle.v11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.TestJdbcUtils;
import com.bstek.dorado.jdbc.support.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.support.JdbcDataResolverOperation;

public class SqlTableTest  extends AbstractOracle11JdbcTestCase {

	public void testSqlDept() throws Exception{
		String tableName = "sql_dept";
		Collection<Record> depts = getDao().query(tableName, null);
		
		Assert.assertTrue(depts.size() > 0);
		{
			Record dept = depts.iterator().next();
			String id = dept.getString("DEPT_ID");
			
			Record dept2 = Dept.random();
			dept2.put("DEPT_ID", id);
			
			getDao().update(tableName, dept2);
			
			Record dept3 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept2, dept3);
		}
	}
	
	public void testSqlDeptResolver() throws Exception {
		String tableName = "sql_dept";
		Collection<Record> depts = getDao().query(tableName, null);
		
		Assert.assertTrue(depts.size() > 0);
		{
			Record dept = depts.iterator().next();
			String id = dept.getString("DEPT_ID");
			
			Record dept2 = Dept.random();
			dept2.put("DEPT_ID", id);
			dept2 = EntityUtils.toEntity(dept2);
			EntityUtils.setState(dept2, EntityState.MODIFIED);
			
			JdbcEnviroment enviroment = null;
			Object parameter = null;
			DataItems dataItems = new DataItems();
			{
				dataItems.put("dept", dept2);
			}
			List<JdbcDataResolverItem> resolverItems = new ArrayList<JdbcDataResolverItem>();
			{
				JdbcDataResolverItem resolverItem = new JdbcDataResolverItem();
				resolverItem.setName("dept");
				resolverItem.setTableName(tableName);
				resolverItems.add(resolverItem);
			}
			JdbcDataResolverContext resolverContext = new JdbcDataResolverContext(enviroment, parameter, dataItems, resolverItems);
			JdbcDataResolverOperation resolverOperation = new JdbcDataResolverOperation(resolverContext);
			resolverOperation.execute();
			//...
			
			Record dept3 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept2, dept3);
		}
	}
	
}
