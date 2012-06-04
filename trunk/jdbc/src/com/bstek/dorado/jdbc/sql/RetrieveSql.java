package com.bstek.dorado.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class RetrieveSql extends AbstractTableSql {

	private Table table;
	private List<AbstractDbColumn> columns = new ArrayList<AbstractDbColumn>();
	private RecordRowMapper recordRowMapper;
	
	public RetrieveSql(Table table) {
		this.table = table;
	}
	
	public void addColumnToken(AbstractDbColumn column) {
		columns.add(column);
	}

	public RecordRowMapper getRecordRowMapper() {
		if (recordRowMapper == null) {
			recordRowMapper = new RecordRowMapper(columns);
		}
		return recordRowMapper;
	}

	@Override
	protected String toSQL(Dialect dialect) {
		String tableToken = this.getTableToken();
		Assert.notEmpty(tableToken, "tableToken must not be empty.");
		Assert.notEmpty(columns, "columns must not be empty.");
		
		List<String> cList = new ArrayList<String>(columns.size());
		for (int i=0; i<columns.size(); i++) {
			AbstractDbColumn column = columns.get(i);
			String columnName = column.getName();
			String propertyName = column.getPropertyName();
			
			if (StringUtils.isEmpty(propertyName) || propertyName.equals(columnName)) {
				cList.add(columnName);
			} else {
				cList.add(columnName + " " + KeyWord.AS + " " + propertyName);
			}
		}
		String columnsToken = StringUtils.join(cList, ',');
		
		String[] keyArray = new String[table.getKeyColumns().size()];
		for (int i = 0; i < keyArray.length; i++) {
			TableKeyColumn keyColumn = table.getKeyColumns().get(i);
			String propertyName = keyColumn.getPropertyName();
			String columnName = keyColumn.getName();
			
			keyArray[i] = columnName + "=:" + propertyName;
		}
		
		String whereToken = StringUtils.join(keyArray, " " + KeyWord.AND + " ");
		
		SqlBuilder sql = new SqlBuilder();
		sql.rightSpace(KeyWord.SELECT, columnsToken, KeyWord.FROM, tableToken, KeyWord.WHERE).append(whereToken);
		
		return sql.build();
	}

}
