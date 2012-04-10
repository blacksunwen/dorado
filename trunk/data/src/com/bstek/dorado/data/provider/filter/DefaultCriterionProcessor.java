package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

public class DefaultCriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		if (expression.length() > 1) {
			DataType dataType = criterion.getDataType();
			FilterOperator filterOperator = null;
			
			Object filterValue = (dataType != null) ? dataType.fromText(expression) : expression;
			if (filterValue instanceof String) {
				filterOperator = FilterOperator.like;
			} else {
				filterOperator = FilterOperator.eq;
			}
			
			criterion.setFilterOperator(filterOperator);
			criterion.setValue(filterValue);
		}
	}

}
