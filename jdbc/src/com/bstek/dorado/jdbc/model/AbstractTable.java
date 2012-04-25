package com.bstek.dorado.jdbc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.DbTableTrigger;
import com.bstek.dorado.jdbc.support.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.support.JdbcRecordOperation;
import com.bstek.dorado.jdbc.support.JdbcRecordOperationProxy;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractTable extends AbstractDbElement implements DbTable {

	private Map<String,AbstractDbColumn> columnMap = new LinkedHashMap<String,AbstractDbColumn>();
	private DbTableTrigger trigger;
	
	public List<AbstractDbColumn> getAllColumns() {
		return new ArrayList<AbstractDbColumn>(columnMap.values());
	}
	
	public AbstractDbColumn getColumn(String name) {
		AbstractDbColumn c = columnMap.get(name);
		Assert.notNull(c, "No column named [" + name + "] in table [" + this.getName() + "]");
		return c;
	}
	
	public void addColumn(AbstractDbColumn column) {
		String columnName = column.getName();
		Assert.notEmpty(columnName, "columnName must not be empty in table [" + this.getName() + "]");
		
		if (columnMap.containsKey(columnName)) {
			throw new IllegalArgumentException("Duplicate column named [" + columnName + "] in table [" + this.getName() + "]");
		}
		columnMap.put(columnName, column);
	}

	@XmlProperty(parser="spring:dorado.jdbc.triggerParser")
	public DbTableTrigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(DbTableTrigger trigger) {
		this.trigger = trigger;
	}
	
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
					if (this.acceptByProxy(column, state)) {
						String nativeColumnName = column.getNativeColumn();
						if (StringUtils.isEmpty(nativeColumnName)) {
							nativeColumnName = column.getName();
						}
						
						String propertyName = column.getPropertyName();
						AbstractDbColumn tableColumn = proxyTable.getColumn(nativeColumnName);
						String tpn = tableColumn.getPropertyName();
						if (StringUtils.isNotEmpty(tpn)) {
							Object value = record.get(propertyName);
							proxyRecord.put(tpn, value);
							proxyPropertyMap.put(propertyName, tpn);
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
	
	protected boolean acceptByProxy(AbstractUpdatableColumn column, EntityState state) {
		if ((EntityState.NEW.equals(state) && column.isInsertable()) || 
				(EntityState.MODIFIED.equals(state) && column.isUpdatable()) ||
				(EntityState.MOVED.equals(state) && column.isUpdatable()) ||
				(EntityState.DELETED.equals(state))
			) {
			return true;
		}
		
		return false;
	}

}
