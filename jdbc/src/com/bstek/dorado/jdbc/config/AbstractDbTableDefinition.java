package com.bstek.dorado.jdbc.config;


public abstract class AbstractDbTableDefinition extends DbElementDefinition {
	private Boolean autoCreateDataType = null;
	private Boolean autoCreateDataProvider = null;

	public boolean isAutoCreateDataProvider() {
		if (autoCreateDataProvider == null) {
			autoCreateDataProvider = this.getFinalPropertyBoolean(XmlConstants.AUTO_CREATE_DATAPROVIDER, false);
		}
		return autoCreateDataProvider;
	}

	public void setAutoCreateDataProvider(boolean autoCreateDataProvider) {
		this.autoCreateDataProvider = autoCreateDataProvider;
	}

	public boolean isAutoCreateDataType() {
		if (autoCreateDataType == null) {
			autoCreateDataType = this.getFinalPropertyBoolean(XmlConstants.AUTO_CREATE_DATATYPE, false);
		}
		return autoCreateDataType;
	}

	public void setAutoCreateDataType(boolean autoCreateDataType) {
		this.autoCreateDataType = autoCreateDataType;
	}
}
