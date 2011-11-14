package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.meta.JdbcEnviromentMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

public interface ModelGeneratorSuit {

	JdbcEnviromentMetaDataGenerator getJdbcEnviromentMetaDataGenerator();
	
	TableMetaDataGenerator getTableMetaDataGenerator();
	
	SqlTableMetaDataGenerator getSqlTableMetaDataGenerator();
}
