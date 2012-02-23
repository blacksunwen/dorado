package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.meta.DataTypeMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.JdbcEnviromentMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.SqlTableMetaDataGenerator;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

public class DefaultModelGeneratorSuit implements ModelGeneratorSuit {

	private JdbcEnviromentMetaDataGenerator jdbcEnviromentMetaDataGenerator = new DefaultJdbcEnviromentMetaDataGenerator();
	private TableMetaDataGenerator tableMetaDataGenerator = new DefaultTableMetaDataGenerator();
	private SqlTableMetaDataGenerator sqlTableMetaDataGenerator = new DefaultSqlTableMetaDataGenerator();
	private DataTypeMetaDataGenerator dataTypeMetaGenerator = new DefaultDataTypeMetaDataGenerator();
	
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
	public DataTypeMetaDataGenerator getDataTypeMetaDataGenerator() {
		return dataTypeMetaGenerator;
	}

	public void setDataTypeMetaGenerator(DataTypeMetaDataGenerator dataTypeMetaGenerator) {
		this.dataTypeMetaGenerator = dataTypeMetaGenerator;
	}

}
