package com.bstek.dorado.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class JdbcParameterSource implements SqlParameterSource {

	protected SqlParameterSource source;
	protected Map<String, SqlParameterSource> subSources;
	protected MapSqlParameterSource handSource = new MapSqlParameterSource();
	protected int inc = 0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JdbcParameterSource(Object parameter) {
		if (parameter == null) {
			source = new MapSqlParameterSource();
		} else {
			if (parameter instanceof SqlParameterSource) {
				source = (SqlParameterSource)parameter;
			} else {
				if (parameter instanceof Map) {
					source = new MapSqlParameterSource((Map) parameter);
				} else {
					source = new BeanPropertySqlParameterSource(parameter);
				}
			}
		}
	}
	
	public String newValue(Object value) {
		String key = "_jdbc" + (++inc);
		this.setValue(key, value);
		return key;
	}
	
	public void setValue(String key, Object value) {
		handSource.addValue(key, value);
	}

	public int getSqlType(String paramName) {
		if (handSource.hasValue(paramName)) {
			return handSource.getSqlType(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length()-1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getSqlType(subParamName);
			} else {
				return source.getSqlType(paramName);
			}
		}
	}

	public String getTypeName(String paramName) {
		if (handSource.hasValue(paramName)) {
			return handSource.getTypeName(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length()-1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getTypeName(subParamName);
			} else {
				return source.getTypeName(paramName);
			}
		}
	}

	public Object getValue(String paramName) throws IllegalArgumentException {
		if (handSource.hasValue(paramName)) {
			return handSource.getValue(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length()-1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getValue(subParamName);
			} else {
				return source.getValue(paramName);
			}
		}
	}

	public boolean hasValue(String paramName) {
		int subIndex = paramName.lastIndexOf('.');
		if (subIndex > 0 && subIndex < paramName.length()-1) {
			String subParamName = paramName.substring(subIndex + 1);
			String subSourceName = paramName.substring(0, subIndex);
			return getSubSource(subSourceName).hasValue(subParamName);
		} else {
			return source.hasValue(paramName);
		}
	}

	protected SqlParameterSource getSubSource(String subSourceName) {
		if (subSources == null) {
			subSources = new HashMap<String, SqlParameterSource>();
		}
		
		SqlParameterSource subSource = subSources.get(subSourceName);
		if (subSource != null) {
			return subSource;
		} else {
			int subIndex2 = subSourceName.lastIndexOf('.');
			if (subIndex2 > 0 && subIndex2 < subSourceName.length()-1) {
				String subParamName2 = subSourceName.substring(subIndex2+1);
				String subSourceName2 = subSourceName.substring(0, subIndex2);
				
				SqlParameterSource subSource2 = this.getSubSource(subSourceName2);
				Object subObject2 = subSource2.getValue(subParamName2);
				subSource = new JdbcParameterSource(subObject2);
			} else {
				Object subObject = source.getValue(subSourceName);
				subSource = new JdbcParameterSource(subObject);
			}
			subSources.put(subSourceName, subSource);
		}
		
		return subSource;
	}
}
