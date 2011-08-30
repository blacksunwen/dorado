package com.bstek.dorado.sample.data;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;

@Component
@Expose
public class Validators {
	private static final String VALID_ACCOUNT = "Dorado";

	public String checkAccountName(String parameter)
			throws InterruptedException {
		Thread.sleep(500);
		if (VALID_ACCOUNT.equals(parameter)) {
			return null;
		} else {
			return "Invalid User Name, Please try \"" + VALID_ACCOUNT + "\".";
		}
	}
}
