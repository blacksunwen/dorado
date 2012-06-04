package com.bstek.dorado.jdbc.feature.abitg;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;
import com.bstek.dorado.jdbc.test.TestTrigger;

public class AbitgTest extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1");
	private TestTrigger tg1 = new TestTrigger("TRIG_BEFORE_INS");
	
	public AbitgTest() {
		super();
		
		this.register(
			t1.addColumn("ID", "INT", "PRIMARY KEY")
			  .addColumn("C1", "VARCHAR(20)")
			  .addColumn("C2", "VARCHAR(20)")
			  .addColumn("C3", "VARCHAR(20)")
		);
		
		this.register(
			tg1.setPosition("BEFORE INSERT")
			   .setTableName("T1")
			   .setClazz(BeforeInsertTrigger.class)
		);
	}
	
	public void test1() throws Exception {
		DataItems dataItems = new DataItems();
		Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
		record.put("ID", Integer.valueOf(10));
		record.put("C1", "Xx");
		dataItems.put("t1", record);
		
		JdbcDataResolverItem item = new JdbcDataResolverItem();
		item.setName("t1");
		item.setTableName("T1");
		item.setSupportBatchSql(false);
		JdbcDataResolver resolver = newResolver(item);
		
		resolver.resolve(dataItems);
		
		Record record2 = t1.get("ID", Integer.valueOf(10));
		
		assertEquals(Integer.valueOf(10), record2.get("ID"));
		assertEquals("Xx", record2.get("C1"));
		assertEquals("Xx_2", record2.get("C2"));
	}
	
	public void test2() throws Exception {
		Integer id = Integer.valueOf(20);
		DataItems dataItems = new DataItems();
		Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
		record.put("ID", id);
		record.put("C1", "Xx");
		record.put("C2", null);
		dataItems.put("t1", record);
		
		JdbcDataResolverItem item = new JdbcDataResolverItem();
		item.setName("t1");
		item.setTableName("T1");
		item.setSupportBatchSql(false);
		JdbcDataResolver resolver = newResolver(item);
		
		resolver.resolve(dataItems);
		assertEquals("Xx_2", record.get("C2"));
		
		Record record2 = t1.get("ID", id);
		
		assertEquals(id, record2.get("ID"));
		assertEquals("Xx", record2.get("C1"));
		assertEquals("Xx_2", record2.get("C2"));
	}
}
