package com.bstek.dorado.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class InsertSql extends AbstractTableSql {

	private List<String> columnTokenList = new ArrayList<String>();
	private List<String> valueTokenList = new ArrayList<String>();
	
	private TableKeyColumn identityColumn;
	private boolean retrieveAfterExecute = false;
	
	public InsertSql() {
		super();
	}
	
	public void addColumnToken(String columnName, String value) {
		columnTokenList.add(columnName);
		valueTokenList.add(value);
	}
	
	public TableKeyColumn getIdentityColumn() {
		return identityColumn;
	}

	public void setIdentityColumn(TableKeyColumn identityColumn) {
		Assert.isNull(this.identityColumn, "already has IDENTITY column.");
		this.identityColumn = identityColumn;
	}

	public boolean isRetrieveAfterExecute() {
		return retrieveAfterExecute;
	}

	public void setRetrieveAfterExecute(boolean retrieveAfterExecute) {
		this.retrieveAfterExecute = retrieveAfterExecute;
	}
	
	@Override
	public String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columnTokenList, "columnTokenList must not be empty.");
		Assert.notEmpty(valueTokenList, "valueTokenList must not be empty.");

		String columnsToken = StringUtils.join(columnTokenList, ',');
		String valuesToken = StringUtils.join(valueTokenList, ',');
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.INSERT_INTO, tableToken).brackets(columnsToken).bothSpace(KeyWord.VALUES).brackets(valuesToken);
		return sql.build();
	}

}
