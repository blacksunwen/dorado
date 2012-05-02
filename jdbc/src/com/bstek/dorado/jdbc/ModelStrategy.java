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
	 * 创建DataType的定义对象
	 * @param tableDef
	 * @return
	 * @throws Exception
	 */
	DataTypeDefinition createDataType(AbstractDbTableDefinition tableDef) throws Exception;
	
	/**
	 * 创建DataProvider的定义对象
	 * @param tableDef
	 * @return
	 * @throws Exception
	 */
	DataProviderDefinition createDataProvider(AbstractDbTableDefinition tableDef) throws Exception;
}
