package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * 自增主键生成器
 * 
 * @author mark.li@bstek.com
 * 
 */
public class IdentityKeyGenerator extends AbstractKeyGenerator<Number> {

	public IdentityKeyGenerator() {
		super();
		this.setName(JdbcConstants.IDENTITY);
	}

	public boolean isIdentity() {
		return true;
	}

	public Number newKey(JdbcRecordOperation operation, TableKeyColumn keyColumn) {
		throw new UnsupportedOperationException();
	}

}
