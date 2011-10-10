package test.com.bstek.jdbc;

import junit.framework.Assert;

import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.jdbc.JdbcDataProvider;
import com.bstek.dorado.jdbc.config.JdbcConfigManager;
import com.bstek.dorado.jdbc.config.xml.ColumnParser;
import com.bstek.dorado.jdbc.manager.JdbcModelManager;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.table.Table;

public class JdbcTestUtils {

	public static ColumnParser columnParser() throws Exception  {
		Context context = Context.getCurrent();
		ColumnParser parser = (ColumnParser)context.getServiceBean("jdbc.config.xml.columnParser");
		return parser;
	}
	
	public static Document newDocument() throws Exception {
		Context context = Context.getCurrent();
		XmlDocumentBuilder builder = (XmlDocumentBuilder)context.getServiceBean("xmlDocumentBuilder");
		Document document = builder.newDocument();
		return document;
	}

	public static JdbcModelManager jdbcModelManager() throws Exception {
		Context context = Context.getCurrent();
		JdbcModelManager parser = (JdbcModelManager)context.getServiceBean("jdbc.jdbcModelManager");
		return parser;
	}
	
	public static ObjectParser tableParser() throws Exception {
		JdbcModelManager modelManager = jdbcModelManager();
		return modelManager.getParser(DbElement.Type.Table);
	}
	
	public static ObjectParser sqlTableParser() throws Exception {
		JdbcModelManager modelManager = jdbcModelManager();
		return modelManager.getParser(DbElement.Type.SqlTable);
	}
	
	public static ObjectParser autoTableParser() throws Exception {
		JdbcModelManager modelManager = jdbcModelManager();
		return modelManager.getParser(DbElement.Type.AutoTable);
	}
	
	public static JdbcConfigManager configManager() throws Exception {
		Context context = Context.getCurrent();
		JdbcConfigManager configManager = (JdbcConfigManager)context.getServiceBean("jdbc.config.jdbcConfigManager");
		return configManager;
	}
	
	public static Table table(String name) throws Exception {
		JdbcModelManager modelMananger = jdbcModelManager();
		return (Table)modelMananger.getDbElement(name);
	}
	
	public static AutoTable autoTable(String name) throws Exception {
		JdbcModelManager modelMananger = jdbcModelManager();
		return (AutoTable)modelMananger.getDbElement(name);
	}
	
	public static JdbcDataProvider getDataProvider(String id) throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		JdbcDataProvider provider = (JdbcDataProvider) dataProviderManager.getDataProvider(id);
		Assert.assertNotNull("no provider named '" + id + "'.", provider);
		return provider;
	}
}
