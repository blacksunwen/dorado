package com.bstek.dorado.sample.basic;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.DoradoAbout;

@Component
public class Ajax {

	@Expose
	public String toUpperCase(String parameter) {
		return "input:\n" + parameter + "\n\n" + "output:\n"
				+ parameter.toUpperCase();
	}

	@Expose
	public int multiply(int num1, int num2) {
		return num1 * num2;
	}

	@Expose
	public Properties getSystemInfo() {
		Properties info = new Properties();
		info.setProperty("product", DoradoAbout.getProductTitle());
		info.setProperty("vendor", DoradoAbout.getVendor());
		info.setProperty("version", DoradoAbout.getVersion());
		return info;
	}
}
