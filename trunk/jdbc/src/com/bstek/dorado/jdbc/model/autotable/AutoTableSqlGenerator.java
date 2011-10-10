package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.DbElement.Type;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.SqlConstants.JunctionModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.BothSpace;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.RightSpace;
import com.bstek.dorado.util.Assert;

public class AutoTableSqlGenerator implements SqlGenerator<AutoTable> {

	@Override
	public Type getType() {
		return Type.AutoTable;
	}
	
	@Override
	public SelectSql selectSql(AutoTable t, Object parameter) {
		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<Column> columns = t.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			AutoTableColumn column = (AutoTableColumn)columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				FromTable fromTable = column.getFromTable();
				String tableAlias = fromTable.getTableAlias();
				
				String columnName = column.getColumnName();
				String columnAlias = column.getColumnAlias();
				if (StringUtils.isEmpty(columnAlias)) {
					columnAlias = columnName;
				}
				String token = tableAlias + "." + columnName + BothSpace.AS + columnAlias;
				columnsToken.append(token);
			}
		}
		
		//fromToken
		StringBuilder fromToken = fromToken(t);
		//where
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		StringBuilder whereToken = whereToken(t, p);
		//order
		StringBuilder orderbyToken = orderByToken(t, p);
		
		//--
		AutoTableSelectSql selectSql = new AutoTableSelectSql();
		selectSql.setParameterSource(p);
		selectSql.setColumnsToken(columnsToken.toString());
		selectSql.setFromToken(fromToken.toString());
		selectSql.setWhereToken(whereToken.toString());
		selectSql.setOrderToken(orderbyToken.toString());
		
		return selectSql;
	}

	protected StringBuilder fromToken(AutoTable t) {
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
				
				String token = SqlUtils.token(fromTable);
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
				
				Dialect dialect = JdbcQueryContext.getInstance().getJdbcEnviroment().getDialect();
				FromTable leftFromTable = t.getFromTable(joinTable.getLeftFromTableAlias());
				FromTable rightFromTable = t.getFromTable(joinTable.getRightFromTableAlias());
				String token = dialect.joinToken(joinModel, leftFromTable, leftColumnNames, rightFromTable, rightColumnNames);
				
				if (i > 0) {
					fromToken.append(',');
				}
				fromToken.append(token);
			}
		}
		
		return fromToken;
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
			Column column = bmr.getColumn();
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
					parameterName = p.newValue(value);
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
						return RightSpace.NOT + "(" + token + ")";
					} else {
						return token;
					}
				}
			}
			
			return "";
		}
	}
	
	protected StringBuilder orderByToken(AutoTable t, JdbcParameterSource p) {
		StringBuilder r = new StringBuilder();
		
		List<Order> orders = t.getOrders();
		if (orders != null && !orders.isEmpty()) {
			Dialect dialect = JdbcQueryContext.getInstance().getJdbcEnviroment().getDialect();
			List<String> tokens = new ArrayList<String>(orders.size());
			for (int i=0; i<orders.size(); i++) {
				Order order = orders.get(i);
				String token = dialect.orderToken(order);
				if (StringUtils.isNotEmpty(token)) {
					tokens.add(token);
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

}
