package com.bstek.dorado.vidorsupport;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.junit.AbstractDoradoTestCase;

public abstract class AbstractCloudoTestCase extends AbstractDoradoTestCase {
	
	public AbstractCloudoTestCase() {
		super();
		this.contextScope = ContextScope.Global;
	}

	@Override
	protected String[] getAddonsConfigLocation() {
		return new String[]{
			"classpath:com/bstek/dorado/vidorsupport/context.xml"
		};
	}
	
	protected void assertJsonEquals(String actual, String expected) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String actualJson = objectMapper.readTree(actual).toString();
		String expectedJson = objectMapper.readTree(expected).toString();
		
		Assert.assertEquals(expectedJson, actualJson);
	}
	
	protected String read(String resourcePath) throws Exception {
		Context context = Context.getCurrent();
		Resource res = context.getResource(resourcePath);
		
		String script = CloudoTestHelper.toString(res);
		return script;
	}
	
}
