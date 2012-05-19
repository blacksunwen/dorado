package com.bstek.dorado.jdbc.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

import org.h2.tools.RunScript;
import org.h2.util.IOUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class H2DataSource extends org.h2.jdbcx.JdbcDataSource implements InitializingBean, DisposableBean {

	private static final long serialVersionUID = 268962357904300911L;

	public H2DataSource() {
		super();
		String dbName = UUID.randomUUID().toString();
		this.setURL("jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");
	}

	public void afterPropertiesSet() throws Exception {
		Class<?> testClazz = TestJdbcUtils.getCurrentTestClass();
		InputStream in = testClazz.getResourceAsStream(testClazz.getSimpleName()+".sql");
		
		if (in != null) {
			Reader reader = new InputStreamReader(in, "UTF-8");
			try {
				RunScript.execute(getConnection(), reader);
			} finally {
				IOUtils.closeSilently(in);
				IOUtils.closeSilently(reader);
			}
		}
	}

	public void destroy() throws Exception {
		this.getConnection().createStatement().execute("SHUTDOWN");
	}
}
