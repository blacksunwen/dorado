package com.bstek.dorado.jdbc.parser;

import java.util.Collection;
import java.util.List;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.sqltable.SqlTable;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableColumn;

public class SqlTableTest extends AbstractParserTestCase {

	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		String MOCK_XML = "com/bstek/dorado/jdbc/parser/sql-table1.xml";
		
		ObjectDefinition definition = parse(MOCK_XML, SqlTable.class);
		{
			assertNotNull(definition);
			assertEquals("sqlTable1", definition.getProperty("name"));
			assertNotNull(definition.getProperty("jdbcEnviroment"));
			
			Collection<Operation> columns = (Collection<Operation>)definition.getProperty("allColumns");
			assertEquals(3, columns.size());
		}
		
		DbElementCreationContext creationContext = new DbElementCreationContext();
		SqlTable table = (SqlTable)definition.create(creationContext);
		{
			assertNotNull(table);
			assertEquals("sqlTable1", table.getName());
			assertEquals("ora11", table.getJdbcEnviroment().getName());
			assertEquals("select * from DEPT", table.getQuerySql());
			
			List<Column> columns = table.getAllColumns();
			assertEquals(3, columns.size());
			
			SqlTableColumn c1 = (SqlTableColumn)columns.get(0);
			assertEquals("DEPT_NAME", c1.getPropertyName());
			assertEquals("DEPT_NAME", c1.getColumnName());
			
			SqlTableColumn c2 = (SqlTableColumn)columns.get(1);
			assertEquals("DEPT_ID", c2.getPropertyName());
			assertEquals("DEPT_ID", c2.getColumnName());
			
			SqlTableColumn c3 = (SqlTableColumn)columns.get(2);
			assertEquals("PARENT_ID", c3.getPropertyName());
			assertEquals("PARENT_ID", c3.getColumnName());
		}
	}
}
