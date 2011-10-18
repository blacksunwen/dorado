package com.bstek.dorado.jdbc.key;

import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

/**
 * 主键生成器
 * 
 * @author mark
 * 
 * @param <T>
 */
public interface KeyGenerator<T> {

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 是否是数据库自增主键
	 * 
	 * @return
	 */
	boolean isIdentity();

	/**
	 * 产生一个新的主键值
	 * 
	 * @param context
	 * @param keyColumn
	 * @param record
	 * @return
	 */
	T newKey(JdbcRecordOperation operation, TableKeyColumn keyColumn);
}
