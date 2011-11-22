package test.com.bstek.jdbc;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.core.Constants;
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

	public static String toString(org.dom4j.Document document) {
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Constants.DEFAULT_CHARSET);

		XMLWriter xmlWriter = new XMLWriter(writer, format);
		try {
			xmlWriter.write(document);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
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
		return getJdbcConfigManager().getParser("Table");
	}
	
	public static ObjectParser sqlTableParser() throws Exception {
		return getJdbcConfigManager().getParser("SqlTable");
	}
	
	public static ObjectParser autoTableParser() throws Exception {
		return getJdbcConfigManager().getParser("AutoTable");
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
		@SuppressWarnings("deprecation")
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
		
		public static Record radomEmpSql(int id) {
			Record r = new Record();
			
			r.set("id", id);
			r.set("lastName", RandomStringUtils.randomAscii(10));
			r.set("firstName", RandomStringUtils.randomAscii(10));
			r.set("title", RandomStringUtils.randomAscii(20));
			r.set("titleOfCourtesy", RandomStringUtils.randomAscii(20));
			r.set("sex", Short.valueOf("1"));
			r.set("birthDate", new Date(1980, 2, 21));
			r.set("hireDate", new Date(2010, 12, 30));
			
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
		
		public static Record radomProduct(int id, String catgId) {
			Record r = new Record();
			
			r.set("ID", id);
			r.set("PRODUCT_NAME", RandomStringUtils.random(10, new char[]{'A','B','C','D'}));
			r.set("SUPPLIER_ID", Integer.valueOf(3));
			r.set("QUANTITY_PER_UNIT", RandomStringUtils.random(3, new char[]{'A','B','C','D'}));
			r.set("UNIT_PRICE", new BigDecimal(10));
			r.set("UNITS_IN_STOCK", Integer.valueOf(RandomStringUtils.randomNumeric(2)));
			r.set("UNITS_ON_ORDER", Integer.valueOf(RandomStringUtils.randomNumeric(2)));
			r.set("REORDER_LEVEL", Integer.valueOf(RandomStringUtils.randomNumeric(2)));
			r.set("DISCONTINUED", Short.valueOf("3"));
			if (catgId != null) {
				r.set("CATEGORY_ID", catgId);
			}
			
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
		public static Record radomAutoT3(int id) {
			Record r = new Record();
			r.set("p_pid", id);
			r.set("p_pname", RandomStringUtils.random(10, new char[]{'A','B','C','D','X','Y','Z'}));
			r.set("p_psupplier", 3);
			r.set("p_pcategory", 4);
			r.set("p_pquanitity", RandomStringUtils.random(2, new char[]{'A','B','C','D','X','Y','Z'}));
			r.set("p_punitprice", 4);
			r.set("p_ccategory", RandomStringUtils.random(10, new char[]{'A','B','C','D','X','Y','Z'}));
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
			setState(records, state);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void setState(Collection records, EntityState state) {
		for (Object item: records) {
			Record r = (Record)item;
			r.getEntityEnhancer().setState(state);
		}
	}
}
