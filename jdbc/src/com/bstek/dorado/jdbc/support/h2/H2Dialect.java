package com.bstek.dorado.jdbc.support.h2;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.autotable.AutoTableColumn;
import com.bstek.dorado.jdbc.model.autotable.FromTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.BothSpace;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.NullsModel;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderModel;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.support.AbstractDialect;
import com.bstek.dorado.util.Assert;

public class H2Dialect extends AbstractDialect {

	public boolean isNarrowSupport() {
		return true;
	}
	
	public String narrowSql(SelectSql selectSql, int maxResults, int firstResult) {
		String sql = this.toSQL(selectSql);
		
		if (firstResult <= 0) {
			return sql + " limit " + maxResults;
		} else {
			return sql + " limit " + maxResults + " offset " + firstResult;
		}
	}
	
	public boolean isSequenceSupport() {
		return true;
	}
	public String sequenceSql(String sequenceName) {
		Assert.notNull(sequenceName, "'sequenceName' can not be empty.");
		return "SELECT NEXT VALUE FOR " + sequenceName;
	}
	
	@Override
	public String joinToken(JoinModel joinModel, FromTable leftFromTable,
			String[] leftColumnNames, FromTable rightFromTable,
			String[] rightColumnNames) {
		int leng = leftColumnNames.length;
		StringBuilder token = new StringBuilder();
		String leftTableAlias = leftFromTable.getTableAlias();
		String rightTableAlias = rightFromTable.getTableAlias();
		
		Table leftTable = leftFromTable.getTable();
		Table rightTable = rightFromTable.getTable();
		
		String tl = SqlUtils.token(leftFromTable);
		String tr = SqlUtils.token(rightFromTable);
		String jm = token(joinModel);
		
		token.append(tl).append(" ").append(jm).append(" ").append(tr);
		token.append(BothSpace.ON);
		for (int i=0; i<leng; i++) {
			if (i>0) {
				token.append(BothSpace.AND);
			}
			
			String leftColumnName = leftColumnNames[i];
			String rightColumnName = rightColumnNames[i];
			Column leftColumn = leftTable.getColumn(leftColumnName);
			Column rightColumn = rightTable.getColumn(rightColumnName);
			
			token.append(leftTableAlias + "." + leftColumn.getColumnName());
			token.append(" = ");
			token.append(rightTableAlias + "." + rightColumn.getColumnName());
		}
		
		return token.toString();
	}

	protected String token(JoinModel joinModel) {
		switch (joinModel) {
		case LEFT_JOIN:
			return "LEFT JOIN";
		case RIGHT_JOIN:
			return "RIGHT JOIN";
		case INNER_JOIN:
			return "INNER JOIN";
		}
		
		throw new IllegalArgumentException("unknown JoinModel '" + joinModel + "'");
	}

	@Override
	public String orderToken(Order order) {
		OrderModel model = order.getOrderModel();
		NullsModel nullsModel = order.getNullsModel(); 
		
		StringBuilder token = new StringBuilder();
		FromTable fromTable = order.getFromTable();
		if (fromTable == null) {
			AutoTableColumn ac = order.getSelfColumn();
			String columnAlias = ac.getColumnAlias();
			if (StringUtils.isNotEmpty(columnAlias)) {
				token.append(columnAlias);
			} else {
				token.append(ac.getColumnName());	
			}
		} else {
			String cn = order.getColumnName();
			Column fromColumn = fromTable.getTable().getColumn(cn);
			token.append(fromTable.getTableAlias()).append('.').append(fromColumn.getColumnName());
		}
		
		if (model != null) {
			token.append(' ');
			token.append(model.toString());
		}
		if (nullsModel != null) {
			token.append(' ');
			String nullsModelToken = nullsModelToken(nullsModel);
			token.append(nullsModelToken);
		}
		
		return token.toString();
	}
	
	protected String nullsModelToken(NullsModel nullsModel) {
		switch(nullsModel) {
		case NULLS_FIRST:
			return "NULLS FIRST";
		case NULLS_LAST:
			return "NULLS LAST";
		}
		
		return "";
	}
}
