package com.bstek.dorado.mock;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.bstek.dorado.data.config.DataConfigLoader;
import com.bstek.dorado.junit.TestUtils;

public class MockDataConfigLoader extends DataConfigLoader {
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Class<?> testClazz = TestUtils.getCurrentClassClass();
		String location = this.getDataLocation(testClazz);
		if (location != null) {
			this.setConfigLocation(location);
		}
		
		super.afterPropertiesSet();
	}

	String getDataLocation(Class<?> clazz) throws IOException {
		String[] tokens = StringUtils.split(clazz.getName(), '.');
		
		String[] pkgTokens = new String[tokens.length];
		System.arraycopy(tokens, 0, pkgTokens, 0, tokens.length-1);
		pkgTokens[pkgTokens.length-1] = "*.model.xml";
		
		String modelClassPath = StringUtils.join(pkgTokens, '/');
		
		String locationExpr = "classpath:" + modelClassPath;
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resourcePatternResolver.getResources(locationExpr);
		if (resources.length > 0) {
			return locationExpr;
		} else {
			return null;
		}
	}
}
