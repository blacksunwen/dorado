package com.bstek.dorado.common.event;

/**
 * 客户端事件的注册信息。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class ClientEventRegisterInfo {

	private static final String[] DEFAULT_SIGNATURE = new String[] { "self",
			"arg" };

	private Class<?> type;
	private String name;
	private String[] signature;

	/**
	 * @param type
	 *            事件宿主的Class类型
	 * @param name
	 *            事件名
	 * @param signature
	 *            事件监听器的方法签名
	 */
	public ClientEventRegisterInfo(Class<?> type, String name,
			String[] signature) {
		this.type = type;
		this.name = name;
		this.signature = (signature != null) ? signature : DEFAULT_SIGNATURE;
	}

	/**
	 * @param type
	 *            事件宿主的Class类型
	 * @param name
	 *            事件名
	 */
	public ClientEventRegisterInfo(Class<?> type, String name) {
		this(type, name, null);
	}

	/**
	 * 返回事件宿主的Class类型。
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * 返回事件名。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 返回事件监听器的方法签名。
	 */
	public String[] getSignature() {
		return signature;
	}
}
