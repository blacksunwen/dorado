package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.meta.DataTypeMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.JdbcEnviromentMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

/**
 * 数据库模型的输出工具
 * 
 * @author mark.li@bstek.com
 *
 */
public interface ModelGeneratorSuit {

	JdbcEnviromentMetaDataGenerator getJdbcEnviromentMetaDataGenerator();
	
	TableMetaDataGenerator getTableMetaDataGenerator();
	
	SqlTableMetaDataGenerator getSqlTableMetaDataGenerator();
	
	DataTypeMetaDataGenerator getDataTypeMetaDataGenerator();
}
