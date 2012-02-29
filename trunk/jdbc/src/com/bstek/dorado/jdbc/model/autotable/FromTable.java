package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	definitionType = "com.bstek.dorado.jdbc.model.autotable.FromTableDefinition"
)
public class FromTable {
	private String name;
	private String tableName;
	private Table table;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return tableName;
	}

	public void setTable(String tableName) {
		this.tableName = tableName;
	}
	
	public Table getTableObject() {
		if (table == null) {
			String tableName = this.getTable();
			Assert.notEmpty(tableName, "table must not be null. [" + name + "]");
			
			table = (Table)JdbcUtils.getDbTable(tableName);
		}
		
		return table;
	}
}
