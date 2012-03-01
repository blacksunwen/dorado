package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public class SimpleFilterCriterionParser implements FilterCriterionParser {

	public FilterCriterion createFilterCriterion(String property,
			DataType dataType, String expression) throws Exception {
		SimpleFilterCriterion filterCriterion = new SimpleFilterCriterion();
		filterCriterion.setProperty(property);
		filterCriterion.setDataType(dataType);
		filterCriterion.setExpression(expression);
		filterCriterion.setValue((dataType != null) ? dataType
				.fromText(expression) : expression);
		return filterCriterion;
	}

}
