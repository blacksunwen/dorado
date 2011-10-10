package com.bstek.dorado.jdbc.model;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.sql.RecordRowMapper;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.ShiftRowMapperResultSetExtractor;
import com.bstek.dorado.jdbc.sql.SqlGenerator;

public class QueryOperation {

	private DbElement dbElement;
	
	public DbElement getDbElement() {
		return dbElement;
	}

	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}
	
	public QueryOperation(DbElement dbElement) {
		super();
		this.setDbElement(dbElement);
	}

	public Page<Record> getPage() {
		return JdbcQueryContext.getInstance().getPage();
	}
	
	public Object getParameter() {
		return JdbcQueryContext.getInstance().getParameter();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute() {
		DbElement dbElement = this.getDbElement();
		Object parameter = this.getParameter();
		Page<Record> page = this.getPage();
		JdbcEnviroment env = dbElement.getJdbcEnviroment();
		Dialect dialect = env.getDialect();
		NamedParameterJdbcTemplate jdbcTemplate = env.getNamedDao().getNamedParameterJdbcTemplate();
		SqlGenerator generator = JdbcUtils.getSqlGenerator(dbElement);
		
		SelectSql selectSql = generator.selectSql(dbElement, parameter);
		JdbcParameterSource jps = selectSql.getParameterSource();
		RecordRowMapper rowMapper = new RecordRowMapper(dbElement.getAllColumns());
		
		int pageSize = page.getPageSize();
		int firstIndex = page.getFirstEntityIndex();
		
		if (page.getPageSize() > 0) {
			if (dialect.isNarrowSupport()) {
				String sql = dialect.narrowSql(selectSql, pageSize, firstIndex);
				List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
				page.setEntities(rs);
			} else {
				ShiftRowMapperResultSetExtractor rse = 
						new ShiftRowMapperResultSetExtractor(rowMapper, pageSize, firstIndex);
				String sql = dialect.toSQL(selectSql);
				List<Record> rs = jdbcTemplate.query(sql, jps, rse);
				page.setEntities(rs);
			}
			
			String countSql = dialect.toCountSQL(selectSql);
			int count = jdbcTemplate.queryForInt(countSql, selectSql.getParameterSource());
			page.setEntityCount(count);
		} else {
			String sql = dialect.toSQL(selectSql);
			List<Record> rs = jdbcTemplate.query(sql, jps, rowMapper);
			page.setEntities(rs);
		}
	}
	
}
