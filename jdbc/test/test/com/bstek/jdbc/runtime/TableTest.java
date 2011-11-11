package test.com.bstek.jdbc.runtime;

import junit.framework.Assert;
import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.model.table.TableColumn;

public class TableTest extends ConfigManagerTestSupport{

	public TableTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test0() throws Exception {
		Table t = JdbcTestUtils.table("people0");
		
		System.out.println(t);
		
		Assert.assertEquals("people0", t.getName());
		Assert.assertEquals(null, t.getCatalog());
		Assert.assertEquals(null, t.getSchema());
		Assert.assertEquals("PEOPLE", t.getTableName());
		Assert.assertEquals("Table", t.getType());
		Assert.assertEquals("xxxxxx", t.getDynamicClause());
		
		Assert.assertEquals(3, t.getAllColumns().size());
		Assert.assertEquals(0, t.getKeyColumns().size());
		
		{
			TableColumn c = (TableColumn)t.getColumn("ID");
			Assert.assertEquals(c, t.getColumn("ID"));
			Assert.assertEquals("ID",   c.getColumnName());
			Assert.assertEquals("p_id", c.getPropertyName());
			Assert.assertEquals(null,   c.getInsertDefaultValue());
			Assert.assertEquals(null,   c.getUpdateDefaultValue());
			Assert.assertNotNull(c.getJdbcType());
			Assert.assertEquals(true,   c.isInsertable());
			Assert.assertEquals(false,  c.isSelectable());
			Assert.assertEquals(true,   c.isUpdatable());
		}
		
		{
			TableColumn c = (TableColumn)t.getAllColumns().get(1);
			Assert.assertEquals(c, t.getColumn("NAME"));
			Assert.assertEquals("NAME",   c.getColumnName());
			Assert.assertEquals("p_name", c.getPropertyName());
			Assert.assertEquals(null,     c.getInsertDefaultValue());
			Assert.assertEquals(null,     c.getUpdateDefaultValue());
			Assert.assertNotNull(c.getJdbcType());
			Assert.assertEquals(true,     c.isInsertable());
			Assert.assertEquals(true,     c.isSelectable());
			Assert.assertEquals(true,     c.isUpdatable());
		}
		
		{
			TableColumn c = (TableColumn)t.getAllColumns().get(2);
			Assert.assertEquals(c, t.getColumn("SEX"));
			Assert.assertEquals("SEX",   c.getColumnName());
			Assert.assertEquals("p_sex", c.getPropertyName());
			Assert.assertEquals(null,    c.getInsertDefaultValue());
			Assert.assertEquals(null,    c.getUpdateDefaultValue());
			Assert.assertNotNull(c.getJdbcType());
			Assert.assertEquals(true,    c.isInsertable());
			Assert.assertEquals(true,    c.isSelectable());
			Assert.assertEquals(true,    c.isUpdatable());
		}
	}

	public void test1() throws Exception{
		Table t1 = JdbcTestUtils.table("EMP");
		Table t2 = JdbcTestUtils.table("EMP_AUTO");
		
		{
			Assert.assertEquals(t1.getCatalog(), t2.getCatalog());
			Assert.assertEquals(t1.getAllColumns().size(), t2.getAllColumns().size());
		}
	}
}
