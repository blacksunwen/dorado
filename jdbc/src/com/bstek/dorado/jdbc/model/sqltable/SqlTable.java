package com.bstek.dorado.jdbc.model.sqltable;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperationProxy;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;

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

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean supportResolverTable() {
		return true;
	}

	@Override
	public Table getResolverTable() {
		if (mainTable == null && StringUtils.isNotEmpty(mainTableName)) {
			mainTable = (Table)JdbcUtils.getDbTable(mainTableName);
		}
		return mainTable;
	}

	@Override
	public JdbcRecordOperationProxy createOperationProxy(Record record, JdbcDataResolverContext jdbcContext) {
		if (EntityUtils.isEntity(record)) {
			EntityState state = EntityUtils.getState(record);
			if (EntityState.isDirty(state)) {
				Table proxyTable = this.getResolverTable();
				Record proxyRecord = new Record();
				try {
					proxyRecord = EntityUtils.toEntity(proxyRecord);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				EntityUtils.setState(proxyRecord, EntityUtils.getState(record));
				JdbcRecordOperation proxyOperation = new JdbcRecordOperation(proxyTable, proxyRecord, jdbcContext);
				
				Map<String, String> proxyPropertyMap = new HashMap<String, String>();
				for (AbstractDbColumn c: this.getAllColumns()) {
					AbstractUpdatableColumn column = (AbstractUpdatableColumn)c;
					String nativeColumnName = column.getNativeColumn();
					String propertyName = column.getPropertyName();
					if (StringUtils.isNotEmpty(nativeColumnName)) {
						if ((EntityState.NEW.equals(state) && column.isInsertable()) || 
							(EntityState.MODIFIED.equals(state) && column.isUpdatable()) ||
							(EntityState.MOVED.equals(state) && column.isUpdatable()) ||
							(EntityState.DELETED.equals(state))
							) {
							AbstractDbColumn tableColumn = proxyTable.getColumn(nativeColumnName);
							String tpn = tableColumn.getPropertyName();
							if (StringUtils.isNotEmpty(tpn)) {
								Object value = record.get(propertyName);
								proxyRecord.put(tpn, value);
								proxyPropertyMap.put(propertyName, tpn);
							}
						}
					}
				}
				
				JdbcRecordOperationProxy proxy = new JdbcRecordOperationProxy();
				proxy.setProxyOperation(proxyOperation);
				proxy.setProxyPropertyMap(proxyPropertyMap);
				proxy.setRecord(proxyRecord);
				
				return proxy;
			}
		}
		
		return null;
	}
	
	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		SqlTable t = (SqlTable)operation.getDbTable();
		Object parameter = operation.getParameter();
		SqlSelectSql selectSql = new SqlSelectSql();
		
		//querySql
		String querySql = t.getQuerySql();
		querySql = SqlUtils.build(querySql, parameter);
		selectSql.setDynamicToken(querySql);
		
		//SqlParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		return selectSql;
	}
}