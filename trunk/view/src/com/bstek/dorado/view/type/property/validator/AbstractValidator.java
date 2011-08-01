package com.bstek.dorado.view.type.property.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.data.type.validator.MessageState;
import com.bstek.dorado.data.type.validator.ValidationMessage;
import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.view.RunAt;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public abstract class AbstractValidator implements Validator {
	private RunAt runAt = RunAt.client;
	private MessageState defaultResultState = MessageState.error;

	@ViewAttribute(defaultValue = "client")
	public RunAt getRunAt() {
		return runAt;
	}

	public void setRunAt(RunAt runAt) {
		this.runAt = runAt;
	}

	@ViewAttribute(defaultValue = "error")
	public MessageState getDefaultResultState() {
		return defaultResultState;
	}

	public void setDefaultResultState(MessageState defaultResultState) {
		this.defaultResultState = defaultResultState;
	}

	private ValidationMessage trimSingleMessage(Object message) {
		if (message instanceof ValidationMessage) {
			return (ValidationMessage) message;
		} else {
			return new ValidationMessage(String.valueOf(message));
		}
	}

	public final List<ValidationMessage> validate(Object value)
			throws Exception {
		if (runAt != RunAt.client) {
			Object result = doValidate(value);
			if (result != null) {
				List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
				if (result instanceof Collection) {
					for (Object message : (Collection<?>) result) {
						messages.add(trimSingleMessage(message));
					}
				} else {
					messages.add(trimSingleMessage(result));
				}
				return messages;
			}
		}
		return null;
	}

	protected abstract Object doValidate(Object value) throws Exception;
}
