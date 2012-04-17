package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

public class DefaultCriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		DataType dataType = criterion.getDataType();
		FilterOperator filterOperator = null;
		
		String valueStr = expression.trim();
		Object filterValue = (dataType != null) ? dataType.fromText(valueStr) : valueStr;
		if (filterValue instanceof String) {
			filterOperator = FilterOperator.like;
		} else {
			filterOperator = FilterOperator.eq;
		}
		
		criterion.setFilterOperator(filterOperator);
		criterion.setValue(filterValue);
	}

}
