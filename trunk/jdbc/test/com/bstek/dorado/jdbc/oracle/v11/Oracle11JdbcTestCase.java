package com.bstek.dorado.jdbc.oracle.v11;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.AbstractJdbcTestCase;

public class Oracle11JdbcTestCase extends AbstractJdbcTestCase {

	public static final String ENV_NAME = "ora11";
	
	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/idesupport/context.xml");
		locations.add("com/bstek/dorado/view/context.xml");
		locations.add("com/bstek/dorado/jdbc/oracle/v11/context.xml");
		return locations;
	}

}
