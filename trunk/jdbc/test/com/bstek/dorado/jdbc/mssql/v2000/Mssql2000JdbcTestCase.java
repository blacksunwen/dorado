package com.bstek.dorado.jdbc.mssql.v2000;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.AbstractJdbcTestCase;

class Mssql2000JdbcTestCase extends AbstractJdbcTestCase {

	public static final String ENV_NAME = "mssql2000";
	
	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/mssql/v2000/context.xml");
		return locations;
	}
}
