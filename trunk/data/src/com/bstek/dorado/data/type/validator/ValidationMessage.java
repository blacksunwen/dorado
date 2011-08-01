/**
 * 
 */
package com.bstek.dorado.data.type.validator;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-25
 */
public class ValidationMessage {
	private MessageState state = MessageState.error;
	private String text;

	public ValidationMessage() {
	}

	public ValidationMessage(String text) {
		this.text = text;
	}

	public ValidationMessage(MessageState state, String text) {
		this(text);
		this.state = state;
	}

	public MessageState getState() {
		return state;
	}

	public void setState(MessageState state) {
		this.state = state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
