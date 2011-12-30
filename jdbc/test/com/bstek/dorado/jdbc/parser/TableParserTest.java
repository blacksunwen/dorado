package com.bstek.dorado.jdbc.parser;

import java.util.List;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElementCreationContext;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

public class TableParserTest extends AbstractParserTestCase{

	public void test() throws Exception {
		String MOCK_XML = "com/bstek/dorado/jdbc/parser/table1.xml";
		
		ObjectDefinition definition = parse(MOCK_XML, Table.class);
		{
			assertNotNull(definition);
			assertEquals("dept1", definition.getProperty("name"));
			assertEquals("DEPT", definition.getProperty("tableName"));
			assertNotNull(definition.getProperty("jdbcEnviroment"));
			
			List<Operation> columns = definition.getInitOperations();
			assertEquals(3, columns.size());
		}
		
		DbElementCreationContext creationContext = new DbElementCreationContext();
		Table table = (Table)definition.create(creationContext);
		{
			assertNotNull(table);
			assertEquals("dept1", table.getName());
			assertEquals("DEPT", table.getTableName());
			assertNull(table.getCatalog());
			assertNull(table.getSchema());
			assertEquals("ora11", table.getJdbcEnviroment().getName());
			assertEquals("#if ($ID) WHERE DEPT_ID=:ID #end", table.getDynamicClause());
			
			List<Column> columns = table.getAllColumns();
			assertEquals(3, columns.size());
			assertTrue(columns.get(0) instanceof TableColumn);
			assertTrue(columns.get(1) instanceof TableKeyColumn);
			assertTrue(columns.get(2) instanceof TableColumn);
			
			TableColumn c1 = (TableColumn)columns.get(0);
			assertEquals("DEPT_NAME", c1.getPropertyName());
			assertEquals("DEPT_NAME", c1.getColumnName());
			
			TableKeyColumn c2 = (TableKeyColumn)columns.get(1);
			assertEquals("DEPT_ID", c2.getPropertyName());
			assertEquals("DEPT_ID", c2.getColumnName());
			
			TableColumn c3 = (TableColumn)columns.get(2);
			assertEquals("PARENT_ID", c3.getPropertyName());
			assertEquals("PARENT_ID", c3.getColumnName());
		}
	}
}
