package com.bstek.dorado.jdbc.support;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AutoTable;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.sql.DeleteAllSql;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsDirection;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderDirection;
import com.bstek.dorado.jdbc.sql.UpdateSql;
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
	private DeleteAllCommand deleteAllCommand;
	private SaveCommand saveCommand;
	private SaveRecordCommand saveRecordCommand;
	
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

	public void setDeleteAllCommand(DeleteAllCommand deleteAllCommand) {
		this.deleteAllCommand = deleteAllCommand;
	}

	public void setSaveCommand(SaveCommand saveCommand) {
		this.saveCommand = saveCommand;
	}

	public void setSaveRecordCommand(SaveRecordCommand saveRecordCommand) {
		this.saveRecordCommand = saveRecordCommand;
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
			Table table = fromTable.getTableObject();
			AbstractDbColumn fromColumn = table.getColumn(columnName);
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
	
	public String toCountSQL(String sql) {
		return "SELECT COUNT(1) FROM ( " + sql + " )";
	}
	
	public String toSQL(SelectSql selectSql) throws Exception{
		return selectSql.getSQL(this);
	}
	
	public String toSQL(RetrieveSql retrieveSql) throws Exception {
		return retrieveSql.getSQL(this);
	}
	
	public String toSQL(DeleteSql deleteSql) throws Exception {
		return deleteSql.getSQL(this);
	}
	
	public String toSQL(DeleteAllSql sql) throws Exception {
		return sql.getSQL(this);
	}
	
	public String toSQL(InsertSql insertSql) throws Exception {
		return insertSql.getSQL(this);
	}
	
	public String toSQL(UpdateSql updateSql) throws Exception {
		return updateSql.getSQL(this);
	}
	
	public String toCountSQL(SelectSql selectSql) throws Exception{
		return selectSql.toCountSQL(this);
	}
	
	public boolean execute(QueryOperation operation) throws Exception{
		if (operation.isProcessDefault()) {
			if (intercepter != null) {
				operation = intercepter.getOperation(operation);
			}
			queryCommand.execute(operation);
		}
		
		return true;
	}

	public boolean execute(SaveOperation operation) throws Exception {
		if (operation.isProcessDefault()) {
			if (intercepter != null) {
				operation = intercepter.getOperation(operation);
			}
			saveCommand.execute(operation);
		} 
		return true;
	}
	
	public boolean execute(SaveRecordOperation operation) throws Exception {
		if (operation.isProcessDefault()) {
			if (intercepter != null) {
				operation = intercepter.getOperation(operation);
			}
			saveRecordCommand.execute(operation);
		}
		return true;
	}
	
	public boolean execute(TableRecordOperation operation) throws Exception {
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
	
	public boolean execute(DeleteAllOperation operation) throws Exception {
		if (intercepter != null) {
			operation = intercepter.getOperation(operation);
		}
		
		deleteAllCommand.execute(operation);
		return true;
	}
}
