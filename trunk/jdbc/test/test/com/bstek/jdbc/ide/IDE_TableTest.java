package test.com.bstek.jdbc.ide;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ide.CreateSqlTableResolver;
import com.bstek.dorado.jdbc.ide.CreateTableResolver;
import com.bstek.dorado.jdbc.ide.ListCatalogResolver;
import com.bstek.dorado.jdbc.ide.ListJdbcEnviromentResolver;
import com.bstek.dorado.jdbc.ide.ListSchemaResolver;
import com.bstek.dorado.jdbc.ide.ListTableTypeResolver;
import com.bstek.dorado.jdbc.ide.ListTablesResolver;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public class IDE_TableTest extends ConfigManagerTestSupport {
	public IDE_TableTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test_listEnvs() throws Exception {
		Context ctx = Context.getCurrent();
		ListJdbcEnviromentResolver resolver = (ListJdbcEnviromentResolver)ctx.getServiceBean("jdbc.ide.listEnviromentResolver");
		String c = resolver.toContent();
		
		System.out.println("Envs: " + c);
	}
	
	public void test_listCatalogs() throws Exception {
		Context ctx = Context.getCurrent();
		ListCatalogResolver resolver = (ListCatalogResolver)ctx.getServiceBean("jdbc.ide.listCatalogResolver");
		String c = resolver.toContent(null);
		System.out.println("CATs: " + c);	
	}
	
	public void test_listSchemas() throws Exception {
		Context ctx = Context.getCurrent();
		ListSchemaResolver resolver = (ListSchemaResolver)ctx.getServiceBean("jdbc.ide.listSchemaResolver");
		String c = resolver.toContent(null, null);
		System.out.println("SCHs: " + c);		
	}
	
	public void test_listTables() throws Exception {
		Context ctx = Context.getCurrent();
		ListTablesResolver resolver = (ListTablesResolver)ctx.getServiceBean("jdbc.ide.listTablesResolver");
		String envName = null;
		String catalog = null;
		String schema  = "PUBLIC";
		String[] tableTypes = null;
		String tableNamePattern = null;
		String c = resolver.toContent(envName, catalog, schema, tableTypes, tableNamePattern);
		System.out.println("TABs: " +c);
	}
	
	public void test_listTableTypes() throws Exception {
		Context ctx = Context.getCurrent();
		ListTableTypeResolver resolver = (ListTableTypeResolver)ctx.getServiceBean("jdbc.ide.listTableTypeResolver");
		String envName = null;
		String c = resolver.toContent(envName);
		System.out.println("TTYs: " + c);
	}
	
	public void test_createTable() throws Exception {
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
	
	public void test_createSqlTable() throws Exception {
		Context ctx = Context.getCurrent();
		CreateSqlTableResolver resolver = (CreateSqlTableResolver)ctx.getServiceBean("jdbc.ide.createSqlTableResolver");
		
		String envName = null;
		String sql = "select * from EMPLOYEES";
		String xml = resolver.toContent(envName, sql);
		
		System.out.println("XML: " + xml);
	}
}
