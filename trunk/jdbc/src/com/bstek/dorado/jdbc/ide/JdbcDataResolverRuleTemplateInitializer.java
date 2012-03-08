package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

public class JdbcDataResolverRuleTemplateInitializer implements
		com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer {

	public static final String TRANSACTION_MANAGERS_STORE_KEY = "jdbc.ide.transactionManagers";
	public static final String TRANSACTION_DEFINITIONS_STORE_KEY = "jdbc.ide.transactionDefinitions";
	
	@Override
	public void initRuleTemplate(RuleTemplate resolverRule,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String[] propertyNameAry = new String[]{"transactionManager", "transactionDefinition"};
		String[] storeKeyAry = new String[]{TRANSACTION_MANAGERS_STORE_KEY, TRANSACTION_DEFINITIONS_STORE_KEY};
		for (int i=0; i<propertyNameAry.length; i++){
			String propertyName = propertyNameAry[i];
			String storeKey = storeKeyAry[i];
			String valueStr = configureStore.getString(storeKey);
			if (StringUtils.isNotEmpty(valueStr)) {
				String[] typeAry = StringUtils.split(valueStr, ",");
				
				PropertyTemplate jdbcTypeProperty = resolverRule.getProperty(propertyName);
				if (jdbcTypeProperty == null) {
					jdbcTypeProperty = new PropertyTemplate(propertyName);
					resolverRule.addProperty(jdbcTypeProperty);
				}
				jdbcTypeProperty.setHighlight(1);
				jdbcTypeProperty.setEnumValues(typeAry);
			}
		}
	}

}
