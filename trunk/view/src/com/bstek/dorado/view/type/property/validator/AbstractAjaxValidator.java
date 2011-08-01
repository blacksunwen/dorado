/**
 * 
 */
package com.bstek.dorado.view.type.property.validator;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-25
 */
public abstract class AbstractAjaxValidator extends AbstractValidator {
	private boolean async = true;
	private String executingMessage;

	@ViewAttribute(defaultValue = "true")
	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public String getExecutingMessage() {
		return executingMessage;
	}

	public void setExecutingMessage(String executingMessage) {
		this.executingMessage = executingMessage;
	}
}
