package com.bstek.dorado.jdbc.model.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.JdbcRecordOperationProxy;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	parser = "spring:dorado.jdbc.tableParser",
	definitionType = "com.bstek.dorado.jdbc.model.table.TableDefinition", 
	properties = {
		@XmlProperty(
			propertyName = "autoCreateColumns",
			propertyType = "boolean"
		)
	},
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true), 
			propertyName="Jdbc_TableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.table.AbstractTableColumn>", 
			implTypes="com.bstek.dorado.jdbc.model.table.Table*"
		)
	}
)

public class Table extends AbstractTable {

	public static final String TYPE = "Table";
	
	private String namespace;
	private String tableName;
	private String dynamicClause;
	
	private boolean retrieveAfterInsert = false;
	private boolean retrieveAfterUpdate = false;
	
	private List<TableColumn> tableColumns = new ArrayList<TableColumn>();
	private List<TableKeyColumn> keyColumns = new ArrayList<TableKeyColumn>();
	
	public void addColumn(AbstractDbColumn column) {
		if (column instanceof TableKeyColumn) {
			keyColumns.add((TableKeyColumn)column);
		} else if (column instanceof TableColumn) {
			tableColumns.add((TableColumn)column);
		} else {
			throw new IllegalArgumentException("unknown column class [" + column.getClass().getName() + "]");
		}
		super.addColumn(column);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}

	@XmlProperty(attributeOnly=true)
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@XmlProperty(attributeOnly=true)
	@IdeProperty(highlight=1, editor="jdbc:service:list-tables.xml")
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@IdeProperty(highlight=1, editor = "multiLines")
	public String getDynamicClause() {
		return dynamicClause;
	}

	public boolean isRetrieveAfterInsert() {
		return retrieveAfterInsert;
	}

	public void setRetrieveAfterInsert(boolean retrieveAfterInsert) {
		this.retrieveAfterInsert = retrieveAfterInsert;
	}

	public boolean isRetrieveAfterUpdate() {
		return retrieveAfterUpdate;
	}

	public void setRetrieveAfterUpdate(boolean retrieveAfterUpdate) {
		this.retrieveAfterUpdate = retrieveAfterUpdate;
	}

	public void setDynamicClause(String dynamicToken) {
		this.dynamicClause = dynamicToken;
	}

	public List<TableColumn> getTableColumns() {
		return Collections.unmodifiableList(tableColumns);
	}
	
	public List<TableKeyColumn> getKeyColumns() {
		return Collections.unmodifiableList(keyColumns);
	}

	@Override
	public boolean supportResolverTable() {
		return false;
	}

	@Override
	public Table getResolverTable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public JdbcRecordOperationProxy createOperationProxy(Record record, JdbcDataResolverContext jdbcContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		//SelectSql
		TableSelectSql selectSql = createSelectSql();
		
		//tableToken
		Dialect dialect = operation.getJdbcEnviroment().getDialect();
		String tableToken = dialect.token(this);
		selectSql.setTableToken(tableToken);
		
		//parameter
		Object parameter = operation.getParameter();
		if (parameter instanceof KeyObject) {
			KeyObject keyObj = (KeyObject)parameter;
			Object[] keyValues = keyObj.getKeyValues();
			
			Map<String, Object> keyParameter = new HashMap<String, Object>();
			List<TableKeyColumn> keyColumnList = this.getKeyColumns();
			StringBuffer dynamicToken = new StringBuffer("WHERE ");
			for (int i = 0; i < keyColumnList.size(); i++) {
				TableKeyColumn keyColumn = keyColumnList.get(i);
				String columnName = keyColumn.getName();
				Object keyValue = keyValues[i];
				
				JdbcType jdbcType = keyColumn.getJdbcType();
				if (jdbcType != null) {
					keyValue = jdbcType.toDB(keyValue);
				}
				
				keyParameter.put(keyColumn.getName(), keyValue);
				
				if (i > 0) {
					dynamicToken.append(" AND ");
				} 
				dynamicToken.append(columnName + " = :" + columnName);
			}
			
			selectSql.setDynamicToken(dynamicToken.toString());
			selectSql.setParameter(keyParameter);
		} else {
			selectSql.setDynamicToken(this.getDynamicClause());
			selectSql.setParameter(parameter);
			
			Criteria criteria = this.getCriteria(operation);
			if (criteria != null) {
				selectSql.setCriteria(criteria);
			}
		}
		
		return selectSql;
	}
	
	private TableSelectSql createSelectSql() {
		//SelectSql
		TableSelectSql selectSql = new TableSelectSql();

		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<AbstractDbColumn> columns = this.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			AbstractDbColumn column = columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String columnName = column.getName();
				String propertyName = column.getPropertyName();
				if (!columnName.equals(propertyName)) {
					String token = columnName + " " + KeyWord.AS + " "  + propertyName;
					columnsToken.append(token);
				} else {
					columnsToken.append(columnName);
				}
			}
		}
		selectSql.setColumnsToken(columnsToken.toString());
		
		return selectSql;
	}
	
	public KeyObject createKeyObject(Object...keys) {
		return new KeyObject(this, keys);
	}
}
