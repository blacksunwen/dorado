package com.bstek.dorado.common.event;

/**
 * 默认的客户端事件监听器实现。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class DefaultClientEvent implements ClientEvent {
	private String signature;
	private String script;

	/**
	 * 构造器。
	 */
	public DefaultClientEvent() {
	}

	/**
	 * 构造器。
	 */
	public DefaultClientEvent(String script) {
		setScript(script);
	}

	/**
	 * 构造器。
	 */
	public DefaultClientEvent(String signature, String script) {
		setSignature(signature);
		setScript(script);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 设置监听器的JavaScript脚本。
	 */
	public void setScript(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}

}
