package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.web.ConsoleUtils;

public class TableRuleTemplateInitializer implements RuleTemplateInitializer {

	public static final String NAMESPACES_STORE_KEY = "jdbc.ide.namespaces";
	
	@Override
	public void initRuleTemplate(RuleTemplate tableRule,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String propertyName = "namespace";
		String storeKey = NAMESPACES_STORE_KEY;
		String valueStr = configureStore.getString(storeKey);
		if (StringUtils.isNotEmpty(valueStr)) {
			String[] typeAry = StringUtils.split(valueStr, ",");
			
			PropertyTemplate property = tableRule.getPrimitiveProperty(propertyName);
			if (property == null) {
				property = new PropertyTemplate(propertyName);
				tableRule.addPrimitiveProperty(property);
			}
			property.setEditor("jdbc:service:list-space");
			property.setHighlight(1);
			property.setEnumValues(typeAry);
		} else {
			ConsoleUtils.outputLoadingInfo("[WARN] " + NAMESPACES_STORE_KEY + " is null.");
		}
	}

}
