package com.bstek.dorado.sample.basic;

import java.io.Writer;

import org.springframework.stereotype.Component;

@Component
public class VelocityInterceptor {
	public void outputText(Writer writer) throws Exception {
		writer.append("This line is outputted by VelocityInterceptor.");
	}
}
