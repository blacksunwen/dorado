package com.bstek.dorado.vidorsupport.rule;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import com.bstek.dorado.vidorsupport.output.AbstractField;
import com.bstek.dorado.vidorsupport.output.BooleanField;
import com.bstek.dorado.vidorsupport.output.IOutputFiledsable;
import com.bstek.dorado.vidorsupport.output.IOutputable;
import com.bstek.dorado.vidorsupport.output.OutputContext;

public class ClientEvent implements IOutputable<OutputContext>, IOutputFiledsable, IOriginalObjectHolder<com.bstek.dorado.idesupport.model.ClientEvent> {
	/**
	 * "事件"名称
	 */
	private String name;
	/**
	 * 是否是不推荐的"事件"
	 */
	private BooleanField deprecated = new BooleanField("deprecated", false);
	
	private com.bstek.dorado.idesupport.model.ClientEvent originalObject;
	
	public ClientEvent(){
		super();
	}
	public ClientEvent(com.bstek.dorado.idesupport.model.ClientEvent event) {
		this();
		
		this.name = event.getName();
		this.deprecated.setValue(event.isDeprecated());
	}

	
	public com.bstek.dorado.idesupport.model.ClientEvent getOriginalObject() {
		return originalObject;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeprecated() {
		return deprecated.getValue();
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated.setValue(deprecated);
	}
	
	public AbstractField<?>[] getOutputFilelds() {
		AbstractField<?>[] fields = new AbstractField[]{
			deprecated
		};
		return fields;
	}


	public void output(OutputContext context) {
		if (deprecated.getValue()) {
			JsonGenerator jsonGenerator = context.getJsonGenerator();
			try {
				jsonGenerator.writeStartObject();
				jsonGenerator.writeFieldName(name);
				jsonGenerator.writeStartObject();
				AbstractField<?>[] fields = this.getOutputFilelds();
				for (AbstractField<?> field: fields) {
					if(field.shouldOutput()) {
						field.output(context);
					}
				}
				jsonGenerator.writeEndObject();
				jsonGenerator.writeEndObject();
			} catch (JsonGenerationException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
