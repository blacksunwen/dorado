package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;

/**
 * JDBC模型与dorado模型转换器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface ModelStrategy {

	void createDataType(AbstractDbTableDefinition tableDef) throws Exception;
	
	void createDataProvider(AbstractDbTableDefinition tableDef) throws Exception;
}
