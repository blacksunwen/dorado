package com.bstek.dorado.jdbc.mysql;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.AbstractJdbcTestCase;

public class MysqlJdbcTestCase extends AbstractJdbcTestCase {
	
	public static final String JDBC_ENV_NAME = "mysql";
	
	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/mysql/context.xml");
		return locations;
	}

	
}
