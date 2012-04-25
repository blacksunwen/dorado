package com.bstek.dorado.jdbc.support;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;

public class QueryCommand {

	private static Log logger = LogFactory.getLog(QueryCommand.class);
	
	public boolean execute(JdbcDataProviderOperation operation) throws Exception {
		Page<Record> page = operation.getJdbcContext().getPage();
		if (page.getPageSize() > 0) {
			this.loadPageRecord(operation);
		} else {
			this.loadAllRecords(operation);
		}
		return true;
	}
	
	/**
	 * 加载全部的记录
	 */
	private void loadAllRecords(JdbcDataProviderOperation operation) throws Exception {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		DbTable dbTable = operation.getDbTable();
		
		SelectSql selectSql = dbTable.selectSql(operation);
		
		RecordRowMapper rowMapper = new RecordRowMapper(dbTable.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();

		String sql = env.getDialect().toSQL(selectSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[SELECT-SQL]" + sql);
		}
		List<Record> rs = jdbcTemplate.query(sql, selectSql.getParameterSource(), rowMapper);
		jdbcContext.getPage().setEntities(rs);
	}

	/**
	 * 加载当前分页的记录
	 */
	private void loadPageRecord(JdbcDataProviderOperation operation) throws Exception {
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		Page<Record> page = jdbcContext.getPage();
		JdbcEnviroment env = operation.getJdbcEnviroment();
		Dialect dialect = env.getDialect();

		DbTable dbTable = operation.getDbTable();
		SelectSql selectSql = dbTable.selectSql(operation);

		RecordRowMapper rowMapper = new RecordRowMapper(dbTable.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getSpringNamedDao().getNamedParameterJdbcTemplate();

		int pageSize = page.getPageSize();
		int firstIndex = page.getFirstEntityIndex() + 1;

		if (dialect.isNarrowSupport()) {
			String sql = dialect.narrowSql(selectSql, pageSize, firstIndex);
			if (logger.isDebugEnabled()) {
				logger.debug("[SELECT-SQL]" + sql);
			}
			List<Record> rs = jdbcTemplate.query(sql, selectSql.getParameterSource(), rowMapper);
			page.setEntities(rs);
		} else {
			ShiftRowMapperResultSetExtractor rse = new ShiftRowMapperResultSetExtractor(
					rowMapper, pageSize, firstIndex);
			String sql = dialect.toSQL(selectSql);
			if (logger.isDebugEnabled()) {
				logger.debug("[SELECT-SQL]" + sql);
			}
			List<Record> rs = jdbcTemplate.query(sql, selectSql.getParameterSource(), rse);
			page.setEntities(rs);
		}

		String countSql = dialect.toCountSQL(selectSql);
		if (logger.isDebugEnabled()) {
			logger.debug("[COUNT-SQL]" + countSql);
		}
		int count = jdbcTemplate.queryForInt(countSql, selectSql.getParameterSource());
		page.setEntityCount(count);
	}
}
