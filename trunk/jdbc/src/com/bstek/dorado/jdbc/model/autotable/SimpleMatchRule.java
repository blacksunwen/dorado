package com.bstek.dorado.jdbc.model.autotable;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AutoTable;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.sql.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlConstants;

@XmlNode (
	nodeName = "Rule"
)
public class SimpleMatchRule extends AbstractSingleColumnMatchRule {

	private String operator;
	private Object value;

	@IdeProperty(enumValues="=,<>,>,<,>=,<=,in,like,like%,%like,%like%")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@XmlProperty
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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
		Object value = this.getValue();
		String operator = this.getOperator();
		
		SqlConstants.Operator sqlOperator = SqlConstants.Operator.value(operator.trim());
		String opToken = this.isNot() ? sqlOperator.notSQL(): sqlOperator.toSQL();
		
		Value valueObject = this.parseValue(parameterSource, value);
		String valueToken = valueObject.token(column.getJdbcType(), sqlOperator);
		if (StringUtils.isNotBlank(valueToken)) {
			return fromTableName + "." + columnName + " " + opToken + " " + valueToken;
		}
		
		return null;
	}

}
