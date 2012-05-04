package com.bstek.dorado.jdbc.feature.abitg;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.h2.api.Trigger;

public class BeforeInsertTrigger implements Trigger {

	public void close() throws SQLException {

	}

	public void fire(Connection conn, Object[] oldRaw, Object[] newRaw)
			throws SQLException {

		System.out.println(">> oldRaw: " + (oldRaw == null ? "<null>": "[" + StringUtils.join(oldRaw, ',') + "]"));
		System.out.println(">> newRaw: " + (newRaw == null ? "<null>": "[" + StringUtils.join(newRaw, ',') + "]"));
		
		newRaw[2] = newRaw[1] + "_2";
	}

	public void init(Connection conn, String schemaName, String triggerName, 
			String tableName, boolean before, int type) throws SQLException {

	}

	public void remove() throws SQLException {

	}

}
