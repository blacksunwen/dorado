package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.DbTable;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark.li@bstek.com
 * 
 */
public class QueryOperation extends
		AbstractDbTableOperation<DataProviderContext, DbTable> {

	public QueryOperation(DbTable dbTable,
			DataProviderContext jdbcContext) {
		super(dbTable, jdbcContext);
	}

	public Object getParameter() {
		DataProviderContext ctx = this.getJdbcContext();
		if (ctx != null) {
			return ctx.getParameter();
		} else {
			return null;
		}
	}
	
	public Criteria getCriteria() {
		DataProviderContext ctx = this.getJdbcContext();
		if (ctx != null) {
			return ctx.getCriteria();
		} else {
			return null;
		}
	}
	
	private List<AbstractDbColumn> columns = null;
	public List<AbstractDbColumn> getDbColumns() {
		if (columns == null) {
			DbTable dbTable = this.getDbTable();
			EntityDataType entityDataType = this.getJdbcContext().getDataType();
			if (entityDataType == null) {
				columns = dbTable.getAllColumns();
			} else {
				Map<String, PropertyDef> propertyDefMap = entityDataType.getPropertyDefs();
				Set<String> propertyDefKeySet = propertyDefMap.keySet();
				columns = new ArrayList<AbstractDbColumn>(propertyDefKeySet.size());
				for (String key: propertyDefKeySet) {
					if (dbTable.hasColumn(key)) {
						AbstractDbColumn column = dbTable.getColumn(key);
						if (column.isSelectable()) {
							columns.add(column);
						}
					}
				}
			}
		}
		
		return columns;
	}
	
	protected boolean doRun() throws Exception {
		return getDialect().execute(this);
	}
}
