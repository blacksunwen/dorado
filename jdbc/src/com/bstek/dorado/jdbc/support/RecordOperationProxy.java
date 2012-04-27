package com.bstek.dorado.jdbc.support;

import java.util.Map;

import com.bstek.dorado.data.variant.Record;

public class RecordOperationProxy {
	
	private Record record;

	private TableRecordOperation proxyOperation;
	
	private Map<String, String> proxyPropertyMap;
	
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public TableRecordOperation getProxyOperation() {
		return proxyOperation;
	}

	public void setProxyOperation(TableRecordOperation proxyOperation) {
		this.proxyOperation = proxyOperation;
	}

	public Map<String, String> getProxyPropertyMap() {
		return proxyPropertyMap;
	}

	public void setProxyPropertyMap(Map<String, String> proxyPropertyMap) {
		this.proxyPropertyMap = proxyPropertyMap;
	}
	
}
