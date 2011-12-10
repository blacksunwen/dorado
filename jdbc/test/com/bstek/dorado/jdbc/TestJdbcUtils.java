package com.bstek.dorado.jdbc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.ide.CreateTableResolver;
import com.bstek.dorado.jdbc.support.DefaultDataTypeMetaGenerator;
import com.bstek.dorado.jdbc.support.DefaultStoredProcedureGenerator;

public class TestJdbcUtils {

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
	
	/**
	 * 输出一个表的xml
	 * @param jdbcEnvName
	 * @param tableName
	 * @return
	 */
	public static String outputTable(String jdbcEnvName, String tableName) {
		return outputTable(jdbcEnvName, null, null, tableName);
	}
	
	/**
	 * 输出一个表的xml
	 * @param jdbcEnvName
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @return
	 */
	public static String outputTable(String jdbcEnvName, String catalog, String schema, String tableName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(jdbcEnvName);
		
		CreateTableResolver resolver = new CreateTableResolver();
		
		String xml = resolver.toContent(catalog, schema, tableName, jdbcEnv);
		return xml;
	}

	public static String outputSP(String jdbcEnvName, String catalog, String schema, String procedureName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(jdbcEnvName);
		DefaultStoredProcedureGenerator generator = new DefaultStoredProcedureGenerator();
		Document document = generator.createDocument(jdbcEnv, catalog, schema, procedureName);
		
		String xml = TestJdbcUtils.toString(document);
		return xml;
	}
	
	public static String outputDataType(String jdbcEnvName, String tableName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(jdbcEnvName);
		DefaultDataTypeMetaGenerator generator = new DefaultDataTypeMetaGenerator();
		Document document = generator.createDocument(jdbcEnv, tableName);
		
		String xml = TestJdbcUtils.toString(document);
		return xml;
	}
	
	public static boolean assertEquals(Record r1, Record r2) {
		Assert.assertEquals(r1.size(), r2.size());
		
		Set<String> keySet1 = r1.keySet();
		Set<String> keySet2 = r2.keySet();
		Assert.assertEquals(keySet1, keySet2);
		
		Iterator<String> keyItr1 = keySet1.iterator();
		while (keyItr1.hasNext()) {
			String key = keyItr1.next();
			Object value1 = r1.get(key);
			Object value2 = r2.get(key);
			
			Assert.assertEquals("key=" + key, value1, value2);
		}
		
		return true;
	}
}
