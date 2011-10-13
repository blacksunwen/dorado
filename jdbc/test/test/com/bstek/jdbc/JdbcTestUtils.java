package test.com.bstek.jdbc;

import java.util.Collection;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
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
	
	public static class Radom {
		public static Record radomEmployee(int id) {
			Record r = new Record();
			
			r.set("p_id", id);
			r.set("p_last_name", RandomStringUtils.randomAscii(10));
			r.set("p_first_name", RandomStringUtils.randomAscii(10));
			r.set("p_title", RandomStringUtils.randomAscii(20));
			r.set("p_title_of_courtesy", RandomStringUtils.randomAscii(20));
			r.set("p_sex", Short.valueOf("1"));
			r.set("p_birth_date", new Date(1980, 2, 21));
			r.set("p_hire_date", new Date(2010, 12, 30));
			r.set("p_address", RandomStringUtils.randomAscii(60));
			r.set("p_city", RandomStringUtils.randomAscii(15));
			r.set("p_region", RandomStringUtils.randomAscii(15));
			r.set("p_postal_code", RandomStringUtils.randomAscii(10));
			r.set("p_country", RandomStringUtils.randomAscii(15));
			r.set("p_phone", RandomStringUtils.randomAlphabetic(20));
			r.set("p_extension", RandomStringUtils.randomAlphabetic(4));
			r.set("p_reports_to", "2");
			r.set("p_notes", RandomStringUtils.randomAscii(100));
			r.set("p_photo_path", RandomStringUtils.randomAscii(100));
			
			try {
				r = (Record)EntityUtils.toEntity(r, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return r;
		}
		
		public static Record radomCategory(int id) {
			Record r = new Record();
			
			r.set("ID", id);
			r.set("CATEGORY_NAME", RandomStringUtils.random(10, new char[]{'A','B','C','D'}));
			r.set("DESCRIPTION", RandomStringUtils.random(50, new char[]{'A','B','C','D'}));
			try {
				r = (Record)EntityUtils.toEntity(r, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return r;
		}
		
		public static Record radomInc() {
			Record r = new Record();
			
			r.set("NAME", RandomStringUtils.random(10, new char[]{'A','B','C','D'}));
			try {
				r = (Record)EntityUtils.toEntity(r, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return r;
		}
		
		public static Record radomUuid() {
			Record r = new Record();
			
			r.set("NAME", RandomStringUtils.random(10, new char[]{'A','B','C','D','X','Y','Z'}));
			try {
				r = (Record)EntityUtils.toEntity(r, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return r;
		}
		
		public static Record radomSequence() {
			Record r = new Record();
			
			r.set("NAME", RandomStringUtils.random(10, new char[]{'A','B','C','D','X','Y','Z'}));
			try {
				r = (Record)EntityUtils.toEntity(r, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return r;
		}
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	public static void setState(DataItems dataItems, String itemName, EntityState state) {
		Object itemValue = dataItems.get(itemName);
		if (itemValue instanceof Record) {
			Record r = (Record)itemValue;
			r.getEntityEnhancer().setState(state);
		} else if (itemValue instanceof Collection) {
			Collection records = (Collection)itemValue;
			for (Object item: records) {
				Record r = (Record)item;
				r.getEntityEnhancer().setState(state);
			}
		}
	}
}
