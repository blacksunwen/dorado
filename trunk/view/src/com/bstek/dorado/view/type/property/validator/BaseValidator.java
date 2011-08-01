/**
 * 
 */
package com.bstek.dorado.view.type.property.validator;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-3
 */
public abstract class BaseValidator extends AbstractValidator {
	private String resultMessage;

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

}
