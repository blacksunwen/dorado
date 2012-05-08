package com.bstek.dorado.jdbc.feature.abutg;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;
import com.bstek.dorado.jdbc.test.TestTrigger;

public class Test extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1");
	private TestTrigger tg1 = new TestTrigger("TRIG_BEFORE_UPT");
	
	public Test() {
		super();
		
		this.register(
			t1.addColumn("ID", "INT", "PRIMARY KEY")
			  .addColumn("C1", "VARCHAR(20)")
			  .addColumn("C2", "VARCHAR(20)")
			  .addColumn("C3", "VARCHAR(20)")
		);
		
		this.register(
			tg1.setTableName("T1")
			   .setPosition("BEFORE UPDATE")
			   .setClazz(BeforeUpdateTrigger.class)
		);
	}
	
	public void test1() throws Exception {
		{
			Record record = new Record();
			record.set("ID", Integer.valueOf(10));
			record.set("C1", "c1");
			record.set("C2", "c2");
			record.set("C3", "c3");
			t1.insert(record);
		}
		
		
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
