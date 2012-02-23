package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.jdbc.config.AbstractDbTableDefinition;

/**
 * JDBC模型与dorado模型转换器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface ModelStrategy {

	/**
	 * 创建DataType定义
	 * @param tableDef
	 * @return
	 */
	DataTypeDefinition createDataTypeDefinition(AbstractDbTableDefinition tableDef);
	
	/**
	 * 创建DataProvider定义
	 * @param tableDef
	 * @return
	 */
	DataProviderDefinition createDataProviderDifinition(AbstractDbTableDefinition tableDef);
}
