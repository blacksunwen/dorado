package com.bstek.dorado.jdbc.model.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperationProxy;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;

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
	
	public void addColumn(AbstractColumn column) {
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

	@IdeProperty(highlight=1, editor="jdbc:service:list-space")
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
		Table table = (Table)operation.getDbTable();
		Object parameter = operation.getParameter();
		Dialect dialect = operation.getJdbcEnviroment().getDialect();
		
		//SelectSql
		TableSelectSql selectSql = new TableSelectSql();

		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<AbstractColumn> columns = table.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			AbstractColumn column = columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String columnName = column.getName();
				String propertyName = column.getPropertyName();
				String token = columnName + " " + KeyWord.AS + " "  + propertyName;
				columnsToken.append(token);
			}
		}
		selectSql.setColumnsToken(columnsToken.toString());
		
		//tableToken
		String tableToken = dialect.token(table);
		selectSql.setTableToken(tableToken);
		
		//dynamicToken
		String dynamicToken = table.getDynamicClause();
		dynamicToken = SqlUtils.build(dynamicToken, parameter);
		
		selectSql.setDynamicToken(dynamicToken);
		
		//JdbcParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		
		return selectSql;
	}
}
