package com.bstek.dorado.jdbc.feature.arbup;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolver;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * JdbcDataResolver支持批量Update语句
 * @author mark.li@bstek.com
 *
 */
public class ArbupTest extends AbstractJdbcTestCase {

	private TestTable t1 = new TestTable("T1")
				.addColumn("ID", "INT", "PRIMARY KEY")
				.addColumn("C1", "VARCHAR(20)")
				.addColumn("C2", "VARCHAR(20)")
				.addColumn("C3", "VARCHAR(20)")
				.addColumn("C4", "VARCHAR(20)");
	private TestTable t2 = new TestTable("T2")
				.addColumn("ID", "INT", "PRIMARY KEY")
				.addColumn("C1", "VARCHAR(20)")
				.addColumn("C2", "VARCHAR(20)")
				.addColumn("C3", "VARCHAR(20)")
				.addColumn("C4", "VARCHAR(20)");
	
	public ArbupTest() {
		super();
		this.register(t1);
		this.register(t2);
	}
	
	public void test1() throws Exception {
		for(int i=1; i<=10; i++) {
			Record r = new Record();
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "a" + i);
			r.set("C2", "b" + i);
			r.set("C3", "c" + i);
			r.set("C4", "d" + i);
			
			t1.insert(r);
		}
		
		EntityDataType dataType = this.getDataType("dataType1");
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for(int i=1; i<=10; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
			r.getEntityEnhancer().setDataType(dataType);
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "aa" + i);
			r.set("C2", "bb" + i);
			r.set("C3", "cc" + i);
			r.set("C4", "dd" + i);
			
			records.add(r);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		resolver.resolve(dataItems);
		
		for(int i=1; i<=10; i++) {
			Record r = t1.get("ID", Integer.valueOf(i));
			assertEquals(Integer.valueOf(i), r.get("ID"));
			assertEquals("aa" + i, r.get("C1"));
			assertEquals("bb" + i, r.get("C2"));
			assertEquals("c" + i, r.get("C3"));
			assertEquals("d" + i, r.get("C4"));
		}
	}
	
	public void test2() throws Exception {
		for(int i=1; i<=10; i++) {
			Record r = new Record();
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "a" + i);
			r.set("C2", "b" + i);
			r.set("C3", "c" + i);
			r.set("C4", "d" + i);
			
			t1.insert(r);
		}
		
		EntityDataType dataType = this.getDataType("dataType1");
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for(int i=1; i<=10; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
			r.getEntityEnhancer().setDataType(dataType);
			r.getEntityEnhancer().getOldValues(true).put("ID", Integer.valueOf(i));
			r.set("ID", Integer.valueOf(i + 1000));
			r.set("C1", "aa" + i);
			r.set("C2", "bb" + i);
			r.set("C3", "cc" + i);
			r.set("C4", "dd" + i);
			
			records.add(r);
		}
		dataItems.put("t1", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver1");
		resolver.resolve(dataItems);
		
		for(int i=1; i<=10; i++) {
			Record r = t1.get("ID", Integer.valueOf(i));
			assertEquals(Integer.valueOf(i), r.get("ID"));
			assertEquals("aa" + i, r.get("C1"));
			assertEquals("bb" + i, r.get("C2"));
			assertEquals("c" + i, r.get("C3"));
			assertEquals("d" + i, r.get("C4"));
		}
	}
	
	public void test3() throws Exception {
		for(int i=1; i<=10; i++) {
			Record r = new Record();
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "a" + i);
			r.set("C2", "b" + i);
			r.set("C3", "c" + i);
			r.set("C4", "d" + i);
			
			t2.insert(r);
		}
		
		EntityDataType dataType = this.getDataType("dataType2");
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for(int i=1; i<=10; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
			r.getEntityEnhancer().setDataType(dataType);
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "aa" + i);
			r.set("C2", "bb" + i);
			r.set("C3", "cc" + i);
			r.set("C4", "dd" + i);
			
			records.add(r);
		}
		dataItems.put("t2", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver2");
		resolver.resolve(dataItems);
		
		for(int i=1; i<=10; i++) {
			Record r = t2.get("ID", Integer.valueOf(i));
			assertEquals(Integer.valueOf(i), r.get("ID"));
			assertEquals("aa" + i, r.get("C1"));
			assertEquals("bb" + i, r.get("C2"));
			assertEquals("c" + i, r.get("C3"));
			assertEquals("d" + i, r.get("C4"));
		}
	}
	
	public void test4() throws Exception {
		for(int i=1; i<=10; i++) {
			Record r = new Record();
			r.set("ID", Integer.valueOf(i));
			r.set("C1", "a" + i);
			r.set("C2", "b" + i);
			r.set("C3", "c" + i);
			r.set("C4", "d" + i);
			
			t2.insert(r);
		}
		
		EntityDataType dataType = this.getDataType("dataType2");
		DataItems dataItems = new DataItems();
		List<Record> records = new ArrayList<Record>();
		for(int i=1; i<=10; i++) {
			Record r = JdbcUtils.getRecordWithState(new Record(), EntityState.MODIFIED);
			r.getEntityEnhancer().setDataType(dataType);
			r.getEntityEnhancer().getOldValues(true).put("ID", Integer.valueOf(i));
			r.set("ID", Integer.valueOf(i + 1000));
			r.set("C1", "aa" + i);
			r.set("C2", "bb" + i);
			r.set("C3", "cc" + i);
			r.set("C4", "dd" + i);
			
			records.add(r);
		}
		dataItems.put("t2", records);
		
		JdbcDataResolver resolver = this.getResolver("dataResolver2");
		resolver.resolve(dataItems);
		
		for(int i=1; i<=10; i++) {
			Record r = t2.get("ID", Integer.valueOf(i + 1000));
			assertEquals(Integer.valueOf(i + 1000), r.get("ID"));
			assertEquals("aa" + i, r.get("C1"));
			assertEquals("bb" + i, r.get("C2"));
			assertEquals("c" + i, r.get("C3"));
			assertEquals("d" + i, r.get("C4"));
		}
	}
}
