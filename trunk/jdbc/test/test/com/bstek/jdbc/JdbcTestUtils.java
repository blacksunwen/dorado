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

	public static JdbcConfigManager getJdbcConfigManager() {
		Context context = Context.getCurrent();
		try {
			JdbcConfigManager parser = (JdbcConfigManager)context.getServiceBean("jdbc.jdbcConfigManager");
			return parser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ObjectParser tableParser() throws Exception {
		return getJdbcConfigManager().getParser(DbElement.Type.Table);
	}
	
	public static ObjectParser sqlTableParser() throws Exception {
		return getJdbcConfigManager().getParser(DbElement.Type.SqlTable);
	}
	
	public static ObjectParser autoTableParser() throws Exception {
		return getJdbcConfigManager().getParser(DbElement.Type.AutoTable);
	}
	
	public static JdbcConfigManager configManager() throws Exception {
		Context context = Context.getCurrent();
		JdbcConfigManager configManager = (JdbcConfigManager)context.getServiceBean("jdbc.config.jdbcConfigManager");
		return configManager;
	}
	
	public static Table table(String name) throws Exception {
		return (Table)getJdbcConfigManager().getDbElement(name);
	}
	
	public static AutoTable autoTable(String name) throws Exception {
		return (AutoTable)getJdbcConfigManager().getDbElement(name);
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
