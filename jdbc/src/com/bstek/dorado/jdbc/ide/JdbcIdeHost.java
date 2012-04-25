package com.bstek.dorado.jdbc.ide;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.bstek.dorado.util.Assert;

public class JdbcIdeHost {

	public static void main(String[] args) throws Exception {
		for (int i=0; i < args.length; i++) {
			System.out.println("arg" + i + "=" + args[i]);
		}
		
		Service service = new Service(args);
		FileWriter fileWriter = new FileWriter(service.resultFile);
		
		try {
			Map<String, String> argMap = service.argMap;
			
			Document document = service.doService(argMap);
			String result = service.toString(document);
			System.out.println("** [Result] " + result);
			
			fileWriter.write(result);
		} catch (Exception t) {
			fileWriter.write("-ERROR-\n");
			t.printStackTrace(new PrintWriter(fileWriter));
			
			throw t;
		} finally {
			service.dataSource.destroy();
			fileWriter.close();
		}
	}

	static class Service {
		private String serviceMethodName;
		private File resultFile;
		private DriverDataSource dataSource;
		private Map<String, String> argMap = new HashMap<String, String>();
		private Class<?> agentClass;
		private Object agentObject;
		
		Service(String [] args) throws Exception {
			Map<String, String> argMap = new HashMap<String, String>();
			for (int i=0; i<args.length; i++) {
				String key = args[i];
				if (key.startsWith("-")) {
					key = key.substring(1);
					String value = args[i+1];
					if (!value.startsWith("-")) {
						argMap.put(key, value);
						i++;
					}
				}
			}
			
			String agentClassName = argMap.remove("jdbcAgent");
			serviceMethodName = argMap.remove("service");
			
			Assert.notEmpty(agentClassName, "[jdbcAgent] must not be null");
			Assert.notEmpty(serviceMethodName, "[service] must not be null");
			
			String resultFilePath = argMap.remove("file");
			String driver = argMap.remove("driver");
			String url = argMap.remove("url");
			String user = argMap.remove("user");
			String password = argMap.remove("password");
			
			this.argMap.putAll(argMap);
			
			resultFile = new File(resultFilePath);
			dataSource = new DriverDataSource(driver, url, user, password);
			
			agentClass = Class.forName(agentClassName);
			agentObject = agentClass.newInstance();
		}
		
		Document doService(Map<String, String> parameter) throws Exception {
			Method serviceMethod = agentClass.getMethod(serviceMethodName, Map.class, DataSource.class);
			Document document = (Document)serviceMethod.invoke(agentObject, parameter, dataSource);
			return document;
		}
		
		String toString(Document document) throws Exception {
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
	
	static class DriverDataSource implements DataSource {
		private String url;
		private String user;
		private String password;
		private PrintWriter logWriter;
		private Set<Connection> connSet = new HashSet<Connection>();
		private final Object connectionMonitor = new Object();
		
		public DriverDataSource(String driverClassName, String url, String user, String password) throws Exception {
			if (driverClassName != null && driverClassName.length() > 0) {
				Class.forName(driverClassName);
			}
			
			this.url = url;
			this.user = user;
			this.password = password;
		}

		public PrintWriter getLogWriter() throws SQLException {
			return logWriter;
		}

		public void setLogWriter(PrintWriter out) throws SQLException {
			logWriter = out;
		}

		public void setLoginTimeout(int seconds) throws SQLException {
		}

		public int getLoginTimeout() throws SQLException {
			return 0;
		}


		public Connection getConnection() throws SQLException {
			synchronized (connectionMonitor) {
				Connection conn = DriverManager.getConnection(url, user, password);
				try {
					return conn;
				} finally {
					connSet.add(conn);
				}
			}
		}

		public Connection getConnection(String username, String password)
				throws SQLException {
			return getConnection();
		}
		
		public void destroy() {
			synchronized (connectionMonitor) {
				for (Connection conn: connSet) {
					close(conn);
				}
				connSet.clear();
			}
		}
		
		private void close(Connection conn) {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}
	}
}
