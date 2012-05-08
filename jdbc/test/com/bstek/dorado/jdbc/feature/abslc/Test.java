package com.bstek.dorado.jdbc.feature.abslc;

import java.util.List;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.test.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.test.TestTable;

/**
 * Table对象支持查询功能
 * @author mark.li@bstek.com
 *
 */
public class Test extends AbstractJdbcTestCase {
	private TestTable t1 = new TestTable("T1");
	
	public Test() {
		super();
		
		this.register(
			t1.addColumn("ID", "INT")
			  .addColumn("C1", "VARCHAR(20)")
		);
	}
	
	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		for (int i=1; i<=10; i++) {
			Record record = new Record();
			record.put("ID", i);
			record.put("C1", "x" + i);
			t1.insert(record);
		}
		
		JdbcDataProvider provider = newProvider("T1");
		List<Record> result = (List<Record>)provider.getResult();
		assertEquals(10, result.size());
	}
	
	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		for (int i=1; i<=5; i++) {
			Record record = new Record();
			record.put("ID", i);
			record.put("C1", "x" + i);
			t1.insert(record);
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
	
	@SuppressWarnings("unchecked")
	public void test3() throws Exception {
		for (int i=1; i<=30; i++) {
			Record record = new Record();
			record.put("ID", i);
			record.put("C1", "x" + i);
			t1.insert(record);
		}
		
		JdbcDataProvider provider = newProvider("T1");
		Record parameter = new Record();
		parameter.put("c1", "3");
		
		List<Record> result = (List<Record>)provider.getResult(parameter);
		assertEquals(4, result.size());
	}
}
