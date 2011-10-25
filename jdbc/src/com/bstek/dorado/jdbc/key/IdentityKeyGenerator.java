package com.bstek.dorado.jdbc.key;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

/**
 * 自增主键生成器
 * 
 * @author mark
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

	@Override
	public Number newKey(JdbcRecordOperation operation, TableKeyColumn keyColumn) {
		throw new UnsupportedOperationException();
	}

}
