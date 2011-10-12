package com.bstek.dorado.jdbc.key;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
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
		this.setName("IDENTITY");
	}

	public boolean isIdentity() {
		return true;
	}

	@Override
	public Number newKey(JdbcDataResolverContext context, TableKeyColumn keyColumn,
			Record record) {
		throw new UnsupportedOperationException();
	}

}
