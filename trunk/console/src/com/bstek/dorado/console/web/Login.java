package com.bstek.dorado.console.web;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.console.ConsoleLogin;
import com.bstek.dorado.console.ConsoleUtils;
import com.bstek.dorado.view.View;

public class Login {

	@Expose
	public boolean login(String inputToken) {
		ConsoleLogin cl = ConsoleUtils.getConsoleLogin();
		
		return cl.validate(inputToken);
	}
	
	public void onViewInit(View view) {
		ConsoleLogin cl = ConsoleUtils.getConsoleLogin();
		cl.onView();
	}
}
