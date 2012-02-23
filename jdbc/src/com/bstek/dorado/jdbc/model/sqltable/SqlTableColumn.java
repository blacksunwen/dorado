package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	nodeName="Column", 
	definitionType="com.bstek.dorado.jdbc.config.ColumnDefinition"
)
public class SqlTableColumn extends AbstractUpdatableColumn {
	
	private String nativeColumnName;

	public String getNativeColumnName() {
		return nativeColumnName;
	}

	public void setNativeColumnName(String nativeColumnName) {
		this.nativeColumnName = nativeColumnName;
	}

}
