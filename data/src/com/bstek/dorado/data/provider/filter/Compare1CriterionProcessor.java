package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

public class Compare1CriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		DataType dataType = criterion.getDataType();
		FilterOperator filterOperator = null;
		
		if (expression.length() > 1) {
			char opChar = expression.charAt(0);
			if ('>' == opChar) {
				filterOperator = FilterOperator.gt;
			} else if ('<' == opChar) {
				filterOperator = FilterOperator.lt;
			} else if ('=' == opChar) {
				filterOperator = FilterOperator.eq;
			} 
			
			if (filterOperator != null) {
				criterion.setFilterOperator(filterOperator);
				criterion.setValue((dataType != null) ? dataType.fromText(expression.substring(1)) : expression);
			}
		}
	}

}
