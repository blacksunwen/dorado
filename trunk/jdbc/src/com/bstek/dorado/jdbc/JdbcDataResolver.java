package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElementTrigger;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.resolver.DataResolver}
 * 
 * @author mark
 * 
 */
public class JdbcDataResolver extends AbstractDataResolver {

	private List<JdbcDataResolverItem> items;
	
	public List<JdbcDataResolverItem> getItems() {
		return items;
	}

	public void setItems(List<JdbcDataResolverItem> items) {
		this.items = items;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object internalResolve(DataItems dataItems, Object parameter)
			throws Exception {
		if (items != null && !items.isEmpty()) {
			for (JdbcDataResolverItem item: items) {
				String name = item.getName();
				String eName = item.getDbElement();
				Assert.notEmpty(name, "value of name property must not be empty.");
				
				if (StringUtils.isNotEmpty(eName)) {
					DbElement dbElement = JdbcUtils.getDbElement(eName);
					if (dataItems.containsKey(name)) {
						Object dataItem = dataItems.get(name);
						if (dataItem instanceof Record) {
							Record record = (Record) dataItem;
							this.doResolve(record, dbElement, parameter);
						} else if (dataItem instanceof Collection) {
							Collection records = (Collection)dataItem;
							this.doResolve(records, dbElement, parameter);
						}
					}
				}
			}
		}
		return null;
	}

	protected void doResolve(Collection<Record> records, DbElement dbElement, Object parameter) {
		for (Record record: records) {
			doResolve(record, dbElement, parameter);
		}
	}
	
	protected void doResolve(Record record, DbElement dbElement, Object parameter) {
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(dbElement.getJdbcEnviroment(), parameter);
		JdbcDataResolverOperation operation = new JdbcDataResolverOperation(dbElement, record, jdbcContext);
		
		DbElementTrigger trigger = dbElement.getTrigger();
		if (trigger == null) {
			operation.execute();
		} else {
			trigger.doResolve(operation);
		}
	}
}
