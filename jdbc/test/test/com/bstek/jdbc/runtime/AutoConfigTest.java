package test.com.bstek.jdbc.runtime;

import org.junit.Assert;

import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityList;
import com.bstek.dorado.jdbc.JdbcDataProvider;

public class AutoConfigTest extends ConfigManagerTestSupport {

	public AutoConfigTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test() throws Exception {
		JdbcDataProvider provider = JdbcTestUtils.getDataProvider("EMP_AUTO");
		Assert.assertNotNull(provider);
		
		EntityList list = (EntityList)provider.getResult();
		
		System.out.println(list.size() > 0);
		
	}
}
