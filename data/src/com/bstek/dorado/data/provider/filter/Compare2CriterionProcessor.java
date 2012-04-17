package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

public class Compare2CriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		DataType dataType = criterion.getDataType();
		FilterOperator filterOperator = null;
		
		if (expression.length() > 2) {
			String opStr = expression.substring(0, 2);
			if (">=".equals(opStr)) {
				filterOperator = FilterOperator.ge;
			} else if ("<=".equals(opStr)) {
				filterOperator = FilterOperator.le;
			} else if ("<>".equals(opStr)) {
				filterOperator = FilterOperator.ne;
			}
			
			if (filterOperator != null) {
				criterion.setFilterOperator(filterOperator);
				String valueStr = expression.substring(2).trim();
				criterion.setValue((dataType != null) ? dataType.fromText(valueStr) : valueStr);
			}
		}
	}

}
