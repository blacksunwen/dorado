package com.bstek.dorado.jdbc.ide;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.bstek.dorado.jdbc.model.sqltable.SqlTable;
import com.bstek.dorado.jdbc.model.table.Table;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractAgent implements IAgent {

	private Map<String, String> paramerters;
	private DatabaseMetaData databaseMetaData;
	private SingleConnectionDataSource dataSource;
	private Driver driver;

	@Override
	public String listTables(Map<String, String> paramerters)
			throws Exception {
		this.resetContext(paramerters);
		try {
			String tables = doListTables();
			System.out.println("*> " + tables);
			return tables;
		} finally {
			this.clearContext();
		}
	}
	
	protected abstract String doListTables() throws Exception;


	@Override
	public String createColumns(Map<String, String> paramerters)
			throws Exception {
		this.resetContext(paramerters);
		try {
			String tableType = paramerters.get(IAgent.TABLE_TYPE);
			Document document = parseText(paramerters.get(IAgent.XML));
			if (Table.TYPE.equals(tableType)) {
				document = createTableColumns(document);
			} else if (SqlTable.TYPE.equals(tableType)) {
				document = createSqlTableColumns(document);
			}
			
			String xml = toString(document);
			System.out.println("*> " + xml);
			return xml;
		} finally {
			this.clearContext();
		}
	}
	
	protected abstract Document createTableColumns(Document document) throws Exception;
	protected abstract Document createSqlTableColumns(Document document) throws Exception;
	
	protected DatabaseMetaData getDatabaseMetaData() throws SQLException {
		if (databaseMetaData == null) {
			databaseMetaData = this.makeDatabaseMetaData(paramerters);
		}
		return databaseMetaData;
	}
	protected DatabaseMetaData makeDatabaseMetaData(Map<String, String> paramerters) throws SQLException {
		DataSource dataSource = getDataSource();
		Connection conn = dataSource.getConnection();
		return conn.getMetaData();
	}
	
	protected SingleConnectionDataSource getDataSource() {
		if (dataSource == null) {
			dataSource = this.makeDataSource(paramerters);
		}
		return dataSource;
	}
	protected SingleConnectionDataSource makeDataSource(Map<String, String> paramerters) {
		String url = paramerters.get(IAgent.URL);
		String user = paramerters.get(IAgent.USER);
		String password = paramerters.get(IAgent.PASSWORD);
		String driverClassName = paramerters.get(IAgent.DRIVER);
		
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource(url, user, password, false);
		dataSource.setDriverClassName(driverClassName);
		return dataSource;
	}
	
	@SuppressWarnings("unchecked")
	protected void resetContext(Map<String, String> paramerters) throws Exception {
		clearContext();
		
		this.paramerters = paramerters;
		System.out.println("*> [CONTEXT]parameters: " + this.paramerters);
		
		this.getDataSource();
		
		String driverClassName = paramerters.get(IAgent.DRIVER);
		Class<?> driverClass = Class.forName(driverClassName, true, ClassUtils.getDefaultClassLoader());

		Enumeration<Driver> driverEnu = DriverManager.getDrivers();
		Driver foundDriver = null;
		while (driverEnu.hasMoreElements()) {
			Driver driver = driverEnu.nextElement();
			if(driver.getClass().getName().equals(driverClassName)) {
				foundDriver = driver;
				break;
			}
		}
		
		if (foundDriver != null) {
			this.driver = foundDriver;
			System.out.println("*> [DRIVER]found driver: " + driver);
		} else {
			this.driver = BeanUtils.instantiateClass((Class<Driver>)driverClass);
			try {
				DriverManager.registerDriver(driver);
				System.out.println("*> [DRIVER]register driver: " + driver);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void clearContext() throws Exception {
		if (driver != null) {
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println("*> [DRIVER]deregister driver: " + driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.driver = null;
		}
		
		if (this.dataSource != null) {
			this.dataSource.resetConnection();
			this.dataSource = null;
		}
		this.paramerters = null;
		this.databaseMetaData = null;
	}
	protected Map<String, String> getParamerters() {
		if (paramerters == null) {
			throw new IllegalArgumentException("parameters is null");
		}
		
		return paramerters;
	}
	
	protected static Document newDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		return factory.newDocumentBuilder().newDocument();
	}
	
	protected static Document parseText(String text) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(text)));
	}
	
	protected static String toString(Document document) throws Exception {
		StringWriter writer = new StringWriter();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");  
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return writer.toString();
	}
	
	protected static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
