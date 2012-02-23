package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.meta.JdbcEnviromentMetaDataGenerator;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcEnviromentMetaDataGenerator implements
		JdbcEnviromentMetaDataGenerator {

	@Override
	public String[] listCatalogs(JdbcEnviroment jdbcEnv) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (String[])org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					ResultSet rs = dbmd.getCatalogs();
					return Utils.toArray(rs,JdbcConstants.TABLE_CAT);
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> listSchemas(JdbcEnviroment jdbcEnv,
			String catalog) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (List<Map<String, String>>)
				org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					ResultSet rs = dbmd.getSchemas();
					return Utils.toListMap(rs);
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] listTableTypes(JdbcEnviroment jdbcEnv) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			return (String[])org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){

				@Override
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					ResultSet rs = dbmd.getTableTypes();
					return Utils.toArray(rs,JdbcConstants.TABLE_TYPE);
				}
				
			});
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private String[] defaultTableTypes = new String[]{"TABLE", "VIEW"};
	
	public String[] defaultTableTypes(JdbcEnviroment jdbcEnv) {
		return getDefaultTableTypes();
	}
	
	public String[] getDefaultTableTypes() {
		return defaultTableTypes;
	}

	public void setDefaultTableTypes(String[] defaultTableTypes) {
		this.defaultTableTypes = defaultTableTypes;
	}
	
	@Override
	public String[] listJdbcTypes(JdbcEnviroment jdbcEnv) {
		List<JdbcType> typeList = jdbcEnv.getDialect().getJdbcTypes();
		String[] nameArray = new String[typeList.size()];
		for (int i=0; i<typeList.size(); i++) {
			JdbcType jdbcType = typeList.get(i);
			nameArray[i] = jdbcType.getName();
		}
		return nameArray;
	}
	
}
