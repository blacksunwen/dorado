package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.core.io.Resource;

public interface JdbcConfigManager {

	void initialize() throws Exception;
	
	void loadConfigs(Resource[] resources) throws Exception;
	
	void unloadConfigs(Resource[] resources) throws Exception;
	
}
