package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public interface FilterCriterionParser {
	FilterCriterion createFilterCriterion(String property, DataType dataType,
			String expression) throws Exception;
}
