package com.bstek.dorado.jdbc.parser;

import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbModel;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition;
import com.bstek.dorado.jdbc.model.table.TableDefinition;

public class DmbParserTest extends AbstractParserTestCase {

	public void test() throws Exception {
		String MOCK_XML = "com/bstek/dorado/jdbc/parser/dbm1.dbm.xml";
		
		DbModel dbModel = parse(MOCK_XML);
		assertNotNull(dbModel);
		
		DbElementDefinition[] defs = dbModel.listAll().toArray(new DbElementDefinition[0]);
		assertEquals(3, defs.length);
		{
			DbElementDefinition def = defs[0];
			assertTrue(def instanceof TableDefinition);
		}
		{
			DbElementDefinition def = defs[1];
			assertTrue(def instanceof SqlTableDefinition);
		}
		{
			DbElementDefinition def = defs[2];
			assertTrue(def instanceof TableDefinition);
		}
	}
}
