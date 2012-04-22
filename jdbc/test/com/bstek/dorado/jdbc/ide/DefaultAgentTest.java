package com.bstek.dorado.jdbc.ide;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import javax.sql.DataSource;

import junit.framework.TestCase;

public class DefaultAgentTest extends TestCase {

	private String url = "jdbc:h2:zip:~/test.zip!/test";
	private String user = "sa";
	private String password = "dorado";
	
	private DataSource dataSource = null;
	private DataSource getDataSource() {
		if (dataSource == null) {
			org.h2.jdbcx.JdbcDataSource dataSource = new org.h2.jdbcx.JdbcDataSource();
			dataSource.setURL(url);
			dataSource.setUser(user);
//			dataSource.setPassword(password);
			this.dataSource = dataSource;
		}
		return dataSource;
	}

	public void testH2_listTables() throws Exception {
		DataSource dataSource = getDataSource();
		Connection conn = dataSource.getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		String catalog = null;
		String schema = null;
		{
			System.out.println("** " + catalog +", " + schema);
			ResultSet rs = dbmd.getTables(null, null, "%", null);
			
			boolean hasPublic = false;
			while (rs.next()) {
				String tn = rs.getString("TABLE_NAME");
				String tc = rs.getString("TABLE_CAT");
				String ts = rs.getString("TABLE_SCHEM");
				
				if ("PUBLIC".equals(ts)) {
					hasPublic = true;
				}
				
				System.out.println("TABLE_NAME=" + tn + ";TABLE_CAT=" + tc + ";TABLE_SCHEM=" + ts);
			}
			
			assertTrue(hasPublic);
		}
		
		{
			catalog = null;
			schema = "PUBLIC";
			
			System.out.println("** " + catalog +", " + schema);
			ResultSet rs = dbmd.getTables(null, "PUBLIC", "%", null);
			
			boolean hasPublic = false;
			while (rs.next()) {
				String tn = rs.getString("TABLE_NAME");
				String tc = rs.getString("TABLE_CAT");
				String ts = rs.getString("TABLE_SCHEM");
				
				if ("PUBLIC".equals(ts)) {
					hasPublic = true;
				}
				
				System.out.println("TABLE_NAME=" + tn + ";TABLE_CAT=" + tc + ";TABLE_SCHEM=" + ts);
			}
			
			assertTrue(hasPublic);
		}
	}
}
