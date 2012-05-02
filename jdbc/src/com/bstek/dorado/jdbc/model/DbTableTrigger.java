package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.SaveOperation;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;

/**
 * 操作{@link com.bstek.dorado.jdbc.model.DbTable}的触发器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbTableTrigger {

	/**
	 * 查询数据的时候触发
	 * @param operation
	 */
	void doQuery(QueryOperation operation) throws Exception;
	
	/**
	 * 保存数据的时候触发
	 * @param operation
	 * @throws Exception
	 */
	void doSave(SaveOperation operation) throws Exception;
	
	/**
	 * 保存每一条记录的时候触发
	 * @param operation
	 * @throws Exception
	 */
	void doSave(SaveRecordOperation operation) throws Exception;

}
