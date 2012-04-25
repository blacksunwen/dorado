package com.bstek.dorado.jdbc.model;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.sqltable.SqlSelectSql;
import com.bstek.dorado.jdbc.model.sqltable.SqlTableColumn;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	parser = "spring:dorado.jdbc.sqlTableParser",
	definitionType="com.bstek.dorado.jdbc.model.sqltable.SqlTableDefinition",
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true), 
			propertyName="Jdbc_SqlTableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.sqltable.SqlTableColumn>"
		)
	}
)
public class SqlTable extends AbstractTable {
	public static final String TYPE = "SqlTable";
	
	private String querySql;
	
	private String mainTableName;

	private Table mainTable;
	
	public void addColumn(AbstractDbColumn column) {
		if (column instanceof SqlTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException("unknown column class " + column.getClass());
		}
	}
	
	@IdeProperty(highlight=1, editor = "multiLines")
	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
	
	@IdeProperty(highlight=1, editor="jdbc:refrence:Table")
	public String getMainTable() {
		return mainTableName;
	}

	public void setMainTable(String table) {
		this.mainTableName = table;
	}

	public String getType() {
		return TYPE;
	}

	public boolean supportResolverTable() {
		return true;
	}

	public Table getResolverTable() {
		if (mainTable == null && StringUtils.isNotEmpty(mainTableName)) {
			mainTable = (Table)JdbcUtils.getDbTable(mainTableName);
		}
		return mainTable;
	}
	
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		SqlSelectSql selectSql = new SqlSelectSql();
		
		//querySql
		SqlTable t = (SqlTable)operation.getDbTable();
		String querySql = t.getQuerySql();
		selectSql.setDynamicToken(querySql);
		
		//parameter
		selectSql.setParameter(operation.getParameter());
		
		if (operation.getJdbcContext().isAutoFilter()) {
			Criteria criteria = operation.getCriteria();
			if (criteria != null) {
				selectSql.setCriteria(criteria);
			}
		}
		
		return selectSql;
	}
}