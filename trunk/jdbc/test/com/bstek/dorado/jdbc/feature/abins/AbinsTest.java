package com.bstek.dorado.jdbc.feature.abins;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * Table对象支持插入功能
 * @author mark.li@bstek.com
 *
 */
public class AbinsTest extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1")
			.addColumn("ID", "INT", "PRIMARY KEY")
			.addColumn("C1", "VARCHAR(20)");
	
	public AbinsTest() {
		super();
		this.register(t1);
	}
	
	public void test1() throws Exception {
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		
		Integer id = Integer.valueOf(10);
		DataItems dataItems = new DataItems();
		{
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			
			record.put("ID", id);
			record.put("C1", "Xx");
			dataItems.put("t1", record);
		}
		
		resolver.resolve(dataItems);
		Record record2 = t1.get("ID", id);
		
		assertEquals(id, record2.get("ID"));
		assertEquals("Xx", record2.get("C1"));
	}
	
	public void test2() throws Exception {
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		dataItems.put("t1", records);

		for (int i=1; i<=20; i++) {
			Record record = JdbcUtils.getRecordWithState(new Record(), EntityState.NEW) ;
			record.put("ID", Integer.valueOf(i));
			record.put("C1", "Xx" + i);
			
			records.add(record);
		}
		
		resolver.resolve(dataItems);
		
		for (int i=1; i<=20; i++) {
			Integer id = Integer.valueOf(i);
			Record record = t1.get("ID", id);
			
			assertEquals(id, record.get("ID"));
			assertEquals("Xx" + i, record.get("C1"));
		}
	}
}
