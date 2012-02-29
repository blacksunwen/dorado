package com.bstek.dorado.jdbc;

import java.util.Map;

import com.bstek.dorado.data.variant.Record;

public class JdbcRecordOperationProxy {
	
	private Record record;

	private JdbcRecordOperation proxyOperation;
	
	private Map<String, String> proxyPropertyMap;
	
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public JdbcRecordOperation getProxyOperation() {
		return proxyOperation;
	}

	public void setProxyOperation(JdbcRecordOperation proxyOperation) {
		this.proxyOperation = proxyOperation;
	}

	public Map<String, String> getProxyPropertyMap() {
		return proxyPropertyMap;
	}

	public void setProxyPropertyMap(Map<String, String> proxyPropertyMap) {
		this.proxyPropertyMap = proxyPropertyMap;
	}
	
}
