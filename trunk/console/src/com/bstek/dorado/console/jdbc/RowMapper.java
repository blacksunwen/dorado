package com.bstek.dorado.console.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Dorado Console 数据库行解析器接口
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2012-12-13
 */
public interface RowMapper<T> {
	/**
	 * 
	 * @param resultset
	 *            当前resultset
	 * @param rowNum
	 *            行号
	 * @return
	 * @throws SQLException
	 */
	public abstract T mapRow(ResultSet resultset, int rowNum)
			throws SQLException;
}
