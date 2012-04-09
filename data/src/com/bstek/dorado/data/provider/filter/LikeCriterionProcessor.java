package com.bstek.dorado.data.provider.filter;

public class LikeCriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		
		if (expression.charAt(0)=='%' || expression.charAt(expression.length()-1)=='%') {
			criterion.setFilterOperator(FilterOperator.like);
			criterion.setValue(expression);
		}
	}

}
