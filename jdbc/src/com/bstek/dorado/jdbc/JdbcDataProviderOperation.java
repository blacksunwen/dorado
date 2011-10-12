package com.bstek.dorado.jdbc;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.AbstractJdbcOperation;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;
import com.bstek.dorado.jdbc.sql.SqlGenerator;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcDataProviderOperation extends
		AbstractJdbcOperation<JdbcDataProviderContext> {

	public JdbcDataProviderOperation(DbElement dbElement,
			JdbcDataProviderContext jdbcContext) {
		super(dbElement, jdbcContext);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void execute() {
		Page<Record> page = this.getJdbcContext().getPage();
		if (page.getPageSize() > 0) {
			this.loadPageRecord();
		} else {
			this.loadAllRecords();
		}
	}

	/**
	 * 加载全部的记录
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void loadAllRecords() {
		JdbcDataProviderContext jdbcContext = this.getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();

		DbElement dbElement = this.getDbElement();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		SelectSql selectSql = generator.selectSql(dbElement, jdbcContext.getParameter(), jdbcContext);
		JdbcParameterSource jps = selectSql.getParameterSource();
		RecordRowMapper rowMapper = new RecordRowMapper(dbElement.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		String sql = env.getDialect().toSQL(selectSql);
		List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
		jdbcContext.getPage().setEntities(rs);
	}

	/**
	 * 加载当前分页的记录
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void loadPageRecord() {
		JdbcDataProviderContext jdbcContext = this.getJdbcContext();
		Page<Record> page = jdbcContext.getPage();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		Dialect dialect = env.getDialect();

		DbElement dbElement = this.getDbElement();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		SelectSql selectSql = generator.selectSql(dbElement, jdbcContext.getParameter(), jdbcContext);
		RecordRowMapper rowMapper = new RecordRowMapper(dbElement.getAllColumns());

		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();

		int pageSize = page.getPageSize();
		int firstIndex = page.getFirstEntityIndex();

		if (dialect.isNarrowSupport()) {
			JdbcParameterSource jps = selectSql.getParameterSource();
			String sql = dialect.narrowSql(selectSql, pageSize, firstIndex);
			List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
			page.setEntities(rs);
		} else {
			ShiftRowMapperResultSetExtractor rse = new ShiftRowMapperResultSetExtractor(
					rowMapper, pageSize, firstIndex);
			String sql = dialect.toSQL(selectSql);
			JdbcParameterSource jps = selectSql.getParameterSource();
			List<Record> rs = jdbcTemplate.query(sql, jps, rse);
			page.setEntities(rs);
		}

		String countSql = dialect.toCountSQL(selectSql);
		int count = jdbcTemplate.queryForInt(countSql, selectSql.getParameterSource());
		page.setEntityCount(count);
	}
}
