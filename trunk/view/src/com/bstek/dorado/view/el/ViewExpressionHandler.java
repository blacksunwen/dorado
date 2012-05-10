package com.bstek.dorado.view.el;

import java.util.List;

import com.bstek.dorado.core.el.DefaultExpressionHandler;
import com.bstek.dorado.core.el.EvaluateMode;
import com.bstek.dorado.core.el.Expression;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 5, 2009
 */
public class ViewExpressionHandler extends DefaultExpressionHandler {

	@Override
	protected Expression createExpression(List<Object> sections,
			EvaluateMode evaluateMode) {
		boolean hasOutputableExpression = false;
		for (Object section : sections) {
			if (section == null) {
				continue;
			}
			if (section instanceof org.apache.commons.jexl2.Expression) {
				String expression = ((org.apache.commons.jexl2.Expression) section)
						.getExpression();
				if (expression.startsWith("this.") || expression.equals("this")) {
					hasOutputableExpression = true;
					break;
				}
			}
		}

		if (hasOutputableExpression) {
			Expression expression;
			if (sections.size() == 1) {
				expression = new SingleExpression(
						(org.apache.commons.jexl2.Expression) sections.get(0),
						evaluateMode);

			} else {
				expression = new CombinedExpression(sections, evaluateMode);
			}
			return expression;
		} else {
			return super.createExpression(sections, evaluateMode);
		}
	}
}
