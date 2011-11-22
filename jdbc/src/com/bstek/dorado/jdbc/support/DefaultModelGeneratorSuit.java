package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.meta.DataTypeMetaGenerator;
import com.bstek.dorado.jdbc.meta.JdbcEnviromentMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

public class DefaultModelGeneratorSuit implements ModelGeneratorSuit {

	private JdbcEnviromentMetaDataGenerator jdbcEnviromentMetaDataGenerator = new DefaultJdbcEnviromentMetaDataGenerator();
	private TableMetaDataGenerator tableMetaDataGenerator = new DefaultTableMetaDataGenerator();
	private SqlTableMetaDataGenerator sqlTableMetaDataGenerator = new DefaultSqlTableMetaDataGenerator();
	private DataTypeMetaGenerator dataTypeMetaGenerator = new DefaultDataTypeMetaGenerator();
	
	@Override
	public JdbcEnviromentMetaDataGenerator getJdbcEnviromentMetaDataGenerator() {
		return jdbcEnviromentMetaDataGenerator;
	}

	@Override
	public TableMetaDataGenerator getTableMetaDataGenerator() {
		return tableMetaDataGenerator;
	}

	@Override
	public SqlTableMetaDataGenerator getSqlTableMetaDataGenerator() {
		return sqlTableMetaDataGenerator;
	}

	public void setJdbcEnviromentMetaDataGenerator(
			JdbcEnviromentMetaDataGenerator jdbcEnviromentMetaDataGenerator) {
		this.jdbcEnviromentMetaDataGenerator = jdbcEnviromentMetaDataGenerator;
	}

	public void setTableMetaDataGenerator(
			TableMetaDataGenerator tableMetaDataGenerator) {
		this.tableMetaDataGenerator = tableMetaDataGenerator;
	}

	public void setSqlTableMetaDataGenerator(
			SqlTableMetaDataGenerator sqlTableMetaDataGenerator) {
		this.sqlTableMetaDataGenerator = sqlTableMetaDataGenerator;
	}

	@Override
	public DataTypeMetaGenerator getDataTypeMetaGenerator() {
		return dataTypeMetaGenerator;
	}

	public void setDataTypeMetaGenerator(DataTypeMetaGenerator dataTypeMetaGenerator) {
		this.dataTypeMetaGenerator = dataTypeMetaGenerator;
	}

}
