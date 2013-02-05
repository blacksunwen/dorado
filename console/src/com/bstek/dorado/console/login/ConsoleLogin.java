package com.bstek.dorado.console.login;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆接口
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public interface ConsoleLogin {


	
	/**
	 * 验证用户名密码是否正确
	 * 
	 * @param 
	 * @return
	 */
	boolean validate(String name, String password);

	/**
	 * 当前操作人是否成功登陆
	 * 
	 * @return
	 */
	boolean isLogin(HttpServletRequest request);
}
