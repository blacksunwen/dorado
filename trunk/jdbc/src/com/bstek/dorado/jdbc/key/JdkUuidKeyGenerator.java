package com.bstek.dorado.jdbc.key;

import java.util.UUID;

import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

public class JdkUuidKeyGenerator implements KeyGenerator<String> {

	private String name = "JDK-UUID";
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public boolean isIdentity() {
		return false;
	}

	@Override
	public String newKey(JdbcQueryContext context, TableKeyColumn keyColumn) {
		UUID id = UUID.randomUUID();
		return id.toString();
	}

}
