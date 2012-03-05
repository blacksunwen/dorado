package com.bstek.dorado.jdbc.ide;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.bstek.dorado.jdbc.model.sqltable.SqlTable;
import com.bstek.dorado.jdbc.model.table.Table;

public abstract class AbstractAgent implements IAgent {

	private Map<String, String> paramerters;
	private DatabaseMetaData databaseMetaData;
	private SingleConnectionDataSource dataSource;
	
	@Override
	public String listSpaces(Map<String, String> paramerters)
			throws Exception {
		this.resetParameters(paramerters);
		try {
			return doListSpaces();
		} finally {
			this.clearParameters();
		}
	}
	
	protected abstract String doListSpaces() throws Exception;

	@Override
	public String listTables(Map<String, String> paramerters)
			throws Exception {
		this.resetParameters(paramerters);
		try {
			return doListTables();
		} finally {
			this.clearParameters();
		}
	}
	
	protected abstract String doListTables() throws Exception;

	@Override
	public String listJdbcTypes(Map<String, String> paramerters)
			throws Exception {
		this.resetParameters(paramerters);
		try {
			String[] typeAry = getJdbcTypeNames();
			return StringUtils.join(typeAry, ",");
		} finally {
			this.clearParameters();
		}
	}
	
	protected abstract String[] getJdbcTypeNames();

	@Override
	public String listKeyGenerators(Map<String, String> paramerters)
			throws Exception {
		this.resetParameters(paramerters);
		try {
			String[] typeAry = getKeyGeneratorNames();
			return StringUtils.join(typeAry, ",");
		} finally {
			this.clearParameters();
		}
	}
	
	protected abstract String[] getKeyGeneratorNames();

	@Override
	public String createColumns(Map<String, String> paramerters)
			throws Exception {
		this.resetParameters(paramerters);
		try {
			String tableType = paramerters.get(IAgent.TABLE_TYPE);
			Document document = parseText(paramerters.get(IAgent.XML));
			if (Table.TYPE.equals(tableType)) {
				document = createTableColumns(document);
			} else if (SqlTable.TYPE.equals(tableType)) {
				document = createSqlTableColumns(document);
			}
			
			return toString(document);
		} finally {
			this.clearParameters();
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
	
	protected void resetParameters(Map<String, String> paramerters) {
		clearParameters();
		this.paramerters = paramerters;
	}
	protected void clearParameters() {
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
	
}
