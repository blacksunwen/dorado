package com.bstek.dorado.jdbc.feature.abdel;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;

public class Test extends AbstractJdbcTestCase {

	public void test1() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		for (int i=1; i<=10; i++) {
			String sql = "INSERT INTO T1 (ID, C1) VALUES (:id, :c1)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "Xx"+i);
			tpl.update(sql, paramSource);
		}
	}
	
	public void test2() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		for (int i=1; i<=10; i++) {
			String sql = "INSERT INTO T1 (ID, C1) VALUES (:id, :c1)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "Xx"+i);
			tpl.update(sql, paramSource);
		}
	}
}
