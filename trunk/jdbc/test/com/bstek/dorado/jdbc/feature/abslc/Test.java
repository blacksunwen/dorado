package com.bstek.dorado.jdbc.feature.abslc;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * Table对象支持查询功能
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
		
		List<Record> result = (List<Record>)provider.getResult();
		assertEquals(10, result.size());
	}
	
	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		JdbcEnviroment env = this.getJdbcEnviroment();
		T1.initData(env, 5);
		
		JdbcDataProvider provider = this.newProvider("T1");
		Record parameter = new Record();
		parameter.put("id", 3);
		
		List<Record> result = (List<Record>)provider.getResult(parameter);
		assertEquals(1, result.size());
		
		Record record = result.get(0);
		assertEquals(Integer.valueOf(3), record.get("ID"));
		assertEquals("x3", record.get("C1"));
	}
	
	@SuppressWarnings("unchecked")
	public void test3() throws Exception {
		JdbcEnviroment env = this.getJdbcEnviroment();
		T1.initData(env, 30);
		
		JdbcDataProvider provider = this.newProvider("T1");
		Record parameter = new Record();
		parameter.put("c1", "3");
		
		List<Record> result = (List<Record>)provider.getResult(parameter);
		assertEquals(4, result.size());
	}
}
