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
	
}
