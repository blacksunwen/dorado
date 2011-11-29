package com.bstek.dorado.jdbc;

import java.util.List;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;

public abstract class AbstractJdbcTestCase extends ConfigManagerTestSupport {

	protected AbstractJdbcTestCase () {
		super();
		for (String location: getExtConfigLocations()) {
			this.addExtensionContextConfigLocation(location);
		}
	}
	
	protected abstract List<String> getExtConfigLocations(); 
}
