package com.bstek.dorado.data.provider.filter;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public class AdvanceFilterCriterionParser implements FilterCriterionParser {

	public FilterCriterion createFilterCriterion(String property,
			DataType dataType, String expression) throws Exception {
		FilterOperator op = null;
		int len = expression.length(), start = 0;
		if (expression != null && len > 1) {
			char c = expression.charAt(0);
			if (c == '=') {
				op = FilterOperator.eq;
				start = 1;
			} else if (c == '<' || c == '>') {
				if (len > 1) {
					char c2 = expression.charAt(1);
					if (c2 != c) {
						if (c2 == '=') {
							op = (c == '<') ? FilterOperator.le
									: FilterOperator.ge;
							start = 2;
						} else if (c2 == '<' || c2 == '>') {
							op = FilterOperator.ne;
							start = 2;
						}
					}
				}
				if (op == null) {
					op = (c == '<') ? FilterOperator.lt : FilterOperator.gt;
					start = 1;
				}
			} else if (c == 'l' && expression.startsWith("like ")) {
				op = FilterOperator.like;
				start = 5;
			}
		}

		String valueExpression = expression.substring(start);
		valueExpression = StringUtils.trim(valueExpression);

		SingleValueFilterCriterion filterCriterion = new SingleValueFilterCriterion();
		filterCriterion.setProperty(property);
		filterCriterion.setDataType(dataType);
		filterCriterion.setExpression(expression);
		filterCriterion.setFilterOperator(op);
		filterCriterion.setValue((dataType != null) ? dataType
				.fromText(valueExpression) : valueExpression);
		return filterCriterion;
	}
}
