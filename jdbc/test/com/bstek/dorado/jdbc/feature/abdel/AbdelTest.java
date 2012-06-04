package com.bstek.dorado.jdbc.feature.abdel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * Table对象delete功能，支持根据主键值delete
 * @author mark.li@bstek.com
 *
 */
public class AbdelTest extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1")
		.addColumn("ID", "INT", "PRIMARY KEY")
		.addColumn("C1", "VARCHAR(20)");
	
	public AbdelTest() {
		super();
		this.register(t1);
	}
	
	public void test1() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		for (int i=1; i<=10; i++) {
			String sql = "INSERT INTO T1 (ID, C1) VALUES (:id, :c1)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "Xx"+i);
			tpl.update(sql, paramSource);
		}
		
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for (int i=1; i<=5; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.DELETED);
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "Xx"+i);
			records.add(r);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		resolver.resolve(dataItems);
		
		for (int i=1; i<=5; i++) {
			List<Map<String,Object>> list = tpl.queryForList("select * from T1 where ID = :id", 
					Collections.singletonMap("id", Integer.valueOf(i)));
			assertEquals(0, list.size());
		}
		
		for (int i=6; i<=10; i++) {
			List<Map<String,Object>> list = tpl.queryForList("select * from T1 where ID = :id", 
					Collections.singletonMap("id", Integer.valueOf(i)));
			assertEquals(1, list.size());
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
		
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for (int i=1; i<=5; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.DELETED);
			r.getEntityEnhancer().getOldValues(true).put("ID", Integer.valueOf(i));
			r.set("ID", Integer.valueOf(i+1000));
			r.set("C1", "Xx"+i);
			records.add(r);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		resolver.resolve(dataItems);
		
		for (int i=1; i<=5; i++) {
			List<Map<String,Object>> list = tpl.queryForList("select * from T1 where ID = :id", 
					Collections.singletonMap("id", Integer.valueOf(i)));
			assertEquals(0, list.size());
		}
		
		for (int i=6; i<=10; i++) {
			List<Map<String,Object>> list = tpl.queryForList("select * from T1 where ID = :id", 
					Collections.singletonMap("id", Integer.valueOf(i)));
			assertEquals(1, list.size());
		}
	}
}
