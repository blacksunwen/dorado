package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.CurdSqlGenerator;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.util.Assert;

public class AutoTableSqlGenerator implements CurdSqlGenerator{

	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		AutoTable autoTable = (AutoTable)operation.getDbTable();
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		Object parameter = jdbcContext.getParameter();
		
		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<AbstractColumn> columns = autoTable.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			AutoTableColumn column = (AutoTableColumn)columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String tableAlias = column.getTableAlias();
				String nativeName = column.getNativeColumnName();
				if (StringUtils.isNotEmpty(nativeName)) {
					String propertyName = column.getPropertyName();
					String token = tableAlias + "." + nativeName + " " + KeyWord.AS + " " + propertyName;
					columnsToken.append(token);
				}
			}
		}
		
		JdbcEnviroment jdbcEnv = operation.getJdbcEnviroment();
		Dialect dialect = jdbcEnv.getDialect();
		//fromToken
		StringBuilder fromToken = fromToken(autoTable, dialect);
		//where
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		StringBuilder whereToken = whereToken(autoTable, p);
		//order
		StringBuilder orderbyToken = orderByToken(autoTable, p, dialect);
		
		//--
		AutoTableSelectSql selectSql = new AutoTableSelectSql();
		selectSql.setParameterSource(p);
		selectSql.setColumnsToken(columnsToken.toString());
		selectSql.setFromToken(fromToken.toString());
		selectSql.setWhereToken(whereToken.toString());
		selectSql.setOrderToken(orderbyToken.toString());
		
		return selectSql;
	}

	protected String token(Dialect dialect, FromTable fromTable) {
		return dialect.token(fromTable.getTable(), fromTable.getTableAlias());
	}
	
	protected StringBuilder fromToken(AutoTable t, Dialect dialect) {
		StringBuilder fromToken = new StringBuilder();
		List<JoinTable> joinTables = t.getJoinTables();
		
		if (joinTables.size() == 0) {
			List<FromTable> fromTables = t.getFromTables();
			Assert.isTrue(fromTables.size() > 0, "no from tables defined.");
			
			for (int i=0; i<fromTables.size(); i++) {
				FromTable fromTable = fromTables.get(i);
				if (i > 0) {
					fromToken.append(',');
				}
				
				String token = token(dialect, fromTable);
				fromToken.append(token);
			}
		} else {
			for (int i=0; i<joinTables.size(); i++) {
				JoinTable joinTable = joinTables.get(i);
				
				JoinModel joinModel = joinTable.getJoinModel();
				String[] leftColumnNames = joinTable.getLeftColumnNames();
				String[] rightColumnNames = joinTable.getRightColumnNames();
				
				Assert.isTrue(leftColumnNames.length > 0, 
						"length of LeftColumnNames must greate than 0.");
				
				Assert.isTrue(rightColumnNames.length > 0, 
						"length of RightColumnNames length must greate than 0.");
				
				Assert.isTrue(leftColumnNames.length == rightColumnNames.length, 
						"length of LeftColumnNames and length of RightColumnNames not equals.");
				
				
				FromTable leftFromTable = t.getFromTable(joinTable.getLeftFromTableAlias());
				FromTable rightFromTable = t.getFromTable(joinTable.getRightFromTableAlias());
				String token = this.joinToken(dialect, t, joinModel, leftFromTable, leftColumnNames, rightFromTable, rightColumnNames);
				
				if (i > 0) {
					fromToken.append(',');
				}
				fromToken.append(token);
			}
		}
		
		return fromToken;
	}

	protected String joinToken(Dialect dialect, AutoTable autoTable, JoinModel joinModel, FromTable leftFromTable,
			String[] leftColumnNames, FromTable rightFromTable,
			String[] rightColumnNames) {
		SqlBuilder token = new SqlBuilder();
		String leftTableAlias = leftFromTable.getTableAlias();
		String rightTableAlias = rightFromTable.getTableAlias();
		
		Table leftTable = leftFromTable.getTable();
		Table rightTable = rightFromTable.getTable();
		
		String tl = token(dialect, leftFromTable);
		String tr = token(dialect, rightFromTable);
		String jm = dialect.token(autoTable,joinModel);
		
		token.append(tl).bothSpace(jm).append(tr);
		token.bothSpace(KeyWord.ON);
		for (int i=0; i<leftColumnNames.length; i++) {
			if (i>0) {
				token.bothSpace(KeyWord.AND);
			}
			
			String leftColumnName = leftColumnNames[i];
			String rightColumnName = rightColumnNames[i];
			AbstractColumn leftColumn = leftTable.getColumn(leftColumnName);
			AbstractColumn rightColumn = rightTable.getColumn(rightColumnName);
			
			token.append(leftTableAlias, ".", leftColumn.getColumnName());
			token.bothSpace("=");
			token.append(rightTableAlias, ".", rightColumn.getColumnName());
		}
		
		return token.build();
	}
	
	protected StringBuilder whereToken(AutoTable t, JdbcParameterSource p) {
		StringBuilder whereToken = new StringBuilder();
		
		Where where = t.getWhere();
		if (where != null) {
			String token = junctionMatchRuleToken(where, p);
			if (StringUtils.isNotEmpty(token)) {
				whereToken.append(token);
			}
		}
		
		return whereToken;
	}
	
	protected String baseMatchRuleToken(BaseMatchRule bmr, JdbcParameterSource p) {
		if (!bmr.isAvailable()) {
			return "";
		} else {
			FromTable fromTable = bmr.getFromTable();
			String tableAlias = fromTable.getTableAlias();
			AbstractColumn column = bmr.getColumn();
			String columnName = column.getColumnName();
			Object value = bmr.getValue();
			String operator = bmr.getOperator();
			
			if (value == null || StringUtils.isEmpty(operator)) {
				return "";
			}
			
			String parameterName = null;
			if (value instanceof String) {
				String strValue = (String)value;
				if (StringUtils.isNotEmpty(strValue) && strValue.length() > 1) {
					if(strValue.charAt(0) == ':') {
						String pn = strValue.substring(1);
						if (p.hasValue(pn)) {
							parameterName = pn;
							value = p.getValue(pn);
						} else {
							value = null;
						}
					}
				}
			}
			
			if (value != null) {
				if (parameterName == null) {
					parameterName = p.addValue(value);
				}
				
				SqlConstants.Operator sqlOperator = SqlConstants.Operator.value(operator.trim());
				String opToken = bmr.isNot() ? sqlOperator.notSQL(): sqlOperator.toSQL();
				
				Object parameterValue = sqlOperator.parameterValue(value);
				p.setValue(parameterName, parameterValue);
				
				return tableAlias + "." + columnName + " " + opToken + " :" + parameterName;
			}
			
			return "";
		}
	}
	
	protected String junctionMatchRuleToken(JunctionMatchRule amr, JdbcParameterSource p) {
		if (!amr.isAvailable()) {
			return "";
		} else {
			List<MatchRule> matchRules = amr.getMatchRules();
			if (matchRules.size() > 0) {
				JunctionModel model = amr.getModel();
				Assert.notNull(model, "JunctionModel must not be null.");
				
				List<String> tokens = new ArrayList<String>(matchRules.size());
				for (MatchRule mr: matchRules) {
					if (mr instanceof BaseMatchRule) {
						BaseMatchRule bmr = (BaseMatchRule)mr;
						String token = baseMatchRuleToken(bmr, p);
						if (StringUtils.isNotEmpty(token)) {
							tokens.add(token);
						}
					} else if (mr instanceof JunctionMatchRule) {
						JunctionMatchRule amr1 = (JunctionMatchRule)mr;
						String token = junctionMatchRuleToken(amr1, p);
						if (StringUtils.isNotEmpty(token)) {
							tokens.add("("+token+")");
						}
					} else {
						throw new IllegalArgumentException("unknown MatchRule class [" + mr.getClass().getName() + "]");
					}
				}
				
				if (tokens.size() > 0) {
					String token;
					if (tokens.size() == 1) {
						token = tokens.get(0);
					} else {
						String m = SqlUtils.bothSpace(model.toString());
						token = StringUtils.join(tokens, m);
					}
					if (amr.isNot()) {
						return KeyWord.NOT + "(" + token + ")";
					} else {
						return token;
					}
				}
			}
			
			return "";
		}
	}
	
	protected StringBuilder orderByToken(AutoTable t, JdbcParameterSource p, Dialect dialect) {
		StringBuilder r = new StringBuilder();
		
		List<Order> orders = t.getOrders();
		if (orders != null && !orders.isEmpty()) {
			List<String> tokens = new ArrayList<String>(orders.size());
			for (int i=0; i<orders.size(); i++) {
				Order order = orders.get(i);
				if (order.isAvailable()) {
					String token = dialect.token(t, order);
					if (StringUtils.isNotEmpty(token)) {
						tokens.add(token);
					}
				}
			}
			
			if (tokens.size() > 0) {
				if (tokens.size() == 1) {
					String s = tokens.get(0).toString(); 
					r.append(s);
				} else {
					String s = StringUtils.join(tokens, ',');
					r.append(s);
				}
			}
		}
		
		return r;
	}

	@Override
	public InsertSql insertSql(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(AutoTableColumn column) {
				return column.isInsertable();
			}
		});
		
		AutoTable autoTable = ( AutoTable)operation.getDbTable();
		FromTable fromTable = autoTable.getMainFromTable();
		Table table = fromTable.getTable();
		
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.insertSql(sOperation);
	}

	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(AutoTableColumn column) {
				return column.isUpdatable();
			}
		});
		
		AutoTable autoTable = ( AutoTable)operation.getDbTable();
		FromTable fromTable = autoTable.getMainFromTable();
		Table table = fromTable.getTable();
		
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.updateSql(sOperation);
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		JdbcRecordOperation sOperation = createOperation(operation, new OperationConfig() {
			@Override
			public boolean accept(AutoTableColumn column) {
				return true;
			}
		});
		
		AutoTable autoTable = ( AutoTable)operation.getDbTable();
		FromTable fromTable = autoTable.getMainFromTable();
		Table table = fromTable.getTable();
		
		CurdSqlGenerator generator = table.getCurdSqlGenerator();
		return generator.deleteSql(sOperation);
	}

	interface OperationConfig {
		boolean accept(AutoTableColumn column);
	}
	
	protected JdbcRecordOperation createOperation(JdbcRecordOperation operation, OperationConfig config) {
		AutoTable autoTable = ( AutoTable)operation.getDbTable();
		FromTable fromTable = autoTable.getMainFromTable();
		Assert.notNull(fromTable, " [" + autoTable.getName() + "] " + "has no fromTable.");
		
		Table table = fromTable.getTable();
		
		Record record = operation.getRecord();
		Record sRecord = new Record();
		String tableAlias = fromTable.getTableAlias();
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (AbstractColumn c: autoTable.getAllColumns()) {
			AutoTableColumn column = (AutoTableColumn)c;
			String columnName = column.getNativeColumnName();
			String propertyName = column.getPropertyName();
			if (StringUtils.isNotEmpty(columnName)) {
				if (tableAlias.equals(column.getTableAlias()) && config.accept(column)) {
					AbstractColumn tableColumn = table.getColumn(columnName);
					String tpn = tableColumn.getPropertyName();
					if (StringUtils.isNotEmpty(tpn)) {
						Object value = record.get(propertyName);
						sRecord.put(tpn, value);
						propertyMap.put(propertyName, tpn);
					}
				}
			}
		}
		
		JdbcRecordOperation sOperation = new JdbcRecordOperation(table, sRecord, operation.getJdbcContext());
		operation.setSubstitute(sOperation,propertyMap);
		
		return sOperation;
	}
}
