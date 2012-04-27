package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.support.JdbcConstants;
import com.bstek.dorado.jdbc.support.TableRecordOperation;

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

	public Number newKey(TableRecordOperation operation, TableKeyColumn keyColumn) {
		throw new UnsupportedOperationException();
	}

}
