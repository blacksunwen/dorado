package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.SqlGenerator;

/**
 * JDBC模块的配置管理器
 * 
 * @author mark
 * 
 */
public interface JdbcConfigManager {

	/**
	 * 初始化，根据默认配置加载dbmodel文件
	 * 
	 * @throws Exception
	 */
	void initialize() throws Exception;

	/**
	 * 获取{@link com.bstek.dorado.config.definition.DefinitionManager}
	 * 
	 * @return
	 */
	JdbcDefinitionManager getDefinitionManager();

	/**
	 * 获取{@link com.bstek.dorado.jdbc.model.DbElement}
	 * 
	 * @param name
	 * @return
	 */
	DbElement getDbElement(String name);

	/**
	 * 获取解析器
	 * 
	 * @param type
	 * @return
	 */
	ObjectParser getParser(String type);

	/**
	 * 获取{@link com.bstek.dorado.jdbc.sql.SqlGenerator}
	 * 
	 * @param type
	 * @return
	 */
	SqlGenerator getSqlGenerator(String type);

}
