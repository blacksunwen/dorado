package com.bstek.dorado.jdbc.ide;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.bstek.dorado.jdbc.model.SqlTable;
import com.bstek.dorado.jdbc.model.Table;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractAgent implements IAgent {

	private Map<String, Object> parameters;
	private DatabaseMetaData databaseMetaData;
	private DataSource dataSource;

	public Document listTables(Map<String, Object> parameters, DataSource dataSource)
			throws Exception {
		this.resetContext(parameters, dataSource);
		try {
			Document document = doListTables();
			return document;
		} finally {
			this.clearContext();
		}
	}
	
	protected abstract Document doListTables() throws Exception;

	public Document createColumns(Map<String, Object> parameters, DataSource dataSource)
			throws Exception {
		this.resetContext(parameters, dataSource);
		try {
			String tableType = (String)parameters.get(IAgent.TABLE_TYPE);
			Document document = parseText((String)parameters.get(IAgent.XML));
			if (Table.TYPE.equals(tableType)) {
				document = createTableColumns(document);
			} else if (SqlTable.TYPE.equals(tableType)) {
				document = createSqlTableColumns(document);
			}
			
			return document;
		} finally {
			this.clearContext();
		}
	}
	
	protected abstract Document createTableColumns(Document document) throws Exception;
	protected abstract Document createSqlTableColumns(Document document) throws Exception;
	
	protected DatabaseMetaData getDatabaseMetaData() throws SQLException {
		return databaseMetaData;
	}
	
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	private void resetContext(Map<String, Object> parameters, DataSource dataSource) throws Exception {
		System.out.println("** [CONTEXT]parameters: " + parameters);
		this.parameters = parameters;
		
		this.dataSource = dataSource;
		this.databaseMetaData = this.getDataSource().getConnection().getMetaData();
	}
	
	protected void clearContext() throws Exception {
		this.parameters = null;
		this.databaseMetaData = null;
		this.dataSource = null;
	}
	
	protected Map<String, Object> getParameters() {
		return parameters;
	}
	
	protected Document newDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		return factory.newDocumentBuilder().newDocument();
	}
	
	protected Document parseText(String text) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(text)));
	}
	
	protected void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected interface ResultSetCallback<T> {
		T call(ResultSet rs) throws Exception;
	}
	
	protected <T>T doCall(ResultSet rs, ResultSetCallback<T> callback) throws Exception {
		try {
			return callback.call(rs);
		} finally {
			close(rs);
		}
	}
	
	protected <T>T doCall(String querySql, ResultSetCallback<T> callback) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(querySql);
			
			return callback.call(rs);
		} finally {
			close(rs);
			close(stmt);
			close(conn);
		}
	}
}
