package test.com.bstek.jdbc.runtime;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProvider;

public class ProvierContextTestCase extends ConfigManagerTestSupport {

	public ProvierContextTestCase() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	protected JdbcDataProvider getDataProvider(String id) throws Exception {
		JdbcDataProvider provider = (JdbcDataProvider) getDataProviderManager().getDataProvider(id);
		Assert.assertNotNull("no provider named '" + id + "'.", provider);
		return provider;
	}
	
	@SuppressWarnings("unchecked")
	public void test_0() throws Exception {
		JdbcDataProvider provider = getDataProvider("jdbc.provider_0");
		
		EntityList<Record> rs = ((EntityList<Record>)provider.getResult());
		Assert.assertEquals(9, rs.size());
		
		System.out.println(rs);
	}
	
	@SuppressWarnings({"unchecked" })
	public void test_1() throws Exception {
		JdbcDataProvider provider = getDataProvider("jdbc.provider_1");
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("ID", 1);
		
		EntityList<Record> list = (EntityList<Record>)provider.getResult(parameter);
		Assert.assertEquals(1, list.size());
		
		Page<Record> page = new Page<Record>(2, 3);
		provider.getResult(parameter, page);
//		Assert.assertEquals(0, page.getEntities().size());
//		Assert.assertEquals(1, page.getEntityCount());
		
	}
	
	public void test_st1() throws Exception {
		{
			JdbcDataProvider provider = getDataProvider("jdbc.provider_st1");
			EntityList<Record> rs = (EntityList<Record>)provider.getResult();
			Assert.assertEquals(9, rs.size());
		}{
			JdbcDataProvider provider = getDataProvider("jdbc.provider_st1");
			Page page = new Page(2,3);
			provider.getResult(page);
			
			Assert.assertEquals(2, page.getEntities().size());
			Assert.assertEquals(9, page.getEntityCount());
		}
		
	}
}
