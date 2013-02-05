package com.bstek.dorado.console.login;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.console.Constants;
import com.bstek.dorado.console.Setting;
import com.bstek.dorado.view.View;
import com.bstek.dorado.web.DoradoContext;

/**
 * 登陆服务接口
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2012-12-24
 * 
 */
public class Login {

	@Expose
	public boolean login(String name, String password) {
		ConsoleLogin cl = Setting.getConsoleLogin();
		return cl.validate(name, password);
	}

	public void onViewInit(View view) {
	}

	@Expose
	public void logout() {
		DoradoContext.getCurrent().removeAttribute(DoradoContext.SESSION,
				Constants.S_DORADO_CONSOLE_LOGIN_SUCCESS);
	}
}
