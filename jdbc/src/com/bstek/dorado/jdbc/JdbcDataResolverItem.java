package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.support.BatchSql;

/**
 * 与UpdateAction的Item对应的Item
 * @author mark.li@bstek.com
 *
 */
@XmlNode(nodeName="ResolverItem")
public class JdbcDataResolverItem {

	private String name;
	
	private String tableName;
	
	private String[] parentKeyProperties = new String[0];
	
	private String[] foreignKeyProperties = new String[0];
	
	private String recursiveProperty;
	
	private EntityDataType dataType;
	
	private DbTable dbTable;
	
	private boolean supportBatchSql = true;
	
	private BatchSql batchSql;
	
	private List<JdbcDataResolverItem> resolverItems = new ArrayList<JdbcDataResolverItem>(5);
	
	public JdbcDataResolverItem clone() {
		JdbcDataResolverItem cloned = new JdbcDataResolverItem();
		cloned.setName(name);
		cloned.setTableName(tableName);
		cloned.setForeignKeyProperties(foreignKeyProperties);
		cloned.setParentKeyProperties(parentKeyProperties);
		cloned.setRecursiveProperty(recursiveProperty);
		
		for (JdbcDataResolverItem item: resolverItems) {
			cloned.getResolverItems().add(item.clone());
		}
		return cloned;
	}
	
	@IdeProperty(highlight=1, editor="jdbc:refrence:Table")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getParentKeyProperties() {
		return parentKeyProperties;
	}

	public void setParentKeyProperties(String[] parentKeyProperties) {
		this.parentKeyProperties = parentKeyProperties;
	}

	public String[] getForeignKeyProperties() {
		return foreignKeyProperties;
	}

	public void setForeignKeyProperties(String[] foreignKeyProperties) {
		this.foreignKeyProperties = foreignKeyProperties;
	}

	public String getRecursiveProperty() {
		return recursiveProperty;
	}

	public void setRecursiveProperty(String recursiveProperty) {
		this.recursiveProperty = recursiveProperty;
	}

	public EntityDataType getDataType() {
		return dataType;
	}

	public void setDataType(EntityDataType dataType) {
		this.dataType = dataType;
	}

	public DbTable getDbTable() {
		if (dbTable == null) {
			dbTable = JdbcUtils.getDbTable(tableName);
		}
		return dbTable;
	}

	public void setDbTable(DbTable dbTable) {
		this.dbTable = dbTable;
	}

	@ClientProperty(escapeValue="true")
	public boolean isSupportBatchSql() {
		return supportBatchSql;
	}

	public void setSupportBatchSql(boolean supportBatchSql) {
		this.supportBatchSql = supportBatchSql;
	}

	public BatchSql getBatchSql() {
		return batchSql;
	}

	public void setBatchSql(BatchSql batchSql) {
		this.batchSql = batchSql;
	}

	@XmlSubNode
	public List<JdbcDataResolverItem> getResolverItems() {
		return this.resolverItems;
	}

	public void setResolverItems(List<JdbcDataResolverItem> resolverItems) {
		this.resolverItems = resolverItems;
	}
	
}
