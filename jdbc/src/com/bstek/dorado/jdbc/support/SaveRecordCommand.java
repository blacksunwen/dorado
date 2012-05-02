package com.bstek.dorado.jdbc.support;

import java.util.Iterator;
import java.util.Map;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.Table;

public class SaveRecordCommand {

	public void execute(SaveRecordOperation operation) throws Exception {
		Record record = operation.getRecord();
		DbTable dbTable = operation.getDbTable();
		DataResolverContext jdbcContext = operation.getJdbcContext();
		
		if (dbTable.supportResolverTable()) {
			Table table = dbTable.getResolverTable();
			if (table != null) {
				RecordOperationProxy proxy = dbTable.createOperationProxy(record, jdbcContext);
				if (proxy != null) {
					TableRecordOperation proxyOperation = proxy.getProxyOperation();
					if (proxyOperation.run()) {
						Map<String, String> propertyMap = proxy.getProxyPropertyMap();
						if (propertyMap != null) {
							Record proxyRecord = proxyOperation.getRecord();
							
							Iterator<String> keyItr = propertyMap.keySet().iterator();
							while (keyItr.hasNext()) {
								String propertyName = keyItr.next();
								String proxyPropertyName = propertyMap.get(propertyName);
								Object proxyValue = proxyRecord.get(proxyPropertyName);
								record.put(propertyName, proxyValue);
							}
						}
					}
				}
			}
		} else {
			TableRecordOperation to = new TableRecordOperation((Table)dbTable, record, jdbcContext);
			to.run();
		}
	}
}
