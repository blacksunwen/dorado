package com.bstek.dorado.data.provider.filter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public class AdvanceFilterCriterionParser implements FilterCriterionParser {

	private List<CriterionProcessor> criterionProcessors;
	
	public List<CriterionProcessor> getCriterionProcessors() {
		return criterionProcessors;
	}

	public void setCriterionProcessors(List<CriterionProcessor> criterionProcessors) {
		this.criterionProcessors = criterionProcessors;
	}

	public FilterCriterion createFilterCriterion(String property,
			DataType dataType, String expression) throws Exception {
		if (StringUtils.isEmpty(expression)) {
			return null;
		}
		
		SingleValueFilterCriterion filterCriterion = new SingleValueFilterCriterion();
		filterCriterion.setProperty(property);
		filterCriterion.setDataType(dataType);
		filterCriterion.setExpression(expression);
		
		for (CriterionProcessor processor: criterionProcessors) {
			processor.doProcess(filterCriterion);
			if (filterCriterion.getFilterOperator() != null) {
				return filterCriterion;
			}
		}

		throw new IllegalArgumentException("Unsupported expression [" + expression + "]");
	}
}
