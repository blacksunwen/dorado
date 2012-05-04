package com.bstek.dorado.jdbc.feature.abpag;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * Table对象支持查询分页功能
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {

	static class T1 {
		static void create(JdbcEnviroment env) {
			String createScript = "CREATE TABLE IF NOT EXISTS T1 (" +
					"ID INT, C1 VARCHAR(20)" +
					")";
			env.getSpringNamedDao().getJdbcTemplate().update(createScript);
		}
		
		static void drop(JdbcEnviroment env) {
			String dropScript = "DROP TABLE IF EXISTS T1";
			env.getSpringNamedDao().getJdbcTemplate().update(dropScript);
		}
		
		static void initData(JdbcEnviroment env, int count) {
			create(env);
			
			NamedParameterJdbcTemplate tmp = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
			env.getSpringNamedDao().getJdbcTemplate().update("TRUNCATE TABLE T1");
			
			String sql = "INSERT INTO T1(ID,C1) VALUES(:id,:c1)";
			for (int i=1; i<=count; i++) {
				MapSqlParameterSource ps = new MapSqlParameterSource();
				ps.addValue("id", i);
				ps.addValue("c1", "x" + i);
				tmp.update(sql, ps);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		JdbcEnviroment env = this.getJdbcEnviroment();
		T1.initData(env, 10);
		
		JdbcDataProvider provider = this.newProvider("T1");
		{
			Page<?> page = new Page<Object>(2, 1);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
		
		{
			Page<?> page = new Page<Object>(2, 3);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
		
		{
			Page<?> page = new Page<Object>(2, 5);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
	}
}
