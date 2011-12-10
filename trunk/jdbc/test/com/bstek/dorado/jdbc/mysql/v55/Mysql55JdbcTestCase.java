package com.bstek.dorado.jdbc.mysql.v55;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.AbstractJdbcTestCase;

public class Mysql55JdbcTestCase extends AbstractJdbcTestCase {
	
	public static final String JDBC_ENV_NAME = "mysql";
	
	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/mysql/v55/context.xml");
		return locations;
	}
}
