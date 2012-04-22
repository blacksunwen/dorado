package com.bstek.dorado.jdbc.model.table;

import java.util.UUID;

import com.bstek.dorado.jdbc.JdbcRecordOperation;

/**
 * UUID的主键生成器
 * @see java.util.UUID
 * @author mark.li@bstek.com
 *
 */
public class JdkUuidKeyGenerator extends AbstractKeyGenerator<String> {

	public JdkUuidKeyGenerator() {
		super();
		this.setName("JDK-UUID");
	}

	public String newKey(JdbcRecordOperation operation, TableKeyColumn keyColumn) {
		UUID id = UUID.randomUUID();
		return id.toString();
	}

}
