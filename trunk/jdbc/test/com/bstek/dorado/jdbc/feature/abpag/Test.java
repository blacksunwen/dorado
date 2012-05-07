package com.bstek.dorado.jdbc.feature.abpag;

import java.util.List;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.TestTable;

/**
 * Table对象支持查询分页功能
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
		
		JdbcDataProvider provider = this.newProvider("T1");
		{
			Page<?> page = new Page<Object>(2, 1);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
		
		{
			Page<?> page = new Page<Object>(2, 3);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
		
		{
			Page<?> page = new Page<Object>(2, 5);
			provider.getResult(page);
			List<Record> result = (List<Record>)page.getEntities();
			assertEquals(2, result.size());
		}
	}
}
