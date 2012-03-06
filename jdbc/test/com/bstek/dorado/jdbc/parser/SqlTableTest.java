package com.bstek.dorado.jdbc.parser;

import java.util.Collection;
import java.util.List;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.sqltable.SqlTable;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableColumn;

public class SqlTableTest extends AbstractParserTestCase {

	public void test() throws Exception {
		String MOCK_XML = "com/bstek/dorado/jdbc/parser/sql-table1.xml";
		
		ObjectDefinition definition = parse(MOCK_XML, SqlTable.class);
		{
			assertNotNull(definition);
			assertEquals("sqlTable1", definition.getProperty("name"));
			assertNotNull(definition.getProperty("jdbcEnviroment"));
			
			Collection<Operation> columns = definition.getInitOperations();
			assertEquals(3, columns.size());
		}
		
		JdbcCreationContext creationContext = new JdbcCreationContext();
		SqlTable table = (SqlTable)definition.create(creationContext);
		{
			assertNotNull(table);
			assertEquals("sqlTable1", table.getName());
			assertEquals("ora11", table.getJdbcEnviroment().getName());
			assertEquals("select * from DEPT", table.getQuerySql());
			
			List<AbstractDbColumn> columns = table.getAllColumns();
			assertEquals(3, columns.size());
			
			SqlTableColumn c1 = (SqlTableColumn)columns.get(0);
			assertEquals("DEPT_NAME", c1.getName());
			
			SqlTableColumn c2 = (SqlTableColumn)columns.get(1);
			assertEquals("DEPT_ID", c2.getName());
			
			SqlTableColumn c3 = (SqlTableColumn)columns.get(2);
			assertEquals("PARENT_ID", c3.getName());
		}
	}
}
