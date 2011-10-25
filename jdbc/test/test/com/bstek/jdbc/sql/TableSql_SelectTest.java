package test.com.bstek.jdbc.sql;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.model.table.TableSqlGenerator;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.h2.H2Dialect;

public class TableSql_SelectTest {

	@Test
	public void test_select_0() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, null, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID FROM t1", sql);
	}
	
	@Test
	public void test_select_1() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, null, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME FROM t1", sql);
	}
	
	@Test
	public void test_select_2() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		table.setCatalog("D");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, null, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME FROM D.t1", sql);
	}
	
	@Test
	public void test_select_3() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		table.setCatalog("D");
		table.setDynamicToken("WHERE 1=1");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, null, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME FROM D.t1 WHERE 1=1", sql);
	}
	
	@Test
	public void test_select_4() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		table.setCatalog("D");
		table.setDynamicToken("#if ($ID) WHERE ID=:ID #end");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, null, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME FROM D.t1", sql);
	}
	@Test
	public void test_select_4_1() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		table.setCatalog("D");
		table.setDynamicToken("#if($ID)WHERE ID=:ID#end");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		TableColumn sex = new TableColumn();
		sex.setColumnName("SEX");
		table.addColumn(sex);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", 5);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, param, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME,SEX FROM D.t1 WHERE ID=:ID", sql);
	}
	@Test
	public void test_select_5() {
		TableSqlGenerator generator = new TableSqlGenerator();
		
		Table table = new Table();
		table.setName("t1");
		table.setTableName("t1");
		table.setCatalog("D");
		table.setDynamicToken("#if($ID)WHERE ID=:ID#end");
		
		TableKeyColumn id = new TableKeyColumn();
		id.setColumnName("ID");
		table.addColumn(id);
		
		TableColumn name = new TableColumn();
		name.setColumnName("NAME");
		table.addColumn(name);
		
		TableColumn sex = new TableColumn();
		sex.setColumnName("SEX");
		table.addColumn(sex);
		
		TableColumn dept = new TableColumn();
		dept.setColumnName("DEPT");
		dept.setSelectable(false);
		table.addColumn(dept);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", 5);
		
		JdbcDataProviderContext jdbcContext = new JdbcDataProviderContext(null, param, null);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jdbcContext);
		SelectSql selectSql = generator.selectSql(operation);
		
		H2Dialect dialect = new H2Dialect();
		String sql = dialect.toSQL(selectSql);
		
		System.out.println(sql);
		Assert.assertEquals("SELECT ID,NAME,SEX FROM D.t1 WHERE ID=:ID", sql);
	}
	
}
