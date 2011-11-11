package com.bstek.dorado.jdbc.model.storedprogram;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.type.JdbcType;

public class SPRunner {

	public static Map<String,Object> execute(StoredProcedure sp, Object param) {
		JdbcEnviroment jdbcEnv = sp.getJdbcEnviroment();
		StoredProgramContext spContext = new StoredProgramContext(jdbcEnv, param);
		JdbcTemplate jdbcTemplate = jdbcEnv.getNamedDao().getJdbcTemplate();
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
		
		String catalog = sp.getCatalog();
		if (StringUtils.isNotEmpty(catalog)) {
			call.withCatalogName(catalog);
		}
		
		String schema = sp.getSchema();
		if (StringUtils.isNotEmpty(schema)) {
			call.withSchemaName(schema);
		}
		
		String procName = sp.getProgramName();
		call.withProcedureName(procName);
		
		//call.withoutProcedureColumnMetaDataAccess();
		
		MapSqlParameterSource spSource = new MapSqlParameterSource();
		JdbcParameterSource source = spContext.getJdbcParameterSource();
		ProgramParameter[] inParameters = sp.getInableParameters();
		for (ProgramParameter parameter: inParameters) {
			inValue(source, parameter, spSource);
			
			JdbcType jdbcType = parameter.getJdbcType();
			String name = parameter.getName();
			int sqlType = jdbcType.getSqlType();
			Integer scale = jdbcType.getScale();
			
			SqlParameter spParam = null;
			if (parameter.getType() == ProgramParameter.Type.IN) {
				if (scale == null) {
					spParam = new SqlParameter(name, sqlType, jdbcType.getTypeName());
				} else {
					spParam = new SqlParameter(name, sqlType, scale);
				}
				
			} else if (parameter.getType() == ProgramParameter.Type.INOUT) {
				SqlReturnType returnType = new JdbcReturnType(jdbcType);
				if (scale == null) {
					spParam = new SqlInOutParameter(name, sqlType, jdbcType.getTypeName(), returnType);
				} else {
					spParam = new SqlInOutParameter(name, sqlType, scale);
				}
			}
			call.addDeclaredParameter(spParam);
		}
		
		Map<String,Object> dbResult = call.execute(spSource);
		
		ProgramParameter[] outParameters = sp.getOutableParameters();
		for (ProgramParameter parameter: outParameters) {
			String name = parameter.getName(); 
			JdbcType jdbcType = parameter.getJdbcType();
			
			Object dbValue = dbResult.get(name);
			Object value = jdbcType.fromDB(dbValue);
			dbResult.put(name, value);
		}
		
		return dbResult;
	}
	
	private static void inValue(JdbcParameterSource source, ProgramParameter p, MapSqlParameterSource spSource) {
		JdbcType jdbcType = p.getJdbcType();
		Object pValue = p.getValue();
		Object value = pValue;
		
		if (pValue != null) {
			if (pValue instanceof String) {
				String pvStr = (String)pValue;
				if (pvStr.length() == 0) {
					value = null;
				} else if (pvStr.length() > 1 && pvStr.charAt(0) == ':') {
					String pName = pvStr.substring(1);
					if (source.hasValue(pName)) {
						value = source.getValue(pName);
					} else {
						value = null;
					}
				} 
			}
		}
		
		value = jdbcType.toDB(value);
		spSource.addValue(p.getName(), value, jdbcType.getSqlType(), jdbcType.getTypeName());
	}
	
	public static Map<String,Object> execute(StoredFunction sf, StoredProgramContext spContext) {
		return null;
	}
}
