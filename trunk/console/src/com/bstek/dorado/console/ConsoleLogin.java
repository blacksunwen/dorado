package com.bstek.dorado.console;

/**
 * 登陆接口
 * @author mark.li@bstek.com
 *
 */
public interface ConsoleLogin {

	/**
	 * 用户访问登陆页面的回调方法
	 */
	void onView();
	
	/**
	 * 获取系统认证码
	 * @return
	 */
	String getToken();
	
	/**
	 * 验证用户输入的认证码是否正确
	 * @param inputToken
	 * @return
	 */
	boolean validate(String inputToken);
	
	/**
	 * 当前操作人是否成功登陆
	 * @return
	 */
	boolean isLogin();
}
