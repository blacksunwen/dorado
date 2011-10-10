package test.com.bstek.jdbc.sql;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.autotable.JunctionMatchRule;
import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.AutoTableSqlGenerator;
import com.bstek.dorado.jdbc.model.autotable.BaseMatchRule;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.JoinTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.autotable.Where;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;
import com.bstek.dorado.jdbc.support.h2.H2Dialect;

public class AutoTable_SelectTest {

	@Test
	public void test0() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		{
			Table tableEmp = employee();
			FromTable fromEmp = new FromTable();
			fromEmp.setTable(tableEmp);
			fromEmp.setTableAlias("emp");
			t.addFromTable(fromEmp);
		}
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		
		//-
		JdbcQueryContext.newInstance(null);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, null);
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2 FROM EMPLOYEE AS emp", sql);
		} finally {
			JdbcQueryContext.clear();
		}
		
	}
	
	@Test
	public void test1() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		Table tableEmp = employee();
		FromTable fromEmp = new FromTable();
		fromEmp.setTable(tableEmp);
		fromEmp.setTableAlias("emp");
		t.addFromTable(fromEmp);
		
		Table tableDept = dept();
		FromTable fromDept = new FromTable();
		fromDept.setTable(tableDept);
		fromDept.setTableAlias("dept");
		t.addFromTable(fromDept);
		
		//.joinTables
		JoinTable joinTable = new JoinTable();
		joinTable.setLeftFromTableAlias("emp");
		joinTable.setLeftColumnNames(new String[]{"DEPT_ID"});
		joinTable.setRightFromTableAlias("dept");
		joinTable.setRightColumnNames(new String[]{"DEPT_ID"});
		joinTable.setJoinModel(JoinModel.LEFT_JOIN);
		t.addJoinTable(joinTable);
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		{
			AutoTableColumn c3 = new AutoTableColumn();
			c3.setAutoTable(t);
			c3.setColumnAlias("c3");
			c3.setTableAlias("dept");
			c3.setColumnName("DEPT_ID");
			t.addColumn(c3);
		}
		{
			AutoTableColumn c4 = new AutoTableColumn();
			c4.setAutoTable(t);
			c4.setColumnAlias("c4");
			c4.setTableAlias("dept");
			c4.setColumnName("DEPT_NAME");
			t.addColumn(c4);
		}
		
		//-
		JdbcQueryContext.newInstance(null);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, null);
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2,dept.DEPT_ID AS c3,dept.DEPT_NAME AS c4 FROM EMPLOYEE AS emp LEFT JOIN DEPT AS dept ON emp.DEPT_ID = dept.DEPT_ID", sql);
		} finally {
			JdbcQueryContext.clear();
		}
		
	}
	
	@Test
	public void test2() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		Table tableEmp = employee();
		FromTable fromEmp = new FromTable();
		fromEmp.setTableAlias("emp");
		fromEmp.setTable(tableEmp);
		t.addFromTable(fromEmp);
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		
		//3.where
		Where where = new Where();
		{
			BaseMatchRule mr1 = new BaseMatchRule();
			mr1.setAutoTable(t);
			mr1.setTableAlias("emp");
			mr1.setColumnName("EMP_NAME");
			mr1.setOperator("=");
			mr1.setValue("ename");
			where.addMatchRule(mr1);
			t.setWhere(where);
		}
		//-
		JdbcQueryContext.newInstance(null);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, ctx.getParameter());
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2 FROM EMPLOYEE AS emp", sql);
		} finally {
			JdbcQueryContext.clear();
		}
	}
	
	@Test
	public void test21() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		Table tableEmp = employee();
		FromTable fromEmp = new FromTable();
		fromEmp.setTableAlias("emp");
		fromEmp.setTable(tableEmp);
		t.addFromTable(fromEmp);
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		
		//3.where
		Where where = new Where();
		{
			BaseMatchRule mr1 = new BaseMatchRule();
			mr1.setAutoTable(t);
			mr1.setTableAlias("emp");
			mr1.setColumnName("EMP_NAME");
			mr1.setOperator("=");
			mr1.setValue("ename");
			where.addMatchRule(mr1);
			t.setWhere(where);
		}
		
		//-
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ename", "LIWI");
		JdbcQueryContext.newInstance(param);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, ctx.getParameter());
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2 FROM EMPLOYEE AS emp WHERE emp.EMP_NAME = :ename", sql);
		} finally {
			JdbcQueryContext.clear();
		}
	}
	
	@Test
	public void test22() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		Table tableEmp = employee();
		{
			FromTable fromEmp = new FromTable();
			fromEmp.setTableAlias("emp");
			fromEmp.setTable(tableEmp);
			t.addFromTable(fromEmp);
		}
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		
		//3.where
		Where where = new Where();
		{
			BaseMatchRule mr1 = new BaseMatchRule();
			mr1.setAutoTable(t);
			mr1.setTableAlias("emp");
			mr1.setColumnName("EMP_NAME");
			mr1.setOperator("=");
			mr1.setValue("ename");
			where.addMatchRule(mr1);
		}
		{
			BaseMatchRule mr2 = new BaseMatchRule();
			mr2.setAutoTable(t);
			mr2.setTableAlias("emp");
			mr2.setColumnName("DEPT_ID");
			mr2.setOperator("like");
			mr2.setValue("edept");
			where.addMatchRule(mr2);
			t.setWhere(where);
		}
		
		JunctionMatchRule amr = new JunctionMatchRule();
		{
			BaseMatchRule mr11 = new BaseMatchRule();
			mr11.setAutoTable(t);
			mr11.setTableAlias("emp");
			mr11.setColumnName("EMP_ID");
			mr11.setOperator(">=");
			mr11.setValue("eid1");
			amr.addMatchRule(mr11);
		}
		{
			BaseMatchRule mr12 = new BaseMatchRule();
			mr12.setAutoTable(t);
			mr12.setTableAlias("emp");
			mr12.setColumnName("EMP_ID");
			mr12.setOperator("<=");
			mr12.setValue("eid2");
			amr.addMatchRule(mr12);
			where.addMatchRule(amr);
			where.setModel(JunctionModel.OR);
		}
		
		//-
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ename", "LIWI");
		param.put("edept", "DEPT001");
		param.put("eid1", "10");
		param.put("eid2", "20");
		JdbcQueryContext.newInstance(param);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, ctx.getParameter());
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2 FROM EMPLOYEE AS emp WHERE emp.EMP_NAME = :ename OR emp.DEPT_ID like :edept OR (emp.EMP_ID >= :eid1 AND emp.EMP_ID <= :eid2)", sql);
		} finally {
			JdbcQueryContext.clear();
		}
	}
	
	@Test
	public void test3() {
		AutoTableSqlGenerator generator = new AutoTableSqlGenerator();
		
		AutoTable t = new AutoTable();
		t.setName("at");
		
		//1.fromTables
		Table tableEmp = employee();
		{
			FromTable fromEmp = new FromTable();
			fromEmp.setTable(tableEmp);
			fromEmp.setTableAlias("emp");
			t.addFromTable(fromEmp);
		}
		
		
		//2.columns
		{
			AutoTableColumn c1 = new AutoTableColumn();
			c1.setAutoTable(t);
			c1.setColumnAlias("c1");
			c1.setTableAlias("emp");
			c1.setColumnName("EMP_ID");
			t.addColumn(c1);
		}
		{
			AutoTableColumn c2 = new AutoTableColumn();
			c2.setAutoTable(t);
			c2.setColumnAlias("c2");
			c2.setTableAlias("emp");
			c2.setColumnName("EMP_NAME");
			t.addColumn(c2);
		}
		{
			AutoTableColumn c3 = new AutoTableColumn();
			c3.setAutoTable(t);
			c3.setColumnAlias("c3");
			c3.setTableAlias("emp");
			c3.setColumnName("DEPT_ID");
			t.addColumn(c3);
		}
		
		//3.order
		{
			Order rd1 = new Order();
			rd1.setAutoTable(t);
			rd1.setColumnName("c1");
			t.addOrder(rd1);
		}
		{
			Order rd2 = new Order();
			rd2.setAutoTable(t);
			rd2.setColumnName("c2");
			rd2.setOrderModel(OrderModel.DESC);
			t.addOrder(rd2);
		}
		{
			Order rd3 = new Order();
			rd3.setAutoTable(t);
			rd3.setColumnName("c3");
			rd3.setOrderModel(OrderModel.ASC);
			rd3.setNullsModel(NullsModel.NULLS_FIRST);
			t.addOrder(rd3);
		}
		
		//-
		JdbcQueryContext.newInstance(null);
		try {
			JdbcQueryContext ctx = JdbcQueryContext.getInstance();
			ctx.setJdbcEnviroment(new JdbcEnviroment());
			ctx.getJdbcEnviroment().setDialect(new H2Dialect());
			
			SelectSql selectSql = generator.selectSql(t, ctx.getParameter());
			Dialect dialect = ctx.getJdbcEnviroment().getDialect();
			String sql = dialect.toSQL(selectSql);
			
			System.out.println(sql);
			
			Assert.assertEquals("SELECT emp.EMP_ID AS c1,emp.EMP_NAME AS c2,emp.DEPT_ID AS c3 FROM EMPLOYEE AS emp ORDER BY c1,c2 DESC,c3 ASC NULLS FIRST", sql);
		} finally {
			JdbcQueryContext.clear();
		}
		
	}
	
	Table employee() {
		Table table = new Table();
		table.setName("t1");
		table.setTableName("EMPLOYEE");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("EMP_ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("EMP_NAME");
		table.addColumn(name);
		
		TableColumn dept = new TableColumn();
		dept.setColumnName("DEPT_ID");
		table.addColumn(dept);
		
		return table;
	}
	
	Table dept() {
		Table table = new Table();
		table.setName("t2");
		table.setTableName("DEPT");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("DEPT_ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("DEPT_NAME");
		table.addColumn(name);
		
		return table;
	}
}
