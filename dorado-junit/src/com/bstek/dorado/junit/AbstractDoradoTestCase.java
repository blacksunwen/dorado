package com.bstek.dorado.junit;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.w3c.dom.Document;

import com.bstek.dorado.config.xml.XmlParserHelper;
import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.EngineStartupListenerManager;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.xml.XmlDocumentBuilder;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.manager.DataTypeManager;

public abstract class AbstractDoradoTestCase extends TestCase {
	
	private final String[] baseConfigLocation = new String[]{
			"com/bstek/dorado/core/context.xml",
			"com/bstek/dorado/config/context.xml",
			"com/bstek/dorado/common/context.xml",
			"com/bstek/dorado/data/context.xml",
			"com/bstek/dorado/idesupport/context.xml",
			"com/bstek/dorado/view/context.xml",
			"com/bstek/dorado/web/context.xml",
			"com/bstek/dorado/junit/test-context.xml"
		};

	private class TestContext extends CommonContext {
		private ApplicationContext applicationContext;
		
		TestContext()  {
			super();
			
			try {
				Context.attachToThreadLocal(this);
				
				this.initApplicationContext();
				beforeStartupListener();
				EngineStartupListenerManager.notifyStartup();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public ApplicationContext getApplicationContext() throws Exception {
			if (applicationContext == null) {
				applicationContext = createApplicationContext();
			}
			return applicationContext;
		}
		
		public void close() throws Exception{
			try {
				ConfigurableApplicationContext configContext = (ConfigurableApplicationContext)applicationContext;
				configContext.close();
			} finally {
				Context.dettachFromThreadLocal();
			}
		}
	}
	
	protected void beforeStartupListener() {
		ConfigureStore store = Configure.getStore();
		store.set("core.runMode", "debug");
	}
	
	private TestContext testContext;
	
	@Override
	protected void setUp() throws Exception {
		TestUtils.setCurrentCaseClass(getClass());
		try {
			super.setUp();
			this.initContextConfigLocation();
			testContext = new TestContext();
			this.beforeCaseRun();
		} finally {
			TestUtils.setCurrentCaseClass(null);
		}
	}
	
	/**
	 * 在每一个TestCase运行前触发的方法
	 * @throws Exception
	 */
	protected void beforeCaseRun() throws Exception {
		
	}

	@Override
	protected void tearDown() throws Exception {
		this.afterCaseRun();
		testContext.close();
		super.tearDown();
		
		System.gc();
		Thread.sleep(1 * 1000);
	}
	
	/**
	 * 在每一个TestCase运行后触发的方法
	 * @throws Exception
	 */
	protected void afterCaseRun() throws Exception {
		
	}
	
	void initContextConfigLocation() {
		List<String> locationList = new ArrayList<String>();
		
		//加载基本的配置文件
		for (String location: baseConfigLocation) {
			locationList.add(location);
		}
		
		//加载Addon的配置文件
		String[] addonsLocation = getAddonsConfigLocation();
		if (addonsLocation != null) {
			for (String location: addonsLocation) {
				locationList.add(location);
			}
		}
		
		//加载Case的配置文件
		String caseLocation = getCaseConfigLocation();
		if (caseLocation != null) {
			locationList.add(caseLocation);
		}
		
		String locationStr = StringUtils.join(locationList, ';');
		Configure.getStore().set("core.contextConfigLocation", locationStr);
	}
	
	/**
	 * Addon的配置文件
	 */
	protected String[] getAddonsConfigLocation() {
		return null;
	}
	
	/**
	 * Case的配置文件
	 * @return
	 */
	String getCaseConfigLocation() {
		String className = this.getClass().getName();
		String [] tokens = StringUtils.split(className, '.');
		tokens[tokens.length-1] = "context.xml";
		String location = StringUtils.join(tokens, '/');
		
		InputStream in = this.getClass().getResourceAsStream(location);
		if (in != null) {
			try {
				return location;
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return null;
		}
	}
	
	protected Resource getCaseResource(String resourceName)  {
		String className = this.getClass().getName();
		String [] tokens = StringUtils.split(className, '.');
		tokens[tokens.length-1] = resourceName;
		
		String location = "classpath:" + StringUtils.join(tokens, '/');
		try {
			return ResourceUtils.getResource(location);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * ------------------- Object Getter -------------------
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getServiceBean(String serviceName) {
		Context ctx = Context.getCurrent();
		try {
			return (T)ctx.getServiceBean(serviceName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected XmlParserHelper getXmlParserHelper() {
		XmlParserHelper helper = this.getServiceBean("xmlParserHelper");
		return helper;
	}
	
	protected XmlDocumentBuilder getXmlDocumentBuilder() {
		XmlDocumentBuilder builder = this.getServiceBean("xmlDocumentBuilder");
		return builder;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends DataType> T getDataType(String name) throws Exception {
		String singleName = name;
		boolean agg = false;
		if (name.charAt(0) == '[') {
			agg = true;
			singleName = singleName.substring(1, singleName.length()-1);
		}
		
		DataType elementDataType = this.getDataTypeManager().getDataType(singleName);
		if (agg) {
			AggregationDataType aggDataType = new AggregationDataType();
			aggDataType.setElementDataType(elementDataType);
			return (T)aggDataType;
		} else {
			return (T)elementDataType;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends DataProvider> T getProvider(String name) throws Exception {
		DataProvider provider = this.getDataProviderManager().getDataProvider(name);
		Assert.assertNotNull("no provider named [" + name + "] defined.", provider);
		
		return (T)provider;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends DataResolver> T getResolver(String name) throws Exception {
		DataResolver resolver = this.getDataResolverManager().getDataResolver(name);
		Assert.assertNotNull("no resolver named [" + name + "] defined.", resolver);
		
		return (T)resolver;
	}
	
	/*
	 * ------------------- Managers -------------------
	 */
	protected DataTypeManager getDataTypeManager() throws Exception {
		return (DataTypeManager) getServiceBean("dataTypeManager");
	}
	protected DataProviderManager getDataProviderManager() throws Exception {
		DataProviderManager dataProviderManager = getServiceBean("dataProviderManager");
		return dataProviderManager;
	}
	protected DataResolverManager getDataResolverManager() throws Exception {
		DataResolverManager dataResolverManager = getServiceBean("dataResolverManager");
		return dataResolverManager;
	}
	
	/*
	 * ------------------- Utils -------------------
	 */
	protected String toString(Document document) throws Exception {
		StringWriter writer = new StringWriter();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");  
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return writer.toString();
	}
}
