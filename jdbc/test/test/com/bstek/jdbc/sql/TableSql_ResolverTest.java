package test.com.bstek.jdbc.sql;

import junit.framework.Assert;

import org.junit.Test;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.model.table.TableSqlGenerator;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.UpdateSql;

public class TableSql_ResolverTest {
	@Test
	public void test_insert_0() {
		TableSqlGenerator generator = new TableSqlGenerator();

		//--
		Table table = new Table();
		table.setName("t1");
		table.setTableName("table1");
		
		{
			TableKeyColumn c = new TableKeyColumn();
			c.setColumnName("ID");
			c.setPropertyName("id");
			table.addColumn(c);
		}{
			TableColumn c = new TableColumn();
			c.setColumnName("NAME");
			c.setPropertyName("name");
			table.addColumn(c);
		}

		//-
		Record record = new Record();
		record.setString("name", "agg");
		
		//-
		{
			InsertSql insertSql = generator.insertSql(table, record, null);
			String sql = insertSql.toSQL(null);
			
			System.out.println(sql);
			Assert.assertEquals("INSERT INTO table1 (ID,NAME) VALUES (:id,:name)", sql);
		}{
			UpdateSql insertSql = generator.updateSql(table, record, null);
			String sql = insertSql.toSQL(null);
			
			System.out.println(sql);
			Assert.assertEquals("UPDATE table1 SET NAME=:name WHERE ID=:id", sql);
		}{
			DeleteSql deleteSql = generator.deleteSql(table, record, null);
			String sql = deleteSql.toSQL(null);
			
			System.out.println(sql);
			Assert.assertEquals("DELETE FROM table1 WHERE ID=:id", sql);
		}
		
	}
}
