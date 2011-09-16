/**
 * 
 */
package com.bstek.dorado.idesupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.DoradoAbout;
import com.bstek.dorado.core.EngineStartupListenerManager;
import com.bstek.dorado.core.el.DefaultExpressionHandler;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.core.io.BaseResourceLoader;
import com.bstek.dorado.core.io.LocationTransformerHolder;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceLoader;
import com.bstek.dorado.core.io.ResourceUtils;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.web.ConsoleUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-6
 */
public class StandaloneRuleSetExporter {
	private static final Log logger = LogFactory
			.getLog(StandaloneRuleSetExporter.class);

	private static final String HOME_LOCATION_PREFIX = "home:";
	private static final int HOME_LOCATION_PREFIX_LEN = HOME_LOCATION_PREFIX
			.length();
	private static final String HOME_PROPERTY = "core.doradoHome";
	private static final String CONTEXT_CONFIG_PROPERTY = "core.contextConfigLocation";
	private static final String WEB_CONFIGURE_LOCATION = "com/bstek/dorado/web/configure.properties";

	private static final String CONTEXT_FILE_EXT = ".xml";
	private static final String HOME_CONTEXT_PREFIX = HOME_LOCATION_PREFIX
			+ "context";
	private static final String HOME_CONTEXT_XML = HOME_CONTEXT_PREFIX
			+ CONTEXT_FILE_EXT;

	private String doradoHome;

	private StandaloneRuleSetExporter(String doradoHome) {
		this.doradoHome = doradoHome;
	}

