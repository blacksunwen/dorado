package com.bstek.dorado.jdbc.feature.arbtg;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;
import com.bstek.dorado.jdbc.test.TestTrigger;

/**
 * Table对象update功能，支持retrieve
 * @author mark.li@bstek.com
 *
 */
public class ArbtgTest extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1")
			.addColumn("ID", "INT", "PRIMARY KEY")
			.addColumn("C1", "VARCHAR(20)")
			.addColumn("C2", "VARCHAR(20)")
			.addColumn("C3", "VARCHAR(20)");
	private TestTrigger tg1 = new TestTrigger("TRIG_BEFORE_UPT")
			.setTableName("T1")
			.setPosition("BEFORE UPDATE")
			.setClazz(BeforeUpdateTrigger.class);

	public ArbtgTest() {
		super();

		this.register(t1);
		this.register(tg1);
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
		Record record = JdbcUtils.getRecordWithState(new Record(),
				EntityState.MODIFIED);
		EntityDataType dataType = this.getDataType("dataType1");
		record.getEntityEnhancer().setDataType(dataType);
		
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

	public void test2() throws Exception {
		for(int i=1; i<=10; i++) {
			Record record = new Record();
			record.set("ID", Integer.valueOf(i));
			record.set("C1", "c1");
			record.set("C2", "c2");
			record.set("C3", "c3");
			t1.insert(record);
		}
		
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for(int i=1; i<=10; i++) {
			Record record = JdbcUtils.getRecordWithState(new Record(),
					EntityState.MODIFIED);
			EntityDataType dataType = this.getDataType("dataType1");
			record.getEntityEnhancer().setDataType(dataType);
			
			record.put("ID", Integer.valueOf(i));
			record.put("C1", "Xx");
			record.put("C2", "Xx"+i);
			records.add(record);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolverItem item = new JdbcDataResolverItem();
		item.setName("t1");
		item.setTableName("T1");
		JdbcDataResolver resolver = newResolver(item);
		resolver.resolve(dataItems);
		
		for(int i=1; i<=10; i++) {
			Record record = records.get(i-1);
			assertEquals(Integer.valueOf(i), Integer.valueOf(record.getInt("ID")));
			assertEquals("Xx", record.get("C1"));
			assertEquals("Xx" + i + "_2", record.get("C2"));
			assertEquals(null, record.get("C3"));
		}
	}
}
