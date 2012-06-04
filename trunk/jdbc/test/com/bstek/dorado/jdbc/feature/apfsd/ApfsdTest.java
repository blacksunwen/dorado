package com.bstek.dorado.jdbc.feature.apfsd;

import java.util.Collection;

import junit.framework.Assert;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

public class ApfsdTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1")
		.addColumn("ID", "INT", "PRIMARY KEY")
		.addColumn("C1", "VARCHAR(20)")
		.addColumn("C2", "VARCHAR(20)")
		.addColumn("C3", "VARCHAR(20)")
		.addColumn("C4", "VARCHAR(20)");
	
	public ApfsdTest() {
		super();
		this.register(t1);
	}
	
	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		int recordCount = 10;
		for (int i=1; i<=recordCount; i++) {
			String sql = "INSERT INTO T1 (ID, C1, C2, C3, C4) VALUES (:id, :c1, :c2, :c3, :c4)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "c1_"+i);
			paramSource.addValue("c2", "c2_"+i);
			paramSource.addValue("c3", "c3_"+i);
			paramSource.addValue("c4", "c4_"+i);
			tpl.update(sql, paramSource);
		}
		
		DataType dataType = this.getDataType("[T1]");
		JdbcDataProvider provider = this.getProvider("dataProvider1");
		
		EntityList<Record> list = (EntityList<Record>)provider.getResult(null, dataType);
		Assert.assertEquals(recordCount, list.size());
		{
			Record r = (Record)list.get(0);
			Assert.assertTrue(r.containsKey("ID"));
			Assert.assertTrue(r.containsKey("C1"));
			Assert.assertTrue(r.containsKey("C2"));
			Assert.assertTrue(r.containsKey("C3"));
			Assert.assertTrue(r.containsKey("C4"));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		int recordCount = 10;
		for (int i=1; i<=recordCount; i++) {
			String sql = "INSERT INTO T1 (ID, C1, C2, C3, C4) VALUES (:id, :c1, :c2, :c3, :c4)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "c1_"+i);
			paramSource.addValue("c2", "c2_"+i);
			paramSource.addValue("c3", "c3_"+i);
			paramSource.addValue("c4", "c4_"+i);
			tpl.update(sql, paramSource);
		}
		
		DataType dataType = this.getDataType("[TC2]");
		JdbcDataProvider provider = this.getProvider("dataProvider1");
		
		EntityList<Record> list = (EntityList<Record>)provider.getResult(null, dataType);
		Assert.assertEquals(recordCount, list.size());
		{
			Record r = (Record)list.get(0);
			Assert.assertTrue(r.containsKey("ID"));
			Assert.assertTrue(r.containsKey("C1"));
			Assert.assertTrue(r.containsKey("C2"));
			Assert.assertTrue(!r.containsKey("C3"));
			Assert.assertTrue(!r.containsKey("C4"));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void test3() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		int recordCount = 10;
		for (int i=1; i<=recordCount; i++) {
			String sql = "INSERT INTO T1 (ID, C1, C2, C3, C4) VALUES (:id, :c1, :c2, :c3, :c4)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "c1_"+i);
			paramSource.addValue("c2", "c2_"+i);
			paramSource.addValue("c3", "c3_"+i);
			paramSource.addValue("c4", "c4_"+i);
			tpl.update(sql, paramSource);
		}
		
		DataType dataType = this.getDataType("[TC2]");
		JdbcDataProvider provider = this.getProvider("dataProvider1");
		
		int pageSize = 2;
		Page<?> page = new Page<Record>(pageSize,3);
		provider.getResult(null, page, dataType);
		Collection<Record> list = (Collection<Record>)page.getEntities();
		Assert.assertEquals(pageSize, list.size());
		{
			Record r = (Record)list.iterator().next();
			Assert.assertTrue(r.containsKey("ID"));
			Assert.assertTrue(r.containsKey("C1"));
			Assert.assertTrue(r.containsKey("C2"));
			Assert.assertTrue(!r.containsKey("C3"));
			Assert.assertTrue(!r.containsKey("C4"));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void test_a1() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		int recordCount = 10;
		for (int i=1; i<=recordCount; i++) {
			String sql = "INSERT INTO T1 (ID, C1, C2, C3, C4) VALUES (:id, :c1, :c2, :c3, :c4)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "c1_"+i);
			paramSource.addValue("c2", "c2_"+i);
			paramSource.addValue("c3", "c3_"+i);
			paramSource.addValue("c4", "c4_"+i);
			tpl.update(sql, paramSource);
		}
		
		DataType dataType = this.getDataType("[AC2]");
		JdbcDataProvider provider = this.getProvider("dataProvider_a1");
		
		EntityList<Record> list = (EntityList<Record>)provider.getResult(null, dataType);
		Assert.assertEquals(recordCount, list.size());
		{
			Record r = (Record)list.get(0);
			Assert.assertTrue(r.containsKey("ID"));
			Assert.assertTrue(r.containsKey("C1"));
			Assert.assertTrue(!r.containsKey("C2"));
			Assert.assertTrue(!r.containsKey("C3"));
			Assert.assertTrue(!r.containsKey("C4"));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void test_a2() throws Exception {
		NamedParameterJdbcTemplate tpl = this.getNamedParameterJdbcTemplate();
		int recordCount = 10;
		for (int i=1; i<=recordCount; i++) {
			String sql = "INSERT INTO T1 (ID, C1, C2, C3, C4) VALUES (:id, :c1, :c2, :c3, :c4)";
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("id", i);
			paramSource.addValue("c1", "c1_"+i);
			paramSource.addValue("c2", "c2_"+i);
			paramSource.addValue("c3", "c3_"+i);
			paramSource.addValue("c4", "c4_"+i);
			tpl.update(sql, paramSource);
		}
		
		DataType dataType = this.getDataType("[AC2]");
		JdbcDataProvider provider = this.getProvider("dataProvider_a1");
		
		int pageSize = 2;
		Page<?> page = new Page<Record>(pageSize,3);
		provider.getResult(null, page, dataType);
		Collection<Record> list = (Collection<Record>)page.getEntities();
		Assert.assertEquals(pageSize, list.size());
		{
			Record r = (Record)list.iterator().next();
			Assert.assertTrue(r.containsKey("ID"));
			Assert.assertTrue(r.containsKey("C1"));
			Assert.assertTrue(!r.containsKey("C2"));
			Assert.assertTrue(!r.containsKey("C3"));
			Assert.assertTrue(!r.containsKey("C4"));
		}
	}
}
