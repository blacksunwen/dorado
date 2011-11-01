package test.com.bstek.jdbc.ide;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ide.CreateTableResolver;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public class IDE_TableTest extends ConfigManagerTestSupport {
	public IDE_TableTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test1() throws Exception {
		Context ctx = Context.getCurrent();
		CreateTableResolver resolver = (CreateTableResolver)ctx.getServiceBean("jdbc.ide.createTableResolver");
		
		String catalog = null;
		String schema = null;
		String table = "EMPLOYEES";
		
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getDefault();
		
		TableGeneratorOption option = new TableGeneratorOption();
		option.setJdbcEnviroment(jdbcEnv);
		option.setGenerateCatalog(false);
		option.setGenerateSchema(false);
		
		String xml = resolver.toContent(catalog, schema, table, option);
		
		System.out.println("XML: " + xml);
	}
}
