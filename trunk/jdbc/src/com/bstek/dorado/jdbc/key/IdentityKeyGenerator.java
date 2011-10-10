package com.bstek.dorado.jdbc.key;

import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

public class IdentityKeyGenerator implements KeyGenerator<Number> {

	public String getName() {
		return "IDENTITY";
	}
	
	public boolean isIdentity() {
		return true;
	}

	@Override
	public Number newKey(JdbcQueryContext context, TableKeyColumn keyColumn) {
		throw new UnsupportedOperationException();
	}

}
