package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderDirection;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDialect implements Dialect {

	private QueryCommand  queryCommand;
	private InsertCommand insertCommand;
	private UpdateCommand updateCommand;
	private DeleteCommand deleteCommand;
	
	private JdbcIntercepter intercepter;

	public void setIntercepter(JdbcIntercepter intercepter) {
		this.intercepter = intercepter;
	}

	public void setQueryCommand(QueryCommand queryCommand) {
		this.queryCommand = queryCommand;
	}

	public void setInsertCommand(InsertCommand insertCommand) {
		this.insertCommand = insertCommand;
	}

	public void setUpdateCommand(UpdateCommand updateCommand) {
		this.updateCommand = updateCommand;
	}

	public void setDeleteCommand(DeleteCommand deleteCommand) {
		this.deleteCommand = deleteCommand;
	}

	public String token(Table table) {
		Assert.notNull(table, "Table must not be null.");
		
		String tableName = table.getTableName();
		String namespace = table.getNamespace();
		
		if (StringUtils.isNotEmpty(namespace)) {
			return namespace + "." + tableName;
		} else {
			return tableName;
		}
	}
	
	public String token(Table table, String alias) {
		String token = token(table);
		if (StringUtils.isEmpty(alias)) {
			return token;
		} else {
			return token + " " + KeyWord.AS + " " + alias;
		}
	}
	
	public String propertyName(Map<String,String> columnMeta) {
		String label = columnMeta.get(JdbcConstants.COLUMN_LABEL);
		if (StringUtils.isEmpty(label)) {
			return columnMeta.get(JdbcConstants.COLUMN_NAME);
		} else {
			return label;
		}
	}
	
	public String toCountSQL(String sql) {
		return "SELECT COUNT(1) FROM ( " + sql + " )";
	}
	
	public String toSQL(SelectSql selectSql) {
		return selectSql.toSQL(this);
	}
	
	public String toCountSQL(SelectSql selectSql) {
		return selectSql.toCountSQL(this);
	}
	
	public boolean execute(JdbcDataProviderOperation operation) {
		if (intercepter != null) {
			operation = intercepter.getOperation(operation);
		}
		return queryCommand.execute(operation);
	}
	
	public boolean execute(JdbcRecordOperation operation) {
		if (intercepter != null) {
			operation = intercepter.getOperation(operation);
		}
		Record record = operation.getRecord();
		Assert.notNull(record, "record must not null.");

		EntityState state = EntityUtils.getState(record);
		switch (state) {
			case NEW: {
				insertCommand.execute(operation);
				return true;
			}
			case MODIFIED: 
			case MOVED:{
				updateCommand.execute(operation);
				return true;
			}
			case DELETED: {
				deleteCommand.execute(operation);
				return true;
			}
		}
		return false;
	}
	
	
	public String token(AutoTable autoTable, JoinOperator joinModel) {
		switch (joinModel) {
		case INNER_JOIN:
			return "INNER JOIN";
		case LEFT_JOIN:
			return "LEFT JOIN";
		case RIGHT_JOIN:
			return "RIGHT JOIN";
		}
		
		throw new IllegalArgumentException("unknown JoinModel '" + joinModel + "'");
	}
	
	@Override
	public String token(AutoTable autoTable, Order order) {
		OrderDirection model = order.getDirection();
		NullsDirection nullsModel = order.getNullsDirection(); 
		String columnName = order.getColumn();
		Assert.notEmpty(columnName, "[" + autoTable.getName() + "] columnName must not be null in order");
		
		StringBuilder token = new StringBuilder();
		String tableAlias = order.getFromTable();
		if (StringUtils.isEmpty(tableAlias)) {
			AutoTableColumn column = (AutoTableColumn)autoTable.getColumn(columnName);
			token.append(column.getName());
		} else {
			FromTable fromTable = autoTable.getFromTable(tableAlias);
			AbstractDbColumn fromColumn = fromTable.getTableObject().getColumn(columnName);
			token.append(fromTable.getName() + '.' + fromColumn.getName());
		}
		
		if (model != null) {
			token.append(' ');
			token.append(model.toString());
		}
		if (nullsModel != null) {
			String nullsModelToken = token(nullsModel);
			if (StringUtils.isNotEmpty(nullsModelToken)) {
				token.append(' ');
				token.append(nullsModelToken);
			}
		}
		
		return token.toString();
	}
	
	protected String token(NullsDirection nullsModel) {
		switch(nullsModel) {
		case NULLS_FIRST:
			return "NULLS FIRST";
		case NULLS_LAST:
			return "NULLS LAST";
		}
		
		return null;
	}
	
	private String defaultCatalog;
	public String defaultCatalog(DataSource dataSource, DatabaseMetaData databaseMetaData) {
		return defaultCatalog;
	}
	
	private String defaultSchema;
	public String defaultSchema(DataSource dataSource, DatabaseMetaData databaseMetaData) {
		return defaultSchema;
	}

	public String getDefaultCatalog() {
		return defaultCatalog;
	}

	public void setDefaultCatalog(String defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
}
