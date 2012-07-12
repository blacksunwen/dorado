package com.bstek.dorado.sample.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.core.DoradoAbout;

@Component
public class Ajax {

	@Expose
	public String toUpperCase(String str) {
		return "input:\n" + str + "\n\n" + "output:\n" + str.toUpperCase();
	}

	@Expose
	public int multiply(int num1, int num2) {
		return num1 * num2;
	}

	@Expose
	public int multiply2(List<Integer> nums) {
		int result = 1;
		for (int num : nums) {
			result *= num;
		}
		return result;
	}

	@Expose
	public void errorAction() {
		System.out.println(100 / 0);
	}

	@Expose
	public Properties getSystemInfo() {
		Properties info = new Properties();
		info.setProperty("product", DoradoAbout.getProductTitle());
		info.setProperty("vendor", DoradoAbout.getVendor());
		info.setProperty("version", DoradoAbout.getVersion());
		return info;
	}

	@Expose
	public Map<String, Long> getMemInfo() {
		Map<String, Long> info = new HashMap<String, Long>();
		Runtime runtime = Runtime.getRuntime();
		info.put("freeMemory", runtime.freeMemory());
		info.put("totalMemory", runtime.totalMemory());
		return info;
	}
}
