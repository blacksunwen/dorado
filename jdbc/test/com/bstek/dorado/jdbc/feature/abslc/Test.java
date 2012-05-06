package com.bstek.dorado.jdbc.feature.abslc;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.AbstractTestTable;
import com.bstek.dorado.jdbc.AbstractTestTable.Action;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * Table对象支持查询功能
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {

	static class T1 extends AbstractTestTable {

		public T1(JdbcEnviroment jdbcEnviroment) {
			super(jdbcEnviroment);
		}

		@Override
		public String getCreateScript() {
			String script = "CREATE TABLE T1 (" +
					"ID INT, C1 VARCHAR(20)" +
					")";
			return script;
		}

		@Override
		public String getDropScript() {
			return "DROP TABLE T1";
		}
		
	}

	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		T1 t1 = new T1(this.getJdbcEnviroment());
		t1.doTest(new Action(){

			public void doAction(JdbcEnviroment jdbcEnviroment) throws Exception {
				NamedParameterJdbcTemplate tmp = jdbcEnviroment.getSpringNamedDao().getNamedParameterJdbcTemplate();
				jdbcEnviroment.getSpringNamedDao().getJdbcTemplate().update("TRUNCATE TABLE T1");
				
				String sql = "INSERT INTO T1(ID,C1) VALUES(:id,:c1)";
				for (int i=1; i<=10; i++) {
					MapSqlParameterSource ps = new MapSqlParameterSource();
					ps.addValue("id", i);
					ps.addValue("c1", "x" + i);
					tmp.update(sql, ps);
				}
				
				JdbcDataProvider provider = newProvider("T1");
				
				List<Record> result = (List<Record>)provider.getResult();
				assertEquals(10, result.size());
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		new T1(this.getJdbcEnviroment()).doTest(new Action(){

			public void doAction(JdbcEnviroment jdbcEnviroment)
					throws Exception {
				NamedParameterJdbcTemplate tmp = jdbcEnviroment.getSpringNamedDao().getNamedParameterJdbcTemplate();
				jdbcEnviroment.getSpringNamedDao().getJdbcTemplate().update("TRUNCATE TABLE T1");
				
				String sql = "INSERT INTO T1(ID,C1) VALUES(:id,:c1)";
				for (int i=1; i<=5; i++) {
					MapSqlParameterSource ps = new MapSqlParameterSource();
					ps.addValue("id", i);
					ps.addValue("c1", "x" + i);
					tmp.update(sql, ps);
				}
				
				JdbcDataProvider provider = newProvider("T1");
				Record parameter = new Record();
				parameter.put("id", 3);
				
				List<Record> result = (List<Record>)provider.getResult(parameter);
				assertEquals(1, result.size());
				
				Record record = result.get(0);
				assertEquals(Integer.valueOf(3), record.get("ID"));
				assertEquals("x3", record.get("C1"));
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void test3() throws Exception {
		new T1(this.getJdbcEnviroment()).doTest(new Action(){

			public void doAction(JdbcEnviroment jdbcEnviroment)
					throws Exception {
				NamedParameterJdbcTemplate tmp = jdbcEnviroment.getSpringNamedDao().getNamedParameterJdbcTemplate();
				jdbcEnviroment.getSpringNamedDao().getJdbcTemplate().update("TRUNCATE TABLE T1");
				
				String sql = "INSERT INTO T1(ID,C1) VALUES(:id,:c1)";
				for (int i=1; i<=30; i++) {
					MapSqlParameterSource ps = new MapSqlParameterSource();
					ps.addValue("id", i);
					ps.addValue("c1", "x" + i);
					tmp.update(sql, ps);
				}
				
				JdbcDataProvider provider = newProvider("T1");
				Record parameter = new Record();
				parameter.put("c1", "3");
				
				List<Record> result = (List<Record>)provider.getResult(parameter);
				assertEquals(4, result.size());
			}
			
		});
	}
}
