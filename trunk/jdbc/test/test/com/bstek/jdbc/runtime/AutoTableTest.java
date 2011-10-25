package test.com.bstek.jdbc.runtime;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.BaseMatchRule;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.JoinTable;
import com.bstek.dorado.jdbc.model.autotable.JunctionMatchRule;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.autotable.Where;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;

public class AutoTableTest extends ConfigManagerTestSupport {

	public AutoTableTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test1() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t1");
		
		{
			Assert.assertEquals("t1", at.getName());
			Assert.assertEquals("emp", at.getMainTableAlias());
		}
		{
			Assert.assertEquals(6, at.getAllColumns().size());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_id");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("ID", c.getColumnName());
			Assert.assertEquals("p_id", c.getPropertyName());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_last_name");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("LAST_NAME", c.getColumnName());
			Assert.assertEquals("p_last_name", c.getPropertyName());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_first_name");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("FIRST_NAME", c.getColumnName());
			Assert.assertEquals("p_first_name", c.getPropertyName());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_title");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("TITLE", c.getColumnName());
			Assert.assertEquals("p_title", c.getPropertyName());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_sex");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("SEX", c.getColumnName());
			Assert.assertEquals("p_sex", c.getPropertyName());
		}
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_birth_date");
			Assert.assertEquals("emp", c.getTableAlias());
			Assert.assertEquals("BIRTH_DATE", c.getColumnName());
			Assert.assertEquals("p_birth_date", c.getPropertyName());
		}
		
		{
			Assert.assertEquals(1, at.getFromTables().size());
		}
		{
			FromTable ft = at.getFromTable("emp");
			Assert.assertEquals("emp", ft.getTableAlias());
			Assert.assertEquals("EMP_0", ft.getTable().getName());
		}
		
		//-----------
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at1");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertEquals(9, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at1");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(9, page.getEntityCount());
		}
		
	}
	
	public void test2() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t2");
		{
			Assert.assertEquals("t2", at.getName());
		}
		
		Assert.assertEquals(7, at.getAllColumns().size());
		{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_pid");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("ID", c.getColumnName());
			Assert.assertEquals("p_pid", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_pname");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("PRODUCT_NAME", c.getColumnName());
			Assert.assertEquals("p_pname", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_psupplier");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("SUPPLIER_ID", c.getColumnName());
			Assert.assertEquals("p_psupplier", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_pcategory");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("CATEGORY_ID", c.getColumnName());
			Assert.assertEquals("p_pcategory", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_pquanitity");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("QUANTITY_PER_UNIT", c.getColumnName());
			Assert.assertEquals("p_pquanitity", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_punitprice");
			Assert.assertEquals("p", c.getTableAlias());
			Assert.assertEquals("UNIT_PRICE", c.getColumnName());
			Assert.assertEquals("p_punitprice", c.getPropertyName());
		}{
			AutoTableColumn c = (AutoTableColumn)at.getColumn("n_ccategory");
			Assert.assertEquals("c", c.getTableAlias());
			Assert.assertEquals("CATEGORY_NAME", c.getColumnName());
			Assert.assertEquals("p_ccategory", c.getPropertyName());
		}
		
		Assert.assertEquals(2, at.getFromTables().size());
		{
			FromTable ft = at.getFromTable("c");
			Assert.assertEquals("CATEGORIES", ft.getTable().getTableName());
			Assert.assertEquals(DbElement.Type.Table, ft.getTable().getType());
		}{
			FromTable ft = at.getFromTable("p");
			Assert.assertEquals("PRODUCTS", ft.getTable().getTableName());
			Assert.assertEquals(DbElement.Type.Table, ft.getTable().getType());
		}
		
		Assert.assertEquals(1, at.getJoinTables().size());
		{
			JoinTable jt = at.getJoinTables().get(0);
			
			Assert.assertEquals("p", jt.getLeftFromTableAlias());
			String[] leftColumnNames = jt.getLeftColumnNames();
			Assert.assertEquals(1, leftColumnNames.length);
			Assert.assertEquals("CATEGORY_ID", leftColumnNames[0]);
			
			Assert.assertEquals("c", jt.getRightFromTableAlias());
			String[] rightColumnNames = jt.getRightColumnNames();
			Assert.assertEquals(1, rightColumnNames.length);
			Assert.assertEquals("ID", rightColumnNames[0]);
			
			Assert.assertEquals(JoinModel.LEFT_JOIN, jt.getJoinModel());
		}
		
		//-----------
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at2");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertTrue(rs.size() > 0);
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at2");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertTrue(page.getEntityCount() > 0);
		}
	}
	
	public void test3() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t3");
		Assert.assertEquals(5, at.getOrders().size());
		{
			Order od = at.getOrders().get(0);
			Assert.assertEquals("c", od.getTableAlias());
			Assert.assertEquals("ID", od.getColumnName());
		}{
			Order od = at.getOrders().get(1);
			Assert.assertEquals("p", od.getTableAlias());
			Assert.assertEquals("ID", od.getColumnName());
			Assert.assertEquals(OrderModel.ASC, od.getOrderModel());
		}{
			Order od = at.getOrders().get(2);
			Assert.assertEquals("p", od.getTableAlias());
			Assert.assertEquals("PRODUCT_NAME", od.getColumnName());
			Assert.assertEquals(NullsModel.NULLS_LAST, od.getNullsModel());
		}{
			Order od = at.getOrders().get(3);
			Assert.assertEquals("p", od.getTableAlias());
			Assert.assertEquals("SUPPLIER_ID", od.getColumnName());
			Assert.assertEquals(OrderModel.DESC, od.getOrderModel());
			Assert.assertEquals(NullsModel.NULLS_FIRST, od.getNullsModel());
		}{
			Order od = at.getOrders().get(4);
			Assert.assertEquals("n_pname", od.getColumnName());
		}
		
		//-----------
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at3");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertTrue(rs.size() > 0);
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at3");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertTrue(page.getEntityCount() > 0);
		}
	}
	
	public void test4() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t4");
		Where where = at.getWhere();
		Assert.assertNotNull(where);
		Assert.assertEquals(1, where.getMatchRules().size());
		{
			BaseMatchRule r = (BaseMatchRule) where.getMatchRules().get(0);
			Assert.assertEquals("emp", r.getTableAlias());
			Assert.assertEquals("ID", r.getColumnName());
			Assert.assertEquals("=", r.getOperator());
			Assert.assertEquals(":arg_id", r.getValue());
		}
		
		//-----------
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at4");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertEquals(9, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at4");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(9, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at4");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_id", 1);
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(1, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at4");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_id", 1);
			Page page = new Page(2,1);
			provider.getResult(param, page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			Assert.assertEquals(1, rs.size());
			Assert.assertEquals(1, page.getEntityCount());
		}
	}
	
	public void test5() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t5");
		Where where = at.getWhere();
		Assert.assertNotNull(where);
		Assert.assertEquals(1, where.getMatchRules().size());
		
		JunctionMatchRule amr = (JunctionMatchRule)where.getMatchRules().get(0);
		Assert.assertEquals(JunctionModel.OR, amr.getModel());
		Assert.assertEquals(2, amr.getMatchRules().size());
		
		{
			BaseMatchRule r = (BaseMatchRule) amr.getMatchRules().get(0);
			Assert.assertEquals("emp", r.getTableAlias());
			Assert.assertEquals("LAST_NAME", r.getColumnName());
			Assert.assertEquals("like%", r.getOperator());
			Assert.assertEquals(":arg_last", r.getValue());
		}{
			BaseMatchRule r = (BaseMatchRule) amr.getMatchRules().get(1);
			Assert.assertEquals("emp", r.getTableAlias());
			Assert.assertEquals("FIRST_NAME", r.getColumnName());
			Assert.assertEquals("%like%", r.getOperator());
			Assert.assertEquals(":arg_first", r.getValue());
		}
		
		//-----------
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertEquals(9, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(9, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(1, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Page page = new Page(2,1);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			Assert.assertEquals(1, rs.size());
			Assert.assertEquals(1, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_last", "D");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(2, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_last", "D");
			Page page = new Page(2,2);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			Assert.assertEquals(0, rs.size());
			Assert.assertEquals(2, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			param.put("arg_last", "D");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(2, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at5");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			param.put("arg_last", "D");
			Page page = new Page(2,2);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			Assert.assertEquals(0, rs.size());
			Assert.assertEquals(2, page.getEntityCount());
		}
	}
	
	public void test6() throws Exception {
		AutoTable at = JdbcTestUtils.autoTable("t6");
		Where where = at.getWhere();
		Assert.assertNotNull(where);
		Assert.assertEquals(2, where.getMatchRules().size());
		
		{
			BaseMatchRule r = (BaseMatchRule) where.getMatchRules().get(0);
			Assert.assertEquals("emp", r.getTableAlias());
			Assert.assertEquals("ID", r.getColumnName());
			Assert.assertEquals("=", r.getOperator());
			Assert.assertEquals(":arg_id", r.getValue());
		}{
			JunctionMatchRule amr = (JunctionMatchRule)where.getMatchRules().get(1);
			Assert.assertEquals(JunctionModel.OR, amr.getModel());
			Assert.assertEquals(2, amr.getMatchRules().size());
			
			{
				BaseMatchRule r = (BaseMatchRule) amr.getMatchRules().get(0);
				Assert.assertEquals("emp", r.getTableAlias());
				Assert.assertEquals("LAST_NAME", r.getColumnName());
				Assert.assertEquals("like%", r.getOperator());
				Assert.assertEquals(":arg_last", r.getValue());
			}{
				BaseMatchRule r = (BaseMatchRule) amr.getMatchRules().get(1);
				Assert.assertEquals("emp", r.getTableAlias());
				Assert.assertEquals("FIRST_NAME", r.getColumnName());
				Assert.assertEquals("%like%", r.getOperator());
				Assert.assertEquals(":arg_first", r.getValue());
			}
		}
		
		//--
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
			Assert.assertEquals(9, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Page page = new Page(2,3);
			provider.getResult(page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(9, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_id", 1);
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(1, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_id", 1);
			Page page = new Page(2,3);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(0, rs.size());
			Assert.assertEquals(1, page.getEntityCount());
		}

		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(1, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			Page page = new Page(2,3);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(0, rs.size());
			Assert.assertEquals(1, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_last", "D");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(2, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_last", "D");
			Page page = new Page(2,1);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(2, page.getEntityCount());
		}
		
		{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			param.put("arg_last", "D");
			EntityList<Record> rs = ((EntityList<Record>)provider.getResult(param));
			Assert.assertEquals(2, rs.size());
		}{
			JdbcDataProvider provider = JdbcTestUtils.getDataProvider("jdbc.provider_at6");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("arg_first", "N");
			param.put("arg_last", "D");
			Page page = new Page(2,1);
			provider.getResult(param,page);
			EntityList<Record> rs = (EntityList<Record>)page.getEntities();
			
			Assert.assertEquals(2, rs.size());
			Assert.assertEquals(2, page.getEntityCount());
		}
	}
}
