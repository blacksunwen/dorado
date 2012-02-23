package com.bstek.dorado.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * 执行数据库操作时候的参数对象。<br>
 * <b>注意：</b>通过{@link #addValue(Object)}或{@link #setValue(String, Object)} 添加或设置的参数并不操作“原始对象”
 * 
 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource
 * @author mark.li@bstek.com
 * 
 */
public class JdbcParameterSource implements SqlParameterSource {

	protected SqlParameterSource source;
	protected Map<String, SqlParameterSource> subSources;
	protected MapSqlParameterSource handSource = new MapSqlParameterSource();
	protected int inc = 0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * 根据“原始对象”构造
	 * @param parameter 原始对象
	 */
	public JdbcParameterSource(Object parameter) {
		if (parameter == null) {
			source = new MapSqlParameterSource();
		} else {
			if (parameter instanceof SqlParameterSource) {
				source = (SqlParameterSource) parameter;
			} else {
				if (parameter instanceof Map) {
					source = new MapSqlParameterSource((Map) parameter);
				} else {
					source = new BeanPropertySqlParameterSource(parameter);
				}
			}
		}
	}

	/**
	 * 添加一个参数，返回这个参数的键值，添加的新参数不会反映在“原始对象”中
	 * @param value
	 * @return
	 */
	public String addValue(Object value) {
		String paramName = "_" + (++inc);
		this.setValue(paramName, value);
		return paramName;
	}

	/**
	 * 添加一个参数并且明确类型，返回这个参数的键值，添加的新参数不会反映在“原始对象”中
	 * 
	 * @param value 
	 * @param sqlType
	 * @return
	 */
	public String addValue(Object value, int sqlType) {
		String paramName = this.addValue(value);
		if (source instanceof AbstractSqlParameterSource) {
			AbstractSqlParameterSource as = (AbstractSqlParameterSource) source;
			as.registerSqlType(paramName, sqlType);
		} else {
			throw new UnsupportedOperationException();
		}

		return paramName;
	}

	/**
	 * 设置一个参数，不会反映在“原始对象”中
	 * 
	 * @param paramName
	 *            参数名称
	 * @param value
	 *            参数值
	 */
	public void setValue(String paramName, Object value) {
		handSource.addValue(paramName, value);
	}

	/**
	 * 设置一个参数，不会反映在“原始对象”中
	 * 
	 * @param paramName
	 *            参数名称
	 * @param value
	 *            参数值
	 * @param sqlType
	 *            参数类型
	 */
	public void setValue(String paramName, Object value, int sqlType) {
		this.setValue(paramName, value);

		SqlParameterSource source = this.source;
		String param = paramName;
		int subIndex = paramName.lastIndexOf('.');
		if (subIndex > 0 && subIndex < paramName.length() - 1) {
			String subParamName = paramName.substring(subIndex + 1);
			String subSourceName = paramName.substring(0, subIndex);
			source = getSubSource(subSourceName);
			param = subParamName;
		}

		if (source instanceof AbstractSqlParameterSource) {
			AbstractSqlParameterSource as = (AbstractSqlParameterSource) source;
			as.registerSqlType(param, sqlType);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/*
	 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource#getSqlType(java.lang.String)
	 */
	public int getSqlType(String paramName) {
		if (handSource.hasValue(paramName)) {
			return handSource.getSqlType(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length() - 1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getSqlType(subParamName);
			} else {
				return source.getSqlType(paramName);
			}
		}
	}

	/*
	 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource#getTypeName(java.lang.String)
	 */
	public String getTypeName(String paramName) {
		if (handSource.hasValue(paramName)) {
			return handSource.getTypeName(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length() - 1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getTypeName(subParamName);
			} else {
				return source.getTypeName(paramName);
			}
		}
	}

	/*
	 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource#getValue(java.lang.String)
	 */
	public Object getValue(String paramName) throws IllegalArgumentException {
		if (handSource.hasValue(paramName)) {
			return handSource.getValue(paramName);
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length() - 1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).getValue(subParamName);
			} else {
				return source.getValue(paramName);
			}
		}
	}

	/*
	 * @see org.springframework.jdbc.core.namedparam.SqlParameterSource#hasValue(java.lang.String)
	 */
	public boolean hasValue(String paramName) {
		if (handSource.hasValue(paramName)) {
			return true;
		} else {
			int subIndex = paramName.lastIndexOf('.');
			if (subIndex > 0 && subIndex < paramName.length() - 1) {
				String subParamName = paramName.substring(subIndex + 1);
				String subSourceName = paramName.substring(0, subIndex);
				return getSubSource(subSourceName).hasValue(subParamName);
			} else {
				return source.hasValue(paramName);
			}
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
			if (subIndex2 > 0 && subIndex2 < subSourceName.length() - 1) {
				String subParamName2 = subSourceName.substring(subIndex2 + 1);
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
