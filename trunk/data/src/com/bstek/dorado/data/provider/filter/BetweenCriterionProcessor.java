package com.bstek.dorado.data.provider.filter;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.util.Assert;

public class BetweenCriterionProcessor implements CriterionProcessor {

	public void doProcess(SingleValueFilterCriterion criterion) {
		String expression = criterion.getExpression();
		DataType dataType = criterion.getDataType();
		
		if (expression.length() > 1 && '[' == expression.charAt(0)) {
			String valueStr = (expression.charAt(expression.length()-1)==']') ? 
					expression.substring(1, expression.length()-1) : expression.substring(1);
			
			String[] valueStrAry = StringUtils.split(valueStr, ',');
			Assert.isTrue(valueStrAry.length == 2, "[" + valueStr + "] must contains two values.");
			
			Object[] valueAry = new Object[valueStrAry.length];
			for (int i=0; i<valueStrAry.length; i++) {
				valueStr = valueStrAry[i];
				valueAry[i] = (dataType != null) ? dataType.fromText(valueStr) : valueStr;
			}
			
			criterion.setFilterOperator(FilterOperator.between);
			criterion.setValue(valueAry);
		}
	}

}
