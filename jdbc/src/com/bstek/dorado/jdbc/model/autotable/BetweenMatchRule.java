package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AutoTable;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.sql.SqlConstants;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode (
	nodeName = "Between"
)
public class BetweenMatchRule extends AbstractSingleColumnMatchRule {

	private Object value1;
	private Object value2;
	private String availableStrategy = "any";
	
	@XmlProperty
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	
	@XmlProperty
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}
	
	@IdeProperty(enumValues="any,both,value1,value2")
	@ClientProperty(escapeValue="any")
	public String getAvailableStrategy() {
		return availableStrategy;
	}
	public void setAvailableStrategy(String availableStrategy) {
		this.availableStrategy = availableStrategy;
	}
	
	@Override
	public String token(AutoTable autoTable, JdbcParameterSource parameterSource) {
		if (!this.isAvailable()) {
			return null;
		}
		
		String fromColumnName = this.getColumn();
		String fromTableName = this.getFromTable();
		
		FromTable fromTable = autoTable.getFromTable(fromTableName);
		Table table = fromTable.getTableObject();
		
		AbstractDbColumn column = table.getColumn(fromColumnName);
		
		String columnName = column.getName();
		
		Value value1Object = this.parseValue(parameterSource, value1);
		Value value2Object = this.parseValue(parameterSource, value2);
		
		SqlConstants.Operator sqlOperator = SqlConstants.Operator.between;
		
		String value1Token = value1Object.token(column.getJdbcType(), sqlOperator);
		String value2Token = value2Object.token(column.getJdbcType(), sqlOperator);
		
		if (value1Token != null && value2Token != null) {
			String opToken = this.isNot() ? sqlOperator.notSQL(): sqlOperator.toSQL();
			return fromTableName + "." + columnName + " " + opToken + " " + value1Token + " AND " + value2Token + "";
		} else 
		if (value1Token != null && ("any".equals(availableStrategy) || "value1".equals(availableStrategy))) {
			sqlOperator = SqlConstants.Operator.ge;
			String opToken = this.isNot() ? sqlOperator.notSQL(): sqlOperator.toSQL();
			
			return fromTableName + "." + columnName + " " + opToken + " " + value1Token;
		} else
		if (value2Token != null && ("any".equals(availableStrategy) || "value2".equals(availableStrategy))) {
			sqlOperator = SqlConstants.Operator.le;
			String opToken = this.isNot() ? sqlOperator.notSQL(): sqlOperator.toSQL();
			
			return fromTableName + "." + columnName + " " + opToken + " " + value2Token;
		}
		
		return null;
	}
	
}
