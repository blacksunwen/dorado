package com.bstek.dorado.jdbc.feature.abutg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;

public class Test extends AbstractJdbcTestCase {

	static class T1 {
		static void create(JdbcEnviroment env) {
			String createScript = "CREATE TABLE IF NOT EXISTS T1 (" +
					"ID INT PRIMARY KEY, C1 VARCHAR(20), C2 VARCHAR(20), C3 VARCHAR(20)" +
					")";
			env.getSpringNamedDao().getJdbcTemplate().update(createScript);
		}
		
		static Record get(JdbcEnviroment env, Integer id) {
			NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
			
			String sql = "SELECT * FROM T1 WHERE ID = :id";
			return tpl.queryForObject(sql, Collections.singletonMap("id", id), new RowMapper<Record>(){

				public Record mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Record record = new Record();
					record.set("ID", rs.getInt("ID"));
					record.set("C1", rs.getString("C1"));
					record.set("C2", rs.getString("C2"));
					record.set("C3", rs.getString("C3"));
					return record;
				}
			});
		}
		
		static void initData(JdbcEnviroment env) {
			create(env);
			NamedParameterJdbcTemplate tpl = env.getSpringNamedDao().getNamedParameterJdbcTemplate();
			String sql = "INSERT INTO T1(ID,C1,C2,C3)VALUES(:id,:c1,:c2,:c3)";
			MapSqlParameterSource ps = new MapSqlParameterSource();
			ps.addValue("id", Integer.valueOf(10));
			ps.addValue("c1", "c1");
			ps.addValue("c2", "c2");
			ps.addValue("c3", "c3");
			tpl.update(sql, ps);
		}
	}
	
	static class TRIG_BEFORE_UPD {
		static void create(JdbcEnviroment env) {
			String script = "CREATE TRIGGER IF NOT EXISTS TRIG_BEFORE_INS BEFORE UPDATE ON T1 FOR EACH ROW CALL \"" 
					+ BeforeUpdateTrigger.class.getName() + "\"";
			
			env.getSpringNamedDao().getJdbcTemplate().update(script);
		}
	}
	
	public void test1() throws Exception {
		JdbcEnviroment env = this.getJdbcEnviroment();
		T1.initData(env);
		TRIG_BEFORE_UPD.create(env);
		
		DataItems dataItems = new DataItems();
		Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED) ;
		record.put("ID", Integer.valueOf(10));
		record.put("C1", "Xx");
		record.put("C2", "Xx");
		dataItems.put("t1", record);
		
		JdbcDataResolverItem item = new JdbcDataResolverItem();
		item.setName("t1");
		item.setTableName("T1");
		JdbcDataResolver resolver = newResolver(item);
		
		resolver.resolve(dataItems);
		
		assertEquals("Xx_2", record.get("C2"));
	}
}