	private RuleTemplateBuilder getRuleTemplateBuilder() throws Exception {
		Context context = Context.getCurrent();
		return (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
	}

	private static RuleSetBuilder getRuleSetBuilder() throws Exception {
		Context context = Context.getCurrent();
		return (RuleSetBuilder) context
				.getServiceBean("idesupport.ruleSetBuilder");
	}

	private String getRealResourcePath(String location) {
		if (location != null && location.startsWith(HOME_LOCATION_PREFIX)) {
			location = ResourceUtils.concatPath(doradoHome,
					location.substring(HOME_LOCATION_PREFIX_LEN));
		}
		return location;
	}

	private String[] getRealResourcesPath(List<String> locations)
			throws IOException {
		if (locations == null || locations.isEmpty()) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (String location : locations) {
			location = getRealResourcePath(location);
			if (StringUtils.isNotEmpty(location)) {
				result.add(location);
			}
		}
		return result.toArray(new String[0]);
	}

	private void pushLocation(List<String> locationList, String location) {
		if (StringUtils.isNotEmpty(location)) {
			location = LocationTransformerHolder.transformLocation(location);
			locationList.add(location);
		}
	}

	private void pushLocations(List<String> locationList, String locations) {
		if (StringUtils.isNotEmpty(locations)) {
			for (String location : org.springframework.util.StringUtils
					.tokenizeToStringArray(
							locations,
							ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS)) {
				pushLocation(locationList, location);
			}
		}
	}

	private void loadConfigureProperties(ConfigureStore configureStore,
			ResourceLoader resourceLoader, String configureLocation,
			boolean silence) throws IOException {
		// 装载附加的基本配置信息
		ConsoleUtils.outputLoadingInfo("Loading configure from ["
				+ configureLocation + "]...");
		if (StringUtils.isNotEmpty(configureLocation)) {
			Resource resource = resourceLoader
					.getResource(getRealResourcePath(configureLocation));
			if (!resource.exists()) {
				if (silence) {
					logger.warn("Can not found resource [" + configureLocation
							+ "].");
					return;
				} else {
					throw new IOException("Can not found resource ["
							+ configureLocation + "].");
				}
			}
			InputStream in = resource.getInputStream();
			Properties properties = new Properties();
			try {
				properties.load(in);
			} finally {
				in.close();
			}

			ExpressionHandler expressionHandler = new DefaultExpressionHandler() {
				@Override
				public JexlContext getJexlContext() {
					JexlContext elContext = new MapContext();
					elContext.set("env", System.getenv());
					return elContext;
				}
			};

			for (Map.Entry<?, ?> entry : properties.entrySet()) {
				String text = (String) entry.getValue();
				Object value = text;
				if (StringUtils.isNotEmpty(text)) {
					Expression expression = expressionHandler.compile(text);
					if (expression != null) {
						value = expression.evaluate();
					}
				}
				configureStore.set((String) entry.getKey(), value);
			}
		}
	}

	private RuleSet exportRuleSet() throws Exception {
		// 输出版本信息
		ConsoleUtils.outputLoadingInfo("Initializing "
				+ DoradoAbout.getProductTitle() + " engine...");
		ConsoleUtils.outputLoadingInfo("[vendor: " + DoradoAbout.getVendor()
				+ "]");

		ConfigureStore configureStore = Configure.getStore();

		// 处理DoradoHome
		configureStore.set(HOME_PROPERTY, doradoHome);
		ConsoleUtils
				.outputLoadingInfo("[dorado home: "
						+ StringUtils.defaultString(doradoHome,
								"<not assigned>") + "]");

		// 创建一个临时的ResourceLoader
		ResourceLoader resourceLoader = new BaseResourceLoader();

		// 读取configure.properties
		loadConfigureProperties(configureStore, resourceLoader,
				WEB_CONFIGURE_LOCATION, false);

		if (StringUtils.isNotEmpty(doradoHome)) {
			String configureLocation = HOME_LOCATION_PREFIX
					+ "configure.properties";
			loadConfigureProperties(configureStore, resourceLoader,
					configureLocation, false);
		}

		List<String> contextLocations = new ArrayList<String>();
		// findPackages
		for (PackageInfo packageInfo : PackageManager.getPackageInfoMap()
				.values()) {
			ConsoleUtils.outputLoadingInfo("Package [" + packageInfo.getName()
					+ " - " + packageInfo.getVersion() + "] found.");

			// 处理Spring的配置文件
			pushLocations(contextLocations, packageInfo.getContextLocations());
		}

		String contextLocationsFromProperties = configureStore
				.getString(CONTEXT_CONFIG_PROPERTY);
		if (contextLocationsFromProperties != null) {
			pushLocations(contextLocations, contextLocationsFromProperties);
		}

		Resource resource;
		resource = resourceLoader
				.getResource(getRealResourcePath(HOME_CONTEXT_XML));
		if (resource.exists()) {
			pushLocations(contextLocations, HOME_CONTEXT_XML);
		}

		configureStore.set(CONTEXT_CONFIG_PROPERTY,
				StringUtils.join(getRealResourcesPath(contextLocations), ';'));
		ConsoleUtils.outputConfigureItem(CONTEXT_CONFIG_PROPERTY);

		CommonContext.init();
		try {
			EngineStartupListenerManager.notifyStartup();

			RuleTemplateManager ruleTemplateManager = getRuleTemplateBuilder()
					.getRuleTemplateManager();
			return getRuleSetBuilder().buildRuleSet(ruleTemplateManager);
		} finally {
			CommonContext.dispose();
		}
	}

	public static RuleSet getRuleSet(String doradoHome) throws Exception {
		if (StringUtils.isEmpty(doradoHome)) {
			doradoHome = System.getenv("DORADO_HOME");
		}
		StandaloneRuleSetExporter instance = new StandaloneRuleSetExporter(
				doradoHome);
		RuleSet ruleSet = instance.exportRuleSet();

		Map<String, PackageInfo> finalPackageInfos = new HashMap<String, PackageInfo>(
				PackageManager.getPackageInfoMap());
		Collection<PackageInfo> packageInfos = ruleSet.getPackageInfos();
		for (PackageInfo packageInfo : packageInfos) {
			finalPackageInfos.put(packageInfo.getName(), packageInfo);
		}

		packageInfos.clear();
		packageInfos.addAll(finalPackageInfos.values());

		return ruleSet;
	}
}
