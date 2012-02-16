package com.bstek.dorado.jdbc.parser;

import java.util.List;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;
import com.bstek.dorado.jdbc.model.AbstractColumn;
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
		
		JdbcCreationContext creationContext = new JdbcCreationContext();
		Table table = (Table)definition.create(creationContext);
		{
			assertNotNull(table);
			assertEquals("dept1", table.getName());
			assertEquals("DEPT", table.getTableName());
			assertNull(table.getCatalog());
			assertNull(table.getSchema());
			assertEquals("ora11", table.getJdbcEnviroment().getName());
			assertEquals("#if ($ID) WHERE DEPT_ID=:ID #end", table.getDynamicClause());
			
			List<AbstractColumn> columns = table.getAllColumns();
			assertEquals(3, columns.size());
			assertTrue(columns.get(0) instanceof TableKeyColumn);
			assertTrue(columns.get(1) instanceof TableColumn);
			assertTrue(columns.get(2) instanceof TableColumn);
			
			TableKeyColumn c1 = (TableKeyColumn)columns.get(0);
			assertEquals("DEPT_ID", c1.getColumnName());
			
			TableColumn c2 = (TableColumn)columns.get(1);
			assertEquals("DEPT_NAME", c2.getColumnName());
			
			TableColumn c3 = (TableColumn)columns.get(2);
			assertEquals("PARENT_ID", c3.getColumnName());
		}
	}
}
