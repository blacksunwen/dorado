package com.bstek.dorado.jdbc;

/**
 * 操作{@link com.bstek.dorado.jdbc.model.DbTable}的触发器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbTableTrigger {

	/**
	 * 查询动作
	 * @param operation
	 */
	void doQuery(JdbcDataProviderOperation operation) throws Exception;
	
	/**
	 * 更新、新增、删除动作
	 * @param operation
	 */
	void doSave(JdbcRecordOperation operation) throws Exception;
}
