package com.bstek.dorado.jdbc.key;

import java.util.UUID;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

/**
 * UUID的主键生成器
 * @see java.util.UUID
 * @author mark
 *
 */
public class JdkUuidKeyGenerator extends AbstractKeyGenerator<String> {

	public JdkUuidKeyGenerator() {
		super();
		this.setName("JDK-UUID");
	}

	@Override
	public String newKey(JdbcDataResolverContext context, TableKeyColumn keyColumn, Record record) {
		UUID id = UUID.randomUUID();
		return id.toString();
	}

}
