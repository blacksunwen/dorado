package com.bstek.dorado.jdbc.parser;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbModel;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition;
import com.bstek.dorado.jdbc.model.table.TableDefinition;

public class JdbcLoaderTest extends AbstractJdbcTestCase {

	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/parser/context.xml");
		return locations;
	}

	
	public void test() {
		DbmManager dbmManager = JdbcUtils.getDbmManager();
		
		DbModel[] dbms = dbmManager.listAllDbms().toArray(new DbModel[0]);
		assertEquals(2, dbms.length);
		{
			DbModel dbm = dbms[0];
			DbElementDefinition[] defs = dbm.listAll().toArray(new DbElementDefinition[0]);
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
}
