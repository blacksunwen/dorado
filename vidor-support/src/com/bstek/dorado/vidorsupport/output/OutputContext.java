package com.bstek.dorado.vidorsupport.output;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

import com.bstek.dorado.core.Configure;

public class OutputContext {

	private Writer writer;
	private boolean usePrettyJson;
	private ObjectMapper objectMapper;
	private JsonGenerator jsonGenerator;

	public OutputContext() {
		this(new StringWriter());
	}
	
	public OutputContext(Writer writer) {
		this.writer = writer;
		objectMapper = new ObjectMapper();
		setUsePrettyJson(Configure.getBoolean("view.outputPrettyJson"));
	}

	public Writer getWriter() {
		return writer;
	}

	public boolean isUsePrettyJson() {
		return usePrettyJson;
	}
	public void setUsePrettyJson(boolean usePrettyJson) {
		this.usePrettyJson = usePrettyJson;
	}
	
	public JsonGenerator getJsonGenerator() {
		if (jsonGenerator == null) {
			try {
				jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(getWriter());
				jsonGenerator.configure(Feature.ESCAPE_NON_ASCII, true);
				if (this.isUsePrettyJson()) {
					jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return jsonGenerator;
	}

}
