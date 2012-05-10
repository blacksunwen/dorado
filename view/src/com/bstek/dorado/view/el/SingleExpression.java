package com.bstek.dorado.view.el;

import org.apache.commons.jexl2.Expression;

import com.bstek.dorado.core.el.EvaluateMode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 5, 2009
 */
public class SingleExpression extends com.bstek.dorado.core.el.SingleExpression
		implements PrevaluateExpression {

	public SingleExpression(Expression expression) {
		super(expression);
	}

	public SingleExpression(Expression expression, EvaluateMode evaluateMode) {
		super(expression, evaluateMode);
	}

	@Override
	protected Object internalEvaluate() {
		if (this.getEvaluateMode() == EvaluateMode.onRead
				&& OutputableExpressionUtils.isOutputableExpressionDisabled()) {
			OutputableExpressionUtils.setSkipedExpression(this);
			return null;
		} else {
			return super.internalEvaluate();
		}
	}

	public Object prevaluate() {
		return getExpression().getExpression();
	}

}
