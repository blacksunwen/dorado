package com.bstek.dorado.console;

/**
 * 禁止一切人员登陆Console
 * @author mark.li@bstek.com
 *
 */
public class ForbinddanceConsoleLogin implements ConsoleLogin {

	public String getToken() {
		return "Forbinddance";
	}

	public boolean validate(String inputToken) {
		return false;
	}

	public boolean isLogin() {
		return false;
	}

	public void onView() {
		
	}

}
